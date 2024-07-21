package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GPaymentVariables;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferenceData {

    @JsonProperty(GPaymentVariables.REFERENCE_ITEM)
    private KeyValueItemPair referenceItem;

    public ReferenceData() {

    }
}
