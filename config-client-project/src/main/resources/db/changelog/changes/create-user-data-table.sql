--liquibase formatted sql

--changeset k1den:1
CREATE TABLE user_data (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           message VARCHAR(255),
                           created_at TIMESTAMP
);

CREATE SEQUENCE message_sequence START 1;