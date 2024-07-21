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

import java.time.LocalDateTime;

@Entity
@Table(name = "b2c_requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class B2CRequest {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "initiator_name")
    private String initiatorName;

    @Column(name = "originator_conversation_id")
    private String originatorConversationId;

    @Column(name = "security_credential", length = 500)
    @JsonIgnore
    //Todo hide from public or generate on demand
    private String securityCredential;

    @Column(name = "command_id")
    private String commandId;

    private Integer amount;

    @Column(name = "partya")
    private Integer partyA;

    @Column(name = "partyb")
    private Long partyB;

    private String remarks;

    @Column(name = "time_out_url")
    private String timeOutUrl;

    @Column(name = "result_url")
    private String resultUrl;

    private String occasion;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "response_description")
    private String responseDescription;

    @JoinColumn(name = "b2c_request_id")
    @OneToOne(mappedBy = "request")
    @JsonIgnore
    private B2CTransactions b2CTransaction;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public B2CRequest() {

    }
}
