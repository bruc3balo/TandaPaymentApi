package com.tanda.payment_api.services.b2c;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payment_api.entities.B2CRequest;
import com.tanda.payment_api.entities.B2CResult;
import com.tanda.payment_api.entities.B2CTransactions;
import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.enums.B2CTransactionStatus;
import com.tanda.payment_api.exceptions.HttpStatusException;
import com.tanda.payment_api.globals.GlobalVariables;
import com.tanda.payment_api.models.*;
import com.tanda.payment_api.repositories.B2CTransactionRepository;
import com.tanda.payment_api.services.daraja.DarajaService;
import com.tanda.payment_api.services.kafka.KafkaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tanda.payment_api.globals.GlobalVariables.TRANSACTION_COMPLETED_DATE_TIME_DATE_FORMAT;

@Service
@Transactional
@Slf4j
public class B2CTransactionServiceImpl implements B2CTransactionService {

    private final B2CTransactionRepository b2cTransactionRepository;
    private final DarajaService darajaService;
    private final KafkaService kafkaService;

    public B2CTransactionServiceImpl(B2CTransactionRepository b2cTransactionRepository, DarajaService darajaService, KafkaService kafkaService) {
        this.b2cTransactionRepository = b2cTransactionRepository;
        this.darajaService = darajaService;
        this.kafkaService = kafkaService;
    }

    //From Pending Requests Source
    @Override
    public B2CTransactions initiateB2C(GwPendingRequest requests) throws JsonProcessingException {

        // Authenticate
        DarajaAuthResponse authResponse = darajaService.authenticate();
        String accessToken = authResponse.getAccessToken();

        // Send b2c request
        B2CTransactions b2CTransactions = B2CTransactions.builder()
                .status(B2CTransactionStatus.INITIATING)
                .build();
        b2CTransactions = b2cTransactionRepository.save(b2CTransactions);

        //Receive b2c response
        B2CResponseModel b2CResponseModel = darajaService.initiateB2cGWRequest(accessToken, b2CTransactions.getId(), requests);
        B2CTransactions transaction = doInitiateB2c(b2CResponseModel, b2CTransactions);

        //Assert that status should be pending
        if (transaction.getStatus() != B2CTransactionStatus.PENDING) {
            throw HttpStatusException.failed("Transaction " + transaction.getStatus().name());
        }

        return transaction;
    }

    //From Api Source
    @Override
    public B2CTransactions initiateB2C(@Valid B2CRequestBodyForm requestBodyForm) throws JsonProcessingException {

        // Authenticate
        DarajaAuthResponse authResponse = darajaService.authenticate();
        String accessToken = authResponse.getAccessToken();

        // Send b2c request
        B2CTransactions b2CTransactions = B2CTransactions.builder()
                .status(B2CTransactionStatus.INITIATING)
                .build();
        b2CTransactions = b2cTransactionRepository.save(b2CTransactions);

        //Receive b2c response
        B2CResponseModel b2CResponseModel = darajaService.initiateB2cApiRequest(accessToken, b2CTransactions.getId(), requestBodyForm);
        return doInitiateB2c(b2CResponseModel, b2CTransactions);
    }

    //Do common functions from initiateB2C
    private B2CTransactions doInitiateB2c(B2CResponseModel b2CResponseModel, B2CTransactions b2CTransactions) {

        log.debug("B2C :: {} => {}", b2CResponseModel.getOriginatorConversationID(), b2CResponseModel.getResponseDescription());

        // Persist request details

        B2CRequestForm form = b2CResponseModel.getForm();

        B2CRequest b2CRequest = B2CRequest.builder()
                .initiatorName(form.getInitiatorName())
                .originatorConversationId(b2CResponseModel.getOriginatorConversationID())
                .securityCredential(form.getSecurityCredential())
                .commandId(form.getCommandID())
                .amount(form.getAmount())
                .partyA(Integer.valueOf(form.getPartyA()))
                .partyB(Long.valueOf(form.getPartyB()))
                .remarks(form.getRemarks())
                .occasion(form.getOccasion())
                .resultUrl(b2CResponseModel.getForm().getResultURL())
                .timeOutUrl(b2CResponseModel.getForm().getQueueTimeOutURL())
                .responseCode(b2CResponseModel.getResponseCode())
                .responseDescription(b2CResponseModel.getResponseDescription())
                .build();

        // Record transaction
        b2CTransactions.setRequest(b2CRequest);
        b2CTransactions.setConversationId(b2CResponseModel.getConversationID());
        b2CTransactions.setStatus(B2CTransactionStatus.PENDING);

        log.debug("Persisting b2c request {}", b2CTransactions.getConversationId());

        return b2cTransactionRepository.save(b2CTransactions);
    }

