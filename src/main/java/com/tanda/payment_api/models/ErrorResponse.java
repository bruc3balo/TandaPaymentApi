package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    public ErrorResponse() {

    }
}
