package com.tanda.payment_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "b2c_result")
@Getter
@Setter
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class B2CResult {
    @Id
    @UuidGenerator
    private String id;

    private Integer resultType;
    private Integer resultCode;
    private String resultDescription;
    private String originatorConversationId;
    private String conversationId;
    private String transactionId;
    private String queueTimeoutUrl;
    private String transactionAmount;
    private String transactionReceipt;
    private Boolean b2cRecipientRegisteredCustomer;
    private BigDecimal b2cChargesPaidAccountAvailableFunds;
    private String receiverPartyPublicName;
    private LocalDateTime transactionCompletedDateTime;
    private BigDecimal b2CUtilityAccountAvailableFunds;
    private BigDecimal b2CWorkingAccountAvailableFunds;

    @JoinColumn(name = "b2c_request_id")
    @OneToOne(mappedBy = "result")
    @JsonIgnore
    private B2CTransactions b2CTransaction;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public B2CResult() {

    }
}
