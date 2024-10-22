package com.tanda.payment_api.repositories;

import com.tanda.payment_api.entities.GwPendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GwPendingRequestsRepository extends JpaRepository<GwPendingRequest, String> {
    Page<GwPendingRequest> findByRetryCountLessThan(Integer maxRetryCount, Pageable pageable);
    Optional<GwPendingRequest> findByTransactionId(String transactionId);
}
