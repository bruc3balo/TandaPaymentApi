CREATE TABLE b2c_requests
(
    id                        VARCHAR(255) NOT NULL,
    initiator_name            VARCHAR(255) NULL,
    originator_conversation_id VARCHAR(255) NULL,
    security_credential       VARCHAR(255) NULL,
    command_id                VARCHAR(255) NULL,
    amount                    DECIMAL NULL,
    partya                    VARCHAR(255) NULL,
    partyb                    VARCHAR(255) NULL,
    remarks                   VARCHAR(255) NULL,
    time_out_url              VARCHAR(255) NULL,
    result_url                VARCHAR(255) NULL,
    occasion                  VARCHAR(255) NULL,
    response_code             VARCHAR(255) NULL,
    response_description      VARCHAR(255) NULL,
    b2c_request_id            VARCHAR(255) NULL,
    updated_at                datetime NULL,
    created_at                datetime NULL,
    CONSTRAINT pk_b2c_requests PRIMARY KEY (id)
);

CREATE TABLE b2c_response
(
    id                                       VARCHAR(255) NOT NULL,
    result_type                              INT NULL,
    result_code                              INT NULL,
    result_description                       VARCHAR(255) NULL,
    originator_conversation_id               VARCHAR(255) NULL,
    conversation_id                          VARCHAR(255) NULL,
    transaction_id                           VARCHAR(255) NULL,
    queue_timeout_url                        VARCHAR(255) NULL,
    transaction_amount                       VARCHAR(255) NULL,
    transaction_receipt                      VARCHAR(255) NULL,
    b2c_recipient_registered_customer        BIT(1) NULL,
    b2c_charges_paid_account_available_funds DECIMAL NULL,
    receiver_party_public_name               VARCHAR(255) NULL,
    transaction_completed_date_time          datetime NULL,
    b2cutility_account_available_funds       DECIMAL NULL,
    b2cworking_account_available_funds       DECIMAL NULL,
    updated_at                               datetime NULL,
    created_atw                              datetime NULL,
    CONSTRAINT pk_b2c_response PRIMARY KEY (id)
);

CREATE TABLE b2c_transactions
(
    id              VARCHAR(255) NOT NULL,
    b2c_request_id  VARCHAR(255) NULL,
    b2c_response_id VARCHAR(255) NULL,
    updated_at      datetime NULL,
    created_at      datetime NULL,
    CONSTRAINT pk_b2c_transactions PRIMARY KEY (id)
);

ALTER TABLE b2c_requests
    ADD CONSTRAINT uc_b2c_requests_b2c_request UNIQUE (b2c_request_id);

ALTER TABLE b2c_transactions
    ADD CONSTRAINT uc_b2c_transactions_b2c_request UNIQUE (b2c_request_id);

ALTER TABLE b2c_transactions
    ADD CONSTRAINT uc_b2c_transactions_b2c_response UNIQUE (b2c_response_id);

ALTER TABLE b2c_requests
    ADD CONSTRAINT FK_B2C_REQUESTS_ON_B2C_REQUEST FOREIGN KEY (b2c_request_id) REFERENCES b2c_transactions (id);

ALTER TABLE b2c_transactions
    ADD CONSTRAINT FK_B2C_TRANSACTIONS_ON_B2C_REQUEST FOREIGN KEY (b2c_request_id) REFERENCES b2c_requests (id);

ALTER TABLE b2c_transactions
    ADD CONSTRAINT FK_B2C_TRANSACTIONS_ON_B2C_RESPONSE FOREIGN KEY (b2c_response_id) REFERENCES b2c_response (id);