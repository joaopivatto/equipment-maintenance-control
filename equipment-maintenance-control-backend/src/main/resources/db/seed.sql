-- =====================================================================
-- seed.sql - Dados basicos para desenvolvimento e teste
--
-- Rode DEPOIS do schema.sql (as tabelas precisam existir e estar vazias):
--   mysql -u root -p < src/main/resources/db/seed.sql
--
-- Usuarios de teste (a senha de TODOS e 1234):
--   Funcionarios: maria@empresa.com   | mario@empresa.com
--   Clientes:     joao@email.com      | jose@email.com
--
-- A versao completa (20+ solicitacoes) fica para o B-32.
-- =====================================================================

USE `equipment_maintenance_control`;

INSERT INTO address (id, zip_code, street, number, complement, neighborhood, city, state) VALUES
                                                                                              (1, '80010000', 'Rua XV de Novembro', 100, 'Apto 11', 'Centro', 'Curitiba', 'PR'),
                                                                                              (2, '80420010', 'Avenida Sete de Setembro', 2500, NULL, 'Batel', 'Curitiba', 'PR');

INSERT INTO phone (id, number) VALUES
                                   (1, '41999990001'),
                                   (2, '41999990002');

-- Perfis (ids 1 e 2 = funcionarios, ids 3 e 4 = clientes)
INSERT INTO profile (id, name, email, password_hash, password_salt, type, active) VALUES
                                                                                      (1, 'Maria', 'maria@empresa.com', 'J4n8FiO5p1Ct0iucyc0J8UTGx4VdwtB1Po4qLNrYy4Q=', 'LZ/VWkcNqNo2WWyY9HVbYg==', 'EMPLOYEE', TRUE),
                                                                                      (2, 'Mário', 'mario@empresa.com', 'm3eoaPg+C/JBoZAv3nCZr2ee4i1LwdllWmC/binvAr0=', 'ug5W42gMUgRM4AETQ8RqoA==', 'EMPLOYEE', TRUE),
                                                                                      (3, 'João', 'joao@email.com', 'id1q9AkCxQn0SngZYAzw/66D7a6fwtHQjUFq7ncr5QA=', 'juooGnj6fx29efXrZvuugw==', 'CUSTOMER', TRUE),
                                                                                      (4, 'José', 'jose@email.com', '2TvANBSWMH/1jS9Yd1tmleypc8uwLYamdA5U4TYOQFc=', '5jdYrQJrZl7iLKV4PYBaUw==', 'CUSTOMER', TRUE);

INSERT INTO employee (id, birth_date) VALUES
                                          (1, '1985-03-12'),
                                          (2, '1990-07-25');

INSERT INTO customer (id, cpf, address_id, phone_id) VALUES
                                                         (3, '52998224725', 1, 1),
                                                         (4, '11144477735', 2, 2);

INSERT INTO equipment_type (id, description, active) VALUES
                                                         (1, 'Notebook', TRUE),
                                                         (2, 'Desktop', TRUE),
                                                         (3, 'Impressora', TRUE),
                                                         (4, 'Mouse', TRUE),
                                                         (5, 'Teclado', TRUE);

INSERT INTO equipment (id, description, type_id) VALUES
                                                     (1, 'Notebook Dell Inspiron 15', 1),
                                                     (2, 'Impressora HP LaserJet', 3);