package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GlobalVariables;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;

import static com.tanda.payment_api.globals.GlobalVariables.*;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class B2CRequestForm extends B2CRequestBodyForm {


    @JsonProperty(QUEUE_TIME_OUT_URL)
    @NotBlank(message = QUEUE_TIME_OUT_URL + _REQUIRED)
    @URL(message = "Invalid " + QUEUE_TIME_OUT_URL + "format")
    private String queueTimeOutURL;

    @JsonProperty(RESULT_URL)
    @NotBlank(message = RESULT_URL + _REQUIRED)
    @URL(message = "Invalid " + RESULT_URL + "format")
    private String resultURL;

    @JsonProperty(GlobalVariables.ORIGINATOR_CONVERSATION_ID)
    @NotBlank(message = GlobalVariables.ORIGINATOR_CONVERSATION_ID + _REQUIRED)
    private String originatorConversationID;

    public B2CRequestForm() {

    }

}
