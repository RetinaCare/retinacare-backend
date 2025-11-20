CREATE TABLE refresh_tokens
(
    refresh_token_id BIGINT                      NOT NULL,
    token            VARCHAR(255)                NOT NULL,
    user_id          UUID                        NOT NULL,
    expiry_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (refresh_token_id)
);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_token UNIQUE (token);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);