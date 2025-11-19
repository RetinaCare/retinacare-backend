CREATE TABLE "users"
(
    user_id       UUID NOT NULL,
    fullname      VARCHAR(255),
    email         VARCHAR(255),
    password_hash VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

ALTER TABLE "users"
    ADD CONSTRAINT uc_user_email UNIQUE (email);