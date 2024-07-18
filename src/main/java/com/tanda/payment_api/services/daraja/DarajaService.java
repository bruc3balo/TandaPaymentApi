package com.tanda.payment_api.services.daraja;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.models.*;
import jakarta.validation.Valid;

public interface DarajaService {
    DarajaAuthResponse authenticate() throws JsonProcessingException;

    B2CResponseModel initiateB2c(String accessToken, String originatorConversationID, @Valid GwPendingRequest gwRequest) throws JsonProcessingException;

    B2CResponseModel initiateB2c(String accessToken, String originatorConversationID, B2CRequestBodyForm form) throws JsonProcessingException;
}
