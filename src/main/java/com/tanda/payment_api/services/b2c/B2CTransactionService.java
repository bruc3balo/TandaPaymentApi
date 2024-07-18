package com.tanda.payment_api.services.b2c;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payment_api.entities.B2CTransactions;
import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.models.B2CRequestBodyForm;
import com.tanda.payment_api.models.B2CResultRequestBodyForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface B2CTransactionService {
    B2CTransactions initiateB2C(GwPendingRequest form) throws JsonProcessingException;
    B2CTransactions initiateB2C(B2CRequestBodyForm form) throws JsonProcessingException;
    B2CTransactions onB2cResult(B2CResultRequestBodyForm form);
    Page<B2CTransactions> getB2cTransactions(Pageable pageable);
}
