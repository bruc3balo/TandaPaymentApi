package com.tanda.payment_api.services.kafka;

import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.globals.GlobalVariables;
import com.tanda.payment_api.models.GwRequest;
import com.tanda.payment_api.models.GwResponse;
import com.tanda.payment_api.services.gw_service.GwService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, GwResponse> gwResponseKafkaTemplate;
    private final GwService gwService;

    public KafkaServiceImpl(@Qualifier(GlobalVariables.GW_RESPONSE_KAFKA_TEMPLATE) KafkaTemplate<String, GwResponse> gwResponseKafkaTemplate, GwService gwService) {
        this.gwResponseKafkaTemplate = gwResponseKafkaTemplate;
        this.gwService = gwService;
    }

    @KafkaListener
    public void onReceivePaymentRequest(GwRequest gwRequest) {
        log.debug("Received GwRequest");
        GwPendingRequest pendingRequest = gwService.createRequest(gwRequest);
    }

    @Override
    public CompletableFuture<SendResult<String, GwResponse>> sendResponse(GwResponse gwResponse) {
        return gwResponseKafkaTemplate.sendDefault(gwResponse.getId(), gwResponse);
    }
}
