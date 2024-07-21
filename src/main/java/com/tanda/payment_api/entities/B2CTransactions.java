package com.tanda.payment_api.entities;

import com.tanda.payment_api.enums.B2CTransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "b2c_transactions",
        indexes = @Index(name = "conversation_id_index", columnList = "conversation_id")
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class B2CTransactions {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "conversation_id", unique = true)
    private String conversationId;

    @Enumerated(EnumType.STRING)
    private B2CTransactionStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "b2c_request_id")
    private B2CRequest request;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "b2c_result_id")
    private B2CResult result;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public B2CTransactions() {

    }
}
