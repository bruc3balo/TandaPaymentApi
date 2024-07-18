package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GlobalVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class B2CResultRequestBodyForm {

    @JsonProperty(GlobalVariables.RESULT)
    private B2CResultForm resultForm;

    public B2CResultRequestBodyForm() {

    }
}
