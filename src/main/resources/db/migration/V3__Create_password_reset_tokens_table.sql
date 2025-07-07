-- Створи цей файл: src/main/resources/db/migration/V3__Create_password_reset_tokens_table.sql
-- Або додай до твого існуючого SQL файлу:

CREATE TABLE password_reset_tokens (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL,
                                       token VARCHAR(255) NOT NULL UNIQUE,
                                       expiry_date TIMESTAMP NOT NULL,
                                       is_used BOOLEAN NOT NULL DEFAULT FALSE,
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       INDEX idx_token (token),
                                       INDEX idx_email (email),
                                       INDEX idx_expiry_date (expiry_date),
                                       INDEX idx_email_active (email, is_used, expiry_date)
);