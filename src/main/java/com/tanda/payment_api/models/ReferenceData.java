package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanda.payment_api.globals.GlobalVariables;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReferenceData {

    @JsonProperty(GlobalVariables.REFERENCE_ITEM)
    private KeyValueItemPair referenceItem;

    public ReferenceData() {

    }
}
