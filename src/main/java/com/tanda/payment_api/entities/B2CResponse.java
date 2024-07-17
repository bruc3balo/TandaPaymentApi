package com.tanda.payment_api.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class B2CResponse {

    private Integer responseResultType;
    private Integer responseResultCode;
    private String responseResultDescription;
    private String responseOriginatorConversationId;
    private String responseConversationId;
    private String responseTransactionId;
    private String responseQueueTimeoutUrl;
    private String responseTransactionAmount;
    private String responseTransactionReceipt;
    private Boolean b2cRecipientRegisteredCustomer;
    private BigDecimal b2cChargesPaidAccountAvailableFunds;
    private String receiverPartyPublicName;
    private LocalDateTime transactionCompletedDateTime;
    private BigDecimal B2CUtilityAccountAvailableFunds;
    private BigDecimal B2CWorkingAccountAvailableFunds;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    private LocalDateTime respondedAt;

    public B2CResponse() {

    }
}
