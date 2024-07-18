package com.tanda.payment_api.repositories;

import com.tanda.payment_api.entities.B2CTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface B2CTransactionRepository extends JpaRepository<B2CTransactions, String>, JpaSpecificationExecutor<B2CTransactions> {
    Optional<B2CTransactions> findByConversationIdAndConversationIdIsNotNull(String conversationId);
}
