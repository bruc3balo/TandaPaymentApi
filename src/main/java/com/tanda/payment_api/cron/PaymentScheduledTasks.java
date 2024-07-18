package com.tanda.payment_api.cron;

import com.tanda.payment_api.entities.B2CTransactions;
import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.services.b2c.B2CTransactionService;
import com.tanda.payment_api.services.gw_service.GwService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduledTasks {

    private final GwService gwService;
    private final B2CTransactionService b2CTransactionService;


    public PaymentScheduledTasks(GwService gwService, B2CTransactionService b2CTransactionService) {
        this.gwService = gwService;
        this.b2CTransactionService = b2CTransactionService;
    }


    @Scheduled(fixedRate = 3000)
    public void sendPaymentRequests() {
        Page<GwPendingRequest> gwPendingRequests = gwService.fetchPendingRequests(PageRequest.of(0, 30));

        for(GwPendingRequest request : gwPendingRequests) {
            try {
                B2CTransactions b2CTransactions = b2CTransactionService.initiateB2C(request);
            } catch (Exception e) {
                request.setRetryCount(request.getRetryCount() + 1);
                request.setStatusReason(e.getMessage());

                gwService.updateGwPendingRequest(request);
            }
        }

    }

}
