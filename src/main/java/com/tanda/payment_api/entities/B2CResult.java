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

    @Column(name = "result_type")
    private Integer resultType;

    @Column(name = "result_code")
    private Integer resultCode;

    @Column(name = "result_description")
    private String resultDescription;

    @Column(name = "originator_conversation_id")
    private String originatorConversationId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "queue_timeout_url")
    private String queueTimeoutUrl;

    @Column(name = "transaction_amount")
    private String transactionAmount;

    @Column(name = "transaction_receipt")
    private String transactionReceipt;

    @Column(name = "b2c_recipient_registered_customer")
    private Boolean b2cRecipientRegisteredCustomer;

    @Column(name = "b2c_charges_paid_account_available_funds")
    private BigDecimal b2cChargesPaidAccountAvailableFunds;

    @Column(name = "receiver_party_public_name")
    private String receiverPartyPublicName;

    @Column(name = "transaction_completed_date_time")
    private LocalDateTime transactionCompletedDateTime;

    @Column(name = "b2c_utility_account_available_funds")
    private BigDecimal b2CUtilityAccountAvailableFunds;

    @Column(name = "b2c_working_account_available_funds")
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
