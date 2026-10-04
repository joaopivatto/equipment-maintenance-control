-- =====================================================================
-- schema.sql
--
-- Cria o banco do zero (instalacao nova), ja com todas as colunas da Fase 0.
-- Atencao: apaga o banco existente com o mesmo nome.
--
-- Execucao (a partir da pasta do backend):
--   mysql -u root -p < src/main/resources/db/schema.sql
-- =====================================================================

DROP DATABASE IF EXISTS `equipment-maintenance-control`;
CREATE DATABASE `equipment-maintenance-control` CHARACTER SET utf8mb4;
USE `equipment-maintenance-control`;

CREATE TABLE address (
                         id           INT          NOT NULL AUTO_INCREMENT,
                         zip_code     VARCHAR(255) NULL,
                         street       VARCHAR(255) NULL,
                         number       INT          NULL,
                         complement   VARCHAR(255) NULL,
                         neighborhood VARCHAR(255) NULL,
                         city         VARCHAR(255) NULL,
                         state        VARCHAR(255) NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE phone (
                       id     INT          NOT NULL AUTO_INCREMENT,
                       number VARCHAR(255) NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE profile (
                         id            INT          NOT NULL AUTO_INCREMENT,
                         name          VARCHAR(255) NULL,
                         email         VARCHAR(255) NOT NULL,
                         password_hash VARCHAR(255) NULL,
                         password_salt VARCHAR(255) NULL,
                         type          VARCHAR(20)  NULL,
                         active        BOOLEAN      NOT NULL DEFAULT TRUE,
                         PRIMARY KEY (id),
                         CONSTRAINT uq_profile_email UNIQUE (email)
);

CREATE TABLE customer (
                          id         INT         NOT NULL,
                          cpf        VARCHAR(11) NOT NULL,
                          address_id INT         NULL,
                          phone_id   INT         NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT uq_customer_cpf UNIQUE (cpf),
                          CONSTRAINT fk_customer_profile FOREIGN KEY (id)         REFERENCES profile (id),
                          CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES address (id),
                          CONSTRAINT fk_customer_phone   FOREIGN KEY (phone_id)   REFERENCES phone (id)
);

CREATE TABLE employee (
                          id         INT  NOT NULL,
                          birth_date DATE NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_employee_profile FOREIGN KEY (id) REFERENCES profile (id)
);

CREATE TABLE equipment_type (
                                id          INT          NOT NULL AUTO_INCREMENT,
                                description VARCHAR(255) NULL,
                                active      BOOLEAN      NOT NULL DEFAULT TRUE,
                                PRIMARY KEY (id)
);

CREATE TABLE equipment (
                           id          INT          NOT NULL AUTO_INCREMENT,
                           description VARCHAR(255) NULL,
                           type_id     INT          NULL,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_equipment_type FOREIGN KEY (type_id) REFERENCES equipment_type (id)
);

CREATE TABLE budget (
                        id          INT            NOT NULL AUTO_INCREMENT,
                        value       DECIMAL(38, 2) NULL,
                        employee_id INT            NOT NULL,
                        created_at  DATETIME       NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_budget_employee FOREIGN KEY (employee_id) REFERENCES employee (id)
);

CREATE TABLE maintenance (
                             id                    INT          NOT NULL AUTO_INCREMENT,
                             description           VARCHAR(255) NULL,
                             customer_instructions VARCHAR(255) NULL,
                             employee_id           INT          NULL,
                             created_at            DATETIME     NULL,
                             PRIMARY KEY (id),
                             CONSTRAINT fk_maintenance_employee FOREIGN KEY (employee_id) REFERENCES employee (id)
);

CREATE TABLE maintenance_request (
                                     id               INT          NOT NULL AUTO_INCREMENT,
                                     created_at       DATETIME     NULL,
                                     updated_at       DATETIME     NULL,
                                     defect           VARCHAR(255) NULL,
                                     payment_date     DATETIME     NULL,
                                     rejection_reason VARCHAR(255) NULL,
                                     finalized_at     DATETIME     NULL,
                                     status           VARCHAR(20)  NOT NULL,
                                     equipment_id     INT          NOT NULL,
                                     customer_id      INT          NOT NULL,
                                     employee_id      INT          NULL,
                                     finalized_by_id  INT          NULL,
                                     budget_id        INT          NULL,
                                     maintenance_id   INT          NULL,
                                     PRIMARY KEY (id),
                                     CONSTRAINT fk_mr_equipment    FOREIGN KEY (equipment_id)    REFERENCES equipment (id),
                                     CONSTRAINT fk_mr_customer     FOREIGN KEY (customer_id)     REFERENCES customer (id),
                                     CONSTRAINT fk_mr_employee     FOREIGN KEY (employee_id)     REFERENCES employee (id),
                                     CONSTRAINT fk_mr_finalized_by FOREIGN KEY (finalized_by_id) REFERENCES employee (id),
                                     CONSTRAINT fk_mr_budget       FOREIGN KEY (budget_id)       REFERENCES budget (id),
                                     CONSTRAINT fk_mr_maintenance  FOREIGN KEY (maintenance_id)  REFERENCES maintenance (id),
                                     INDEX idx_mr_status (status),
                                     INDEX idx_mr_customer_created (customer_id, created_at)
);

CREATE TABLE maintenance_request_history (
                                             id                     INT          NOT NULL AUTO_INCREMENT,
                                             maintenance_request_id INT          NOT NULL,
                                             updated_at             DATETIME     NULL,
                                             status                 VARCHAR(20)  NULL,
                                             employee_id            INT          NULL,
                                             reason                 VARCHAR(255) NULL,
                                             PRIMARY KEY (id),
                                             CONSTRAINT fk_history_request  FOREIGN KEY (maintenance_request_id) REFERENCES maintenance_request (id),
                                             CONSTRAINT fk_history_employee FOREIGN KEY (employee_id)            REFERENCES employee (id)
);

CREATE TABLE redirect (
                          id                      INT      NOT NULL AUTO_INCREMENT,
                          source_employee_id      INT      NOT NULL,
                          destination_employee_id INT      NOT NULL,
                          maintenance_request_id  INT      NOT NULL,
                          created_at              DATETIME NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_redirect_source      FOREIGN KEY (source_employee_id)      REFERENCES employee (id),
                          CONSTRAINT fk_redirect_destination FOREIGN KEY (destination_employee_id) REFERENCES employee (id),
                          CONSTRAINT fk_redirect_request     FOREIGN KEY (maintenance_request_id)  REFERENCES maintenance_request (id)
);