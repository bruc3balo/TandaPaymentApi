package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GPaymentVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class B2CResultForm {

    @JsonProperty(GPaymentVariables.RESULT_TYPE)
    private Integer resultType;

    @JsonProperty(GPaymentVariables.RESULT_CODE)
    private Integer resultCode;

    @JsonProperty(GPaymentVariables.RESULT_DESC)
    private String resultDesc;

    @JsonProperty(GPaymentVariables.ORIGINATOR_CONVERSATION_ID)
    private String originatorConversationID;

    @JsonProperty(GPaymentVariables.CONVERSATION_ID)
    private String conversationID;

    @JsonProperty(GPaymentVariables.TRANSACTION_ID)
    private String transactionID;

    @JsonProperty(GPaymentVariables.RESULT_PARAMETERS)
    private ResultParameters resultParameters;

    @JsonProperty(GPaymentVariables.REFERENCE_DATA)
    private ReferenceData referenceData;

    public B2CResultForm() {

    }
}
