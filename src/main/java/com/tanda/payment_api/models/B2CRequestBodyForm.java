package com.tanda.payment_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import static com.tanda.payment_api.globals.GPaymentVariables.*;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class B2CRequestBodyForm {

    @NotBlank(message = TRANSACTION_ID + _REQUIRED)
    @JsonProperty(TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(INITIATOR_NAME)
    @NotBlank(message = INITIATOR_NAME + _REQUIRED)
    private String initiatorName;

    //Assumed the password has been already generated
    @JsonProperty(SECURITY_CREDENTIALS)
    @NotBlank(message = SECURITY_CREDENTIALS + _REQUIRED)
    private String securityCredential;

    @JsonProperty(COMMAND_ID)
    @NotBlank(message = COMMAND_ID + _REQUIRED)
    private String commandID;

    @JsonProperty(AMOUNT)
    @Min(value = 10, message = AMOUNT+" must be greater than 10")
    @Max(value = 150_000, message = AMOUNT+"must be less than 150,000")
    private Integer amount;

    @JsonProperty(PARTY_A)
    @Pattern(regexp = "^\\d{5,6}$", message = PARTY_A+" must be 5 or 6 digits")
    private String partyA;

    @JsonProperty(PARTY_B)
    @Pattern(regexp = "^254.*$", message = PARTY_B+" number must start with 254")
    private String partyB;

    @JsonProperty(REMARKS)
    @Length(max = 100)
    private String remarks;

    @JsonProperty(OCCASSION)
    @Length(max = 100)
    private String occasion;

    public B2CRequestBodyForm() {

    }
}
