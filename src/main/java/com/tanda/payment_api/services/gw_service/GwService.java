package com.tanda.payment_api.services.gw_service;

import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.models.GwRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GwService {
    Page<GwPendingRequest> fetchPendingRequests(Pageable pageable);

    GwPendingRequest createRequest(@Valid GwRequest gwRequest);

    GwPendingRequest updateGwPendingRequest(GwPendingRequest pendingRequests);

    void deleteGwPendingRequest(GwPendingRequest pendingRequests);
}
