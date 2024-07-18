package com.tanda.payment_api.services.kafka;

import com.tanda.payment_api.models.GwResponse;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaService {
    CompletableFuture<SendResult<String, GwResponse>> sendResponse(GwResponse gwResponse);
}
