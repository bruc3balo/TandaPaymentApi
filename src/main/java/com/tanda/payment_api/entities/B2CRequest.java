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
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class B2CRequest {

    private String initiatorName;
    private String originatorConversationID;
    private String securityCredential;
    private String commandId;
    private BigDecimal amount;
    private String partyA;
    private String partyB;
    private String remarks;
    private String timeOutUrl;
    private String resultUrl;
    private String occassion;
    private String responseCode;
    private String responseDescription;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    private LocalDateTime respondedAt;


    public B2CRequest() {

    }
}
