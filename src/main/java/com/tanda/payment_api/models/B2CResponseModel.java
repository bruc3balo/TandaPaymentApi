package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GPaymentVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class B2CResponseModel {

    @JsonProperty(GPaymentVariables.CONVERSATION_ID)
    private String conversationID;

    @JsonProperty(GPaymentVariables.ORIGINATOR_CONVERSATION_ID)
    private String originatorConversationID;

    @JsonProperty(GPaymentVariables.RESPONSE_CODE)
    private String responseCode;

    @JsonProperty(GPaymentVariables.RESPONSE_DESCRIPTION)
    private String responseDescription;

    private transient B2CRequestForm form;

    public B2CResponseModel() {

    }
}
