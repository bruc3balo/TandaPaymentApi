package com.tanda.payment_api.cron;

import com.tanda.payment_api.entities.B2CTransactions;
import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.enums.B2CTransactionStatus;
import com.tanda.payment_api.models.GwResponse;
import com.tanda.payment_api.services.b2c.B2CTransactionService;
import com.tanda.payment_api.services.gw_service.GwService;
import com.tanda.payment_api.services.kafka.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.tanda.payment_api.globals.GlobalVariables.MAX_RETRY_COUNT;

@Component
@Slf4j
public class PaymentScheduledTasks {

    private final GwService gwService;
    private final B2CTransactionService b2CTransactionService;
    private final KafkaService kafkaService;


    public PaymentScheduledTasks(GwService gwService, B2CTransactionService b2CTransactionService, KafkaService kafkaService) {
        this.gwService = gwService;
        this.b2CTransactionService = b2CTransactionService;
        this.kafkaService = kafkaService;
    }


    @Scheduled(fixedRate = 10000)
    public void sendPaymentRequests() {
        Page<GwPendingRequest> gwPendingRequests = gwService.fetchPendingRequests(PageRequest.of(0, 50));

        for (GwPendingRequest request : gwPendingRequests) {

            try {
                B2CTransactions b2CTransactions = b2CTransactionService.initiateB2C(request);

                gwService.deleteGwPendingRequest(request);
                log.debug("Cleared GwRequest {}", request.getId());

                GwResponse gwResponse = GwResponse.builder()
                        .id(request.getId())
                        .status(B2CTransactionStatus.FAILED.name())
                        .build();

                kafkaService.sendResponse(gwResponse);
                log.debug("Sent GwResponse {}", gwResponse.getId());

            } catch (Exception e) {

                boolean isFail = Objects.equals(request.getRetryCount() + 1, MAX_RETRY_COUNT);
                if (isFail) {

                    gwService.deleteGwPendingRequest(request);
                    log.debug("Cleared GwRequest {}", request.getId());

                    GwResponse gwResponse = GwResponse.builder()
                            .id(request.getId())
                            .status(B2CTransactionStatus.FAILED.name())
                            .build();

                    kafkaService.sendResponse(gwResponse);
                    continue;
                }

                request.setRetryCount(request.getRetryCount() + 1);
                request.setStatusReason(e.getMessage());
                gwService.updateGwPendingRequest(request);
            }
        }

    }

}
