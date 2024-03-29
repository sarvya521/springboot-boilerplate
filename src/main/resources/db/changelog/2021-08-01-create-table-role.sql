-- liquibase formatted sql
-- changeset Sarvesh:create-table-role splitStatements:true

CREATE TABLE IF NOT EXISTS role (
  id BINARY(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID(), TRUE)) PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  version INT NOT NULL,
  CONSTRAINT uk_role_name UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS role_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_id BINARY(16) NOT NULL,
  name VARCHAR(50) NOT NULL,
  performed_by VARCHAR(255) NOT NULL,
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  version INT NOT NULL
);
