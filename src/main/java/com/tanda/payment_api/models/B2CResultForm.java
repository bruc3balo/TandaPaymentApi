package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GlobalVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class B2CResultForm {

    @JsonProperty(GlobalVariables.RESULT_TYPE)
    private Integer resultType;

    @JsonProperty(GlobalVariables.RESULT_CODE)
    private Integer resultCode;

    @JsonProperty(GlobalVariables.RESULT_DESC)
    private String resultDesc;

    @JsonProperty(GlobalVariables.ORIGINATOR_CONVERSATION_ID)
    private String originatorConversationID;

    @JsonProperty(GlobalVariables.CONVERSATION_ID)
    private String conversationID;

    @JsonProperty(GlobalVariables.TRANSACTION_ID)
    private String transactionID;

    @JsonProperty(GlobalVariables.RESULT_PARAMETERS)
    private ResultParameters resultParameters;

    @JsonProperty(GlobalVariables.REFERENCE_DATA)
    private ReferenceData referenceData;

    public B2CResultForm() {

    }
}
