package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GlobalVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class B2CResponseModel {

    @JsonProperty(GlobalVariables.CONVERSATION_ID)
    private String conversationID;

    @JsonProperty(GlobalVariables.ORIGINATOR_CONVERSATION_ID)
    private String originatorConversationID;

    @JsonProperty(GlobalVariables.RESPONSE_CODE)
    private String responseCode;

    @JsonProperty(GlobalVariables.RESPONSE_DESCRIPTION)
    private String responseDescription;

    private transient B2CRequestForm form;

    public B2CResponseModel() {

    }
}
