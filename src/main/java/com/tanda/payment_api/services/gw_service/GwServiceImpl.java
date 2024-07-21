package com.tanda.payment_api.services.gw_service;

import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.exceptions.HttpStatusException;
import com.tanda.payment_api.models.GwRequest;
import com.tanda.payment_api.repositories.GwPendingRequestsRepository;
import com.tanda.payment_api.services.b2c.B2CTransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tanda.payment_api.globals.GlobalVariables.MAX_RETRY_COUNT;

@Service
@Transactional
public class GwServiceImpl implements GwService {

    private final GwPendingRequestsRepository pendingRequestsRepository;
    private final B2CTransactionService b2CTransactionService;

    public GwServiceImpl(GwPendingRequestsRepository pendingRequestsRepository, B2CTransactionService b2CTransactionService) {
        this.pendingRequestsRepository = pendingRequestsRepository;
        this.b2CTransactionService = b2CTransactionService;
    }

    @Override
    public Page<GwPendingRequest> fetchPendingRequests(Pageable pageable) {
        return pendingRequestsRepository.findByRetryCountLessThan(MAX_RETRY_COUNT, pageable);
    }

    @Override
    public GwPendingRequest createRequest(@Valid GwRequest gwRequest) {

        //check for duplicate requests
        pendingRequestsRepository.findByTransactionId(gwRequest.getTransactionId()).ifPresent((t) -> {
            throw HttpStatusException.duplicate("GwRequest already exists");
        });

        //check for duplicate transactions
        b2CTransactionService.findByTransactionId(gwRequest.getTransactionId()).ifPresent((t) -> {
            throw HttpStatusException.duplicate("B2C Transaction already exists");
        });



        GwPendingRequest pendingRequests = GwPendingRequest.builder()
                .amount(gwRequest.getAmount())
                .mobileNumber(gwRequest.getMobileNumber())
                .transactionId(gwRequest.getTransactionId())
                .retryCount(0)
                .statusReason("New")
                .build();

        return pendingRequestsRepository.save(pendingRequests);
    }

    @Override
    public GwPendingRequest updateGwPendingRequest(GwPendingRequest pendingRequests) {
        return pendingRequestsRepository.save(pendingRequests);
    }

    @Override
    public void deleteGwPendingRequest(GwPendingRequest pendingRequests) {
        pendingRequestsRepository.delete(pendingRequests);
    }
}
