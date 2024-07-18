package com.tanda.payment_api.services.daraja;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanda.payment_api.entities.GwPendingRequest;
import com.tanda.payment_api.exceptions.HttpStatusException;
import com.tanda.payment_api.models.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

@Service
@Slf4j
public class DarajaServiceImpl implements DarajaService {

    @Value("${daraja.b2c.url}")
    private String darajaB2cUrl;

    @Value("${daraja.auth.url}")
    private String darajaAuthUrl;

    @Value("${daraja.host}")
    private String darajaHost;

    @Value("${daraja.key}")
    private String consumerKey;

    @Value("${daraja.secret}")
    private String consumerSecret;

    @Value("${daraja.result.url}")
    private String resultURL;

    @Value("${daraja.queue.timeout.url}")
    private String queueTimeOutURL;

    @Value("${initiator.name}")
    private String initiatorName;

    @Value("${security.credential}")
    private String securityCredentials;

    @Value("${business.short-code}")
    private int businessShortCode;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private static final SimpleDateFormat darajaDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public DarajaServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

    }


    @Override
    public DarajaAuthResponse authenticate() throws JsonProcessingException {

        log.debug("Authenticating daraja");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(consumerKey, consumerSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<JsonNode> authenticateResponse = restTemplate.exchange(darajaAuthUrl, HttpMethod.GET, httpEntity, JsonNode.class);
        if (!authenticateResponse.getStatusCode().equals(HttpStatus.OK)) {

            JsonNode errorBody = authenticateResponse.getBody();
            HttpStatus httpStatus = HttpStatus.valueOf(authenticateResponse.getStatusCode().value());
            if (errorBody == null) throw new HttpStatusException(httpStatus, httpStatus.getReasonPhrase());

            ErrorResponse errorResponse = objectMapper.readValue(errorBody.asText(), ErrorResponse.class);
            throw HttpStatusException.failed(errorResponse.getErrorMessage());
        }

        assert (authenticateResponse.getBody() != null);
        DarajaAuthResponse body = objectMapper.readValue(authenticateResponse.getBody().asText(), DarajaAuthResponse.class);
        if (body == null) throw HttpStatusException.failed("Daraja did not return an authentication response");

        return body;
    }

    @Override
    public B2CResponseModel initiateB2c(String accessToken, String originatorConversationID, @Valid GwPendingRequest gwRequest) throws JsonProcessingException {
        log.debug("initiating b2c request");


        B2CRequestForm request = B2CRequestForm.builder()
                .originatorConversationID(originatorConversationID)
                .initiatorName(initiatorName)
                .securityCredential(securityCredentials)
                .commandID("BusinessPayment")
                .amount(gwRequest.getAmount().intValue())
                .partyA(businessShortCode)
                .partyB(gwRequest.getMobileNumber())
                .remarks("GW request transaction")
                .occasion("Testing")
                .resultURL(resultURL)
                .queueTimeOutURL(queueTimeOutURL)
                .build();

        return doInitiateB2c(accessToken, request);
    }


    @Override
    public B2CResponseModel initiateB2c(String accessToken, String originatorConversationID, @Valid B2CRequestBodyForm form) throws JsonProcessingException {

        log.debug("initiating b2c request");


        B2CRequestForm request = B2CRequestForm.builder()
                .originatorConversationID(originatorConversationID)
                .initiatorName(form.getInitiatorName())
                .securityCredential(form.getSecurityCredential())
                .commandID(form.getCommandID())
                .amount(form.getAmount())
                .partyA(form.getPartyA())
                .partyB(form.getPartyB())
                .remarks(form.getRemarks())
                .occasion(form.getOccasion())
                .resultURL(resultURL)
                .queueTimeOutURL(queueTimeOutURL)
                .build();

        return doInitiateB2c(accessToken, request);

    }


    private B2CResponseModel doInitiateB2c(String accessToken, B2CRequestForm request) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<JsonNode> b2cResponse = restTemplate.exchange(darajaB2cUrl, HttpMethod.POST, httpEntity, JsonNode.class);
        if (!b2cResponse.getStatusCode().equals(HttpStatus.OK)) {

            JsonNode errorBody = b2cResponse.getBody();
            HttpStatus httpStatus = HttpStatus.valueOf(b2cResponse.getStatusCode().value());
            if (errorBody == null) throw new HttpStatusException(httpStatus, httpStatus.getReasonPhrase());

            ErrorResponse errorResponse = objectMapper.readValue(errorBody.asText(), ErrorResponse.class);
            throw HttpStatusException.failed(errorResponse.getErrorMessage());
        }


        assert (b2cResponse.getBody() != null);
        B2CResponseModel body = objectMapper.readValue(b2cResponse.getBody().asText(), B2CResponseModel.class);
        if (body == null) throw HttpStatusException.failed("Daraja did not return a response");

        body.setForm(request);

        return body;
    }

}
