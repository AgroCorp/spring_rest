-- liquibase formatted sql

-- changeset gabor:1647467973578-1
CREATE SEQUENCE hibernate_sequence INCREMENT BY 1 START WITH 1;

-- changeset gabor:1647467973578-2
CREATE TABLE user
(
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

