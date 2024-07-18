package com.tanda.payment_api.entities;

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
@Table(name = "gw_pending_requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GwPendingRequest {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    private BigDecimal amount;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "status_reason")
    private String statusReason;

    @Column(name = "retry_count")
    private Integer retryCount;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public GwPendingRequest() {

    }
}
