-- liquibase formatted sql

-- changeset gabor:1647469378926-1
ALTER TABLE user
    ADD email             VARCHAR(255) NULL,
    ADD first_name        VARCHAR(255) NULL,
    ADD last_name         VARCHAR(255) NULL,
    ADD password          VARCHAR(255) NULL,
    ADD registration_date datetime     NULL,
    ADD username          VARCHAR(255) NULL;

-- changeset gabor:1647469378926-7
ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

-- changeset gabor:1647469378926-8
ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