    //Receive result of b2c from daraja
    @Override
    public B2CTransactions onB2cResult(B2CResultRequestBodyForm form) {

        log.debug("Received b2c result");

        B2CResultForm resultForm = form.getResultForm();
        B2CTransactions b2CTransactions = b2cTransactionRepository.findByConversationIdAndConversationIdIsNotNull(resultForm.getConversationID()).orElseThrow(() -> HttpStatusException.notFound("Transaction not found"));

        if (b2CTransactions.getResult() != null) throw HttpStatusException.duplicate("Result already recorded");

        Integer resultCode = resultForm.getResultCode();
        Integer resultType = resultForm.getResultType();
        String resultDescription = resultForm.getResultDesc();

        String originatorConversationID = resultForm.getOriginatorConversationID();
        assert (b2CTransactions.getRequest().getOriginatorConversationId().equals(originatorConversationID));

        String conversationID = resultForm.getConversationID();

        String transactionID = resultForm.getTransactionID();

        if (resultForm.getResultCode() != 0) {
            B2CResult b2CResult = B2CResult.builder()
                    .resultCode(resultCode)
                    .resultType(resultType)
                    .resultDescription(resultDescription)
                    .originatorConversationId(originatorConversationID)
                    .conversationId(conversationID)
                    .transactionId(transactionID)
                    .queueTimeoutUrl(resultForm.getReferenceData().getReferenceItem().getValue())
                    .build();

            b2CTransactions.setResult(b2CResult);
            b2CTransactions.setStatus(B2CTransactionStatus.FAILED);
        } else {

            Map<String, String> resultParameters = resultForm.getResultParameters().getResultParameter()
                    .stream().collect(Collectors.toMap(KeyValueItemPair::getKey, KeyValueItemPair::getValue));

            String transactionAmount = resultParameters.get(GlobalVariables.TRANSACTION_AMOUNT);
            String transactionReceipt = resultParameters.get(GlobalVariables.TRANSACTION_RECEIPT);
            Boolean b2cRecipientRegisteredCustomer = resultParameters.get(GlobalVariables.B2C_RECIPIENT_IS_REGISTERED_CUSTOMER).equals("Y");
            BigDecimal b2cChargesPaidAccountAvailableFunds = new BigDecimal(resultParameters.get(GlobalVariables.B2C_CHARGES_PAID_ACCOUNT_AVAILABLE_FUNDS));

            String receiverPartyPublicName = resultParameters.get(GlobalVariables.RECEIVER_PARTY_PUBLIC_NAME);
            String[] receiverInfo = receiverPartyPublicName.split("-");
            String number = receiverInfo[0];
            String name = receiverInfo[1];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TRANSACTION_COMPLETED_DATE_TIME_DATE_FORMAT);
            LocalDateTime transactionCompletedDateTime = LocalDateTime.parse(resultParameters.get(GlobalVariables.TRANSACTION_COMPLETED_DATE_TIME), formatter);

            BigDecimal b2CUtilityAccountAvailableFunds = new BigDecimal(resultParameters.get(GlobalVariables.B2C_UTILITY_ACCOUNT_AVAILABLE_FUNDS));
            BigDecimal b2CWorkingAccountAvailableFunds = new BigDecimal(resultParameters.get(GlobalVariables.B2C_WORKING_ACCOUNT_AVAILABLE_FUNDS));

            B2CResult b2CResult = B2CResult.builder()
                    .resultCode(resultCode)
                    .resultType(resultType)
                    .resultDescription(resultDescription)
                    .originatorConversationId(originatorConversationID)
                    .conversationId(conversationID)
                    .transactionId(transactionID)
                    .queueTimeoutUrl(resultForm.getReferenceData().getReferenceItem().getValue())
                    .transactionAmount(transactionAmount)
                    .transactionReceipt(transactionReceipt)
                    .b2cRecipientRegisteredCustomer(b2cRecipientRegisteredCustomer)
                    .b2cChargesPaidAccountAvailableFunds(b2cChargesPaidAccountAvailableFunds)
                    .receiverPartyPublicName(receiverPartyPublicName)
                    .transactionCompletedDateTime(transactionCompletedDateTime)
                    .b2CUtilityAccountAvailableFunds(b2CUtilityAccountAvailableFunds)
                    .b2CWorkingAccountAvailableFunds(b2CWorkingAccountAvailableFunds)
                    .build();

            b2CTransactions.setResult(b2CResult);
            b2CTransactions.setStatus(B2CTransactionStatus.SUCCESS);
        }

        b2CTransactions = b2cTransactionRepository.save(b2CTransactions);
        B2CResult b2CResult = b2CTransactions.getResult();

        GwResponse gwResponse = GwResponse.builder()
                .id(b2CTransactions.getId())
                .mpesaReference(b2CResult.getTransactionReceipt())
                .status(b2CTransactions.getStatus().name())
                .build();

        kafkaService.sendResponse(gwResponse);

        return b2CTransactions;
    }

    @Override
    public Optional<B2CTransactions> findByTransactionId(String transactionId) {
        if(transactionId == null) return Optional.empty();
        return b2cTransactionRepository.findById(transactionId);
    }

    //Retrieve list of transactions
    @Override
    public Page<B2CTransactions> getB2cTransactions(Pageable pageable) {
        return b2cTransactionRepository.findAll(pageable);
    }

    //To be used only for test purposes
    // Not for prod
    @Override
    public void removeTransaction(String id) {

        //Not necessary for performance
        if (b2cTransactionRepository.findById(id).isEmpty())
            throw HttpStatusException.notFound("Transaction not found");

        b2cTransactionRepository.deleteById(id);


    }

}
