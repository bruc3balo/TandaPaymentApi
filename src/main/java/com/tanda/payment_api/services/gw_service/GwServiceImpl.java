package com.tanda.payment_api.services.gw_service;

import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.models.GwRequest;
import com.tanda.payment_api.repositories.GwPendingRequestsRepository;
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

    public GwServiceImpl(GwPendingRequestsRepository pendingRequestsRepository) {
        this.pendingRequestsRepository = pendingRequestsRepository;
    }

    @Override
    public Page<GwPendingRequest> fetchPendingRequests(Pageable pageable) {
        return pendingRequestsRepository.findByRetryCountLessThan(MAX_RETRY_COUNT, pageable);
    }

    @Override
    public GwPendingRequest createRequest(@Valid GwRequest gwRequest) {

        GwPendingRequest pendingRequests = GwPendingRequest.builder()
                .amount(gwRequest.getAmount())
                .mobileNumber(gwRequest.getMobileNumber())
                .transactionId(gwRequest.getTransactionId())
                .retryCount(0)
                .statusReason("Newly created gw request")
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
