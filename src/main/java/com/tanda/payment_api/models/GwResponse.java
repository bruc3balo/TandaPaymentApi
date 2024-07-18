package com.tanda.payment_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GwResponse {

    private String id;

    private String status;

    private String mpesaReference;

    public GwResponse() {

    }
}
