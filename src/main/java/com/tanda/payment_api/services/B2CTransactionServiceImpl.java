package com.tanda.payment_api.services;

import com.tanda.payment_api.repositories.B2CTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class B2CTransactionServiceImpl implements B2CTransactionService {

    private final B2CTransactionRepository b2cTransactionRepository;

    public B2CTransactionServiceImpl(B2CTransactionRepository b2cTransactionRepository) {
        this.b2cTransactionRepository = b2cTransactionRepository;
    }
}
