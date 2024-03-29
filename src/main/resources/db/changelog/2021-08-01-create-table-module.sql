-- liquibase formatted sql
-- changeset Sarvesh:create-table-module

CREATE TABLE IF NOT EXISTS module (
  id BINARY(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID(), TRUE)) PRIMARY KEY,
  name VARCHAR(512) NOT NULL,
  description VARCHAR(1000),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status ENUM("CREATED","UPDATED","DEACTIVATED","REACTIVATED") NOT NULL,
  CONSTRAINT uk_module_name UNIQUE (name)
);