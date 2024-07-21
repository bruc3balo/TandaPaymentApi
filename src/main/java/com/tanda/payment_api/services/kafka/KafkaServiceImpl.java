package com.tanda.payment_api.services.kafka;

import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.exceptions.HttpStatusException;
import com.tanda.payment_api.globals.GlobalVariables;
import com.tanda.payment_api.models.GwRequest;
import com.tanda.payment_api.models.GwResponse;
import com.tanda.payment_api.services.b2c.B2CTransactionService;
import com.tanda.payment_api.services.gw_service.GwService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.tanda.payment_api.globals.GlobalVariables.KAFKA_GW_GROUP_ID;
import static com.tanda.payment_api.globals.GlobalVariables.KAFKA_GW_REQUEST_CONTAINER_FACTORY;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, GwResponse> gwResponseKafkaTemplate;
    private final GwService gwService;
    private final B2CTransactionService b2cTransactionService;

    public KafkaServiceImpl(@Qualifier(GlobalVariables.GW_RESPONSE_KAFKA_TEMPLATE) KafkaTemplate<String, GwResponse> gwResponseKafkaTemplate, GwService gwService, B2CTransactionService b2cTransactionService) {
        this.gwResponseKafkaTemplate = gwResponseKafkaTemplate;
        this.gwService = gwService;
        this.b2cTransactionService = b2cTransactionService;
    }

    @KafkaListener(topics = GlobalVariables.KAFKA_GW_REQUEST_TOPIC, groupId = KAFKA_GW_GROUP_ID, containerFactory = KAFKA_GW_REQUEST_CONTAINER_FACTORY)
    public void onReceivePaymentRequest(@Payload GwRequest gwRequest, Acknowledgment ack) {
        log.debug("Received GwRequest : {}", gwRequest.getTransactionId());

        //check if transaction has been recorded
        b2cTransactionService.findByTransactionId(gwRequest.getTransactionId()).ifPresent((t) -> {
            throw HttpStatusException.duplicate("B2C Transaction already exists");
        });

        GwPendingRequest pendingRequest = gwService.createRequest(gwRequest);
        ack.acknowledge();
    }

    @Override
    public CompletableFuture<SendResult<String, GwResponse>> sendResponse(GwResponse gwResponse) {
        log.debug("Sending GwResponse : {}", gwResponse.getId());
        return gwResponseKafkaTemplate.send(GlobalVariables.KAFKA_GW_RESPONSE_TOPIC, gwResponse.getId(), gwResponse);
    }

}
