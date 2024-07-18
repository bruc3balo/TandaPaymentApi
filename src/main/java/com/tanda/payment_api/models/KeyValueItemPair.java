package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyValueItemPair {

    @JsonProperty("Key")
    private String key;

    @JsonProperty("Value")
    private String value;

    public KeyValueItemPair() {
    }
}
