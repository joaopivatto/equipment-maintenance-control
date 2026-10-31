USE `equipment_maintenance_control`;

INSERT INTO address (id, zip_code, street, number, complement, neighborhood, city, state) VALUES
                                                                                              (1, '80010000', 'Rua XV de Novembro', 100, 'Apto 11', 'Centro', 'Curitiba', 'PR'),
                                                                                              (2, '80420010', 'Avenida Sete de Setembro', 2500, NULL, 'Batel', 'Curitiba', 'PR'),
                                                                                              (3, '80240000', 'Rua Marechal Deodoro', 350, NULL, 'Centro', 'Curitiba', 'PR'),
                                                                                              (4, '82010330', 'Rua Padre Anchieta', 980, 'Casa 2', 'Alto da XV', 'Curitiba', 'PR');

INSERT INTO phone (id, number) VALUES
                                   (1, '41999990001'),
                                   (2, '41999990002'),
                                   (3, '41999990003'),
                                   (4, '41999990004');

INSERT INTO profile (id, name, email, password_hash, password_salt, type, active) VALUES
                                                                                      (1, 'Maria', 'maria@empresa.com', 'J4n8FiO5p1Ct0iucyc0J8UTGx4VdwtB1Po4qLNrYy4Q=', 'LZ/VWkcNqNo2WWyY9HVbYg==', 'EMPLOYEE', TRUE),
                                                                                      (2, 'Mário', 'mario@empresa.com', 'm3eoaPg+C/JBoZAv3nCZr2ee4i1LwdllWmC/binvAr0=', 'ug5W42gMUgRM4AETQ8RqoA==', 'EMPLOYEE', TRUE),
                                                                                      (3, 'João', 'joao@email.com', 'id1q9AkCxQn0SngZYAzw/66D7a6fwtHQjUFq7ncr5QA=', 'juooGnj6fx29efXrZvuugw==', 'CUSTOMER', TRUE),
                                                                                      (4, 'José', 'jose@email.com', '2TvANBSWMH/1jS9Yd1tmleypc8uwLYamdA5U4TYOQFc=', '5jdYrQJrZl7iLKV4PYBaUw==', 'CUSTOMER', TRUE),
                                                                                      (5, 'Joana', 'joana@email.com', 'OXQ58SBKexIFfZwJdswWvIvXJ9zi3rp7nwFh79Ar7bQ=', 'g1DTn5uXcZMhCmV67e+TpQ==', 'CUSTOMER', TRUE),
                                                                                      (6, 'Joaquina', 'joaquina@email.com', 'mg1Ipx/oSSNYeQ6aqQC031MxeCWKrvVptgXXDiNzseE=', 'rljABHZrydFGuPGS27eICg==', 'CUSTOMER', TRUE);

INSERT INTO employee (id, birth_date) VALUES
                                          (1, '1985-03-12'),
                                          (2, '1990-07-25');

INSERT INTO customer (id, cpf, address_id, phone_id) VALUES
                                                         (3, '52998224725', 1, 1),
                                                         (4, '11144477735', 2, 2),
                                                         (5, '32819547664', 3, 3),
                                                         (6, '47109285332', 4, 4);

INSERT INTO equipment_type (id, description, active) VALUES
                                                         (1, 'Notebook', TRUE),
                                                         (2, 'Desktop', TRUE),
                                                         (3, 'Impressora', TRUE),
                                                         (4, 'Mouse', TRUE),
                                                         (5, 'Teclado', TRUE);

INSERT INTO equipment (id, description, type_id) VALUES
                                                     (1, 'Notebook Dell Inspiron 15', 1),
                                                     (2, 'Impressora HP LaserJet', 3),
                                                     (3, 'Desktop Compaq Presario', 2),
                                                     (4, 'Mouse Logitech M90', 4),
                                                     (5, 'Teclado Multilaser TC123', 5),
                                                     (6, 'Notebook Lenovo Ideapad', 1),
                                                     (7, 'Desktop Dell Optiplex', 2),
                                                     (8, 'Impressora Epson L3150', 3),
                                                     (9, 'Mouse Dell MS116', 4),
                                                     (10, 'Teclado sem fio Logitech K380', 5),
                                                     (11, 'Notebook Acer Aspire 5', 1),
                                                     (12, 'Desktop Positivo Master', 2),
                                                     (13, 'Impressora Brother DCP-T420W', 3),
                                                     (14, 'Mouse Multilaser MO226', 4),
                                                     (15, 'Teclado Mecânico Redragon', 5),
                                                     (16, 'Notebook Samsung Book X20', 1),
                                                     (17, 'Desktop Dell Inspiron 3470', 2),
                                                     (18, 'Impressora HP DeskJet 2774', 3),
                                                     (19, 'Mouse Microsoft Basic Optical', 4),
                                                     (20, 'Teclado Dell KB216', 5),
                                                     (21, 'Notebook HP Pavilion 14', 1),
                                                     (22, 'Desktop LG SmartPC', 2),
                                                     (23, 'Impressora Canon PIXMA G3111', 3),
                                                     (24, 'Mouse sem fio HP Z3700', 4),
                                                     (25, 'Teclado sem fio Dell KM3322', 5),
                                                     (26, 'Notebook Asus Vivobook 15', 1),
                                                     (27, 'Monitor LG 24 polegadas', 2);

INSERT INTO budget (id, value, employee_id, created_at) VALUES
                                                            (1, 80.00, 1, '2025-11-08 08:45:00'),
                                                            (2, 60.00, 2, '2025-11-22 11:20:00'),
                                                            (3, 250.00, 1, '2025-12-09 16:00:00'),
                                                            (4, 300.00, 2, '2025-12-19 09:15:00'),
                                                            (5, 90.00, 1, '2026-01-02 13:40:00'),
                                                            (6, 50.00, 2, '2026-01-14 10:00:00'),
                                                            (7, 70.00, 1, '2026-01-26 15:30:00'),
                                                            (8, 450.00, 2, '2026-02-09 09:50:00'),
                                                            (9, 120.00, 1, '2026-02-26 11:00:00'),
                                                            (10, 95.00, 2, '2026-03-10 14:20:00'),
                                                            (11, 40.00, 1, '2026-03-27 08:30:00'),
                                                            (12, 55.00, 2, '2026-04-10 10:10:00'),
                                                            (13, 180.00, 1, '2026-04-27 13:00:00'),
                                                            (14, 200.00, 2, '2026-05-11 09:40:00'),
                                                            (15, 150.00, 1, '2026-05-28 15:00:00'),
                                                            (16, 45.00, 2, '2026-06-14 11:30:00'),
                                                            (17, 65.00, 1, '2026-06-28 10:00:00'),
                                                            (18, 220.00, 2, '2026-07-15 14:50:00'),
                                                            (19, 190.00, 1, '2026-08-01 09:20:00'),
                                                            (20, 110.00, 2, '2026-08-18 16:10:00'),
                                                            (21, 35.00, 1, '2026-09-01 08:00:00'),
                                                            (22, 140.00, 2, '2026-10-06 09:30:00');

INSERT INTO maintenance (id, description, customer_instructions, employee_id, created_at) VALUES
                                                                                              (1, 'Troca de cabo USB do mouse', 'Cliente deve evitar puxar o fio com força', 1, '2026-04-15 08:30:00'),
                                                                                              (2, 'Substituição da tecla espaço', 'Aguardar 24h antes de uso intenso', 2, '2026-04-29 10:10:00'),
                                                                                              (3, 'Troca da bateria', 'Carregar por 2h antes de usar', 2, '2026-05-21 13:00:00'),
                                                                                              (4, 'Substituição do cabo SATA', 'Verificar BIOS após instalação', 2, '2026-05-31 09:40:00'),
                                                                                              (5, 'Troca do cartucho de toner', 'Descartar toner antigo corretamente', 1, '2026-06-16 15:00:00'),
                                                                                              (6, 'Limpeza interna do mouse', 'Evitar uso em superfícies com poeira', 2, '2026-07-03 11:30:00'),
                                                                                              (7, 'Troca do LED indicador', 'Nenhuma instrução especial', 1, '2026-07-18 10:00:00'),
                                                                                              (8, 'Troca do teclado do notebook', 'Testar todas as teclas antes da entrega', 2, '2026-08-03 14:50:00'),
                                                                                              (9, 'Troca da fonte de alimentação', 'Monitorar temperatura por 48h', 1, '2026-08-20 09:20:00'),
                                                                                              (10, 'Ajuste do rolete de alimentação de papel', 'Realizar teste de impressão de 10 páginas', 2, '2026-09-06 16:10:00'),
                                                                                              (11, 'Troca de pilhas e reset do receptor', 'Explicar ao cliente como trocar pilhas futuramente', 1, '2026-09-30 08:00:00');

INSERT INTO maintenance_request (id, created_at, updated_at, defect, payment_date, rejection_reason, finalized_at, status, equipment_id, customer_id, employee_id, finalized_by_id, budget_id, maintenance_id) VALUES
                                                                                                                                                                                                                   (1, '2025-09-21 09:00:00', '2025-09-21 09:00:00', 'Tela não liga', NULL, NULL, NULL, 'ABERTA', 1, 3, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                                   (2, '2025-10-03 10:30:00', '2025-10-03 10:30:00', 'Impressora não imprime', NULL, NULL, NULL, 'ABERTA', 2, 5, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                                   (3, '2025-10-20 14:00:00', '2025-10-20 14:00:00', 'Computador não liga', NULL, NULL, NULL, 'ABERTA', 3, 6, NULL, NULL, NULL, NULL),

                                                                                                                                                                                                                   (4, '2025-11-03 08:45:00', '2025-11-08 08:45:00', 'Mouse sem clique esquerdo', NULL, NULL, NULL, 'ORCADA', 4, 4, 1, NULL, 1, NULL),
                                                                                                                                                                                                                   (5, '2025-11-18 11:20:00', '2025-11-22 11:20:00', 'Teclado com teclas travando', NULL, NULL, NULL, 'ORCADA', 5, 3, 2, NULL, 2, NULL),
                                                                                                                                                                                                                   (6, '2025-12-04 16:00:00', '2025-12-09 16:00:00', 'Notebook superaquecendo', NULL, NULL, NULL, 'ORCADA', 6, 6, 1, NULL, 3, NULL),

                                                                                                                                                                                                                   (7, '2025-12-14 09:15:00', '2025-12-23 09:15:00', 'Desktop com tela azul', NULL, 'Orçamento acima do esperado', NULL, 'REJEITADA', 7, 5, 2, NULL, 4, NULL),
                                                                                                                                                                                                                   (8, '2025-12-28 13:40:00', '2026-01-07 13:40:00', 'Impressora com papel enroscado', NULL, 'Cliente decidiu não consertar agora', NULL, 'REJEITADA', 8, 4, 1, NULL, 5, NULL),

                                                                                                                                                                                                                   (9, '2026-01-09 10:00:00', '2026-01-19 10:00:00', 'Mouse não é reconhecido pelo PC', NULL, NULL, NULL, 'APROVADA', 9, 3, 2, NULL, 6, NULL),
                                                                                                                                                                                                                   (10, '2026-01-21 15:30:00', '2026-01-31 15:30:00', 'Teclado sem fio não pareia', NULL, NULL, NULL, 'APROVADA', 10, 6, 1, NULL, 7, NULL),
                                                                                                                                                                                                                   (11, '2026-02-05 09:50:00', '2026-02-19 09:50:00', 'Notebook com tela trincada', NULL, 'Cliente pediu segunda opinião', NULL, 'APROVADA', 11, 5, 2, NULL, 8, NULL),

                                                                                                                                                                                                                   (12, '2026-02-21 11:00:00', '2026-03-10 11:00:00', 'Desktop com ventoinha barulhenta', NULL, NULL, NULL, 'REDIRECIONADA', 12, 4, 2, NULL, 9, NULL),
                                                                                                                                                                                                                   (13, '2026-03-05 14:20:00', '2026-03-27 14:20:00', 'Impressora com manchas na impressão', NULL, NULL, NULL, 'REDIRECIONADA', 13, 6, 2, NULL, 10, NULL),

                                                                                                                                                                                                                   (14, '2026-03-22 08:30:00', '2026-04-15 08:30:00', 'Mouse com fio desencapado', NULL, NULL, NULL, 'ARRUMADA', 14, 3, 1, NULL, 11, 1),
                                                                                                                                                                                                                   (15, '2026-04-06 10:10:00', '2026-04-29 10:10:00', 'Teclado com tecla espaço quebrada', NULL, NULL, NULL, 'ARRUMADA', 15, 4, 2, NULL, 12, 2),
                                                                                                                                                                                                                   (16, '2026-04-22 13:00:00', '2026-05-21 13:00:00', 'Notebook não carrega bateria', NULL, NULL, NULL, 'ARRUMADA', 16, 5, 2, NULL, 13, 3),

                                                                                                                                                                                                                   (17, '2026-05-07 09:40:00', '2026-06-12 09:40:00', 'Desktop não reconhece HD', '2026-06-12 09:40:00', NULL, NULL, 'PAGA', 17, 6, 2, NULL, 14, 4),
                                                                                                                                                                                                                   (18, '2026-05-23 15:00:00', '2026-06-28 15:00:00', 'Impressora com toner vazando', '2026-06-28 15:00:00', NULL, NULL, 'PAGA', 18, 3, 1, NULL, 15, 5),
                                                                                                                                                                                                                   (19, '2026-06-09 11:30:00', '2026-07-15 11:30:00', 'Mouse travando ao clicar', '2026-07-15 11:30:00', NULL, NULL, 'PAGA', 19, 4, 2, NULL, 16, 6),

                                                                                                                                                                                                                   (20, '2026-06-24 10:00:00', '2026-08-06 10:00:00', 'Teclado com LED queimado', '2026-07-30 10:00:00', NULL, '2026-08-06 10:00:00', 'FINALIZADA', 20, 5, 1, 1, 17, 7),
                                                                                                                                                                                                                   (21, '2026-07-10 14:50:00', '2026-08-22 14:50:00', 'Notebook com teclado parcialmente funcional', '2026-08-15 14:50:00', NULL, '2026-08-22 14:50:00', 'FINALIZADA', 21, 6, 2, 2, 18, 8),
                                                                                                                                                                                                                   (22, '2026-07-27 09:20:00', '2026-09-08 09:20:00', 'Desktop desligando sozinho', '2026-09-01 09:20:00', NULL, '2026-09-08 09:20:00', 'FINALIZADA', 22, 3, 1, 1, 19, 9),
                                                                                                                                                                                                                   (23, '2026-08-13 16:10:00', '2026-09-25 16:10:00', 'Impressora com alimentação de papel falhando', '2026-09-18 16:10:00', NULL, '2026-09-25 16:10:00', 'FINALIZADA', 23, 4, 2, 2, 20, 10),
                                                                                                                                                                                                                   (24, '2026-08-27 08:00:00', '2026-10-19 08:00:00', 'Mouse sem fio não liga', '2026-10-12 08:00:00', 'Cliente achou caro para o problema', '2026-10-19 08:00:00', 'FINALIZADA', 24, 5, 1, 1, 21, 11),

                                                                                                                                                                                                                   (25, '2026-10-14 10:00:00', '2026-10-14 10:00:00', 'Teclado sem fio não conecta via Bluetooth', NULL, NULL, NULL, 'ABERTA', 25, 3, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                                   (26, '2026-10-04 09:30:00', '2026-10-06 09:30:00', 'Notebook com tela piscando', NULL, NULL, NULL, 'ORCADA', 26, 4, 2, NULL, 22, NULL),
                                                                                                                                                                                                                   (27, NOW(), NOW(), 'Monitor sem imagem após queda de energia', NULL, NULL, NULL, 'ABERTA', 27, 5, NULL, NULL, NULL, NULL);

INSERT INTO redirect (id, source_employee_id, destination_employee_id, maintenance_request_id, created_at) VALUES
                                                                                                               (1, 1, 2, 12, '2026-03-10 11:00:00'),
                                                                                                               (2, 2, 1, 13, '2026-03-22 14:20:00'),
                                                                                                               (3, 1, 2, 13, '2026-03-27 14:20:00'),
                                                                                                               (4, 1, 2, 16, '2026-05-09 13:00:00'),
                                                                                                               (5, 1, 2, 24, '2026-09-18 08:00:00'),
                                                                                                               (6, 2, 1, 24, '2026-09-23 08:00:00');

INSERT INTO maintenance_request_history (maintenance_request_id, updated_at, status, employee_id, reason) VALUES
                                                                                                              (1, '2025-09-21 09:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (2, '2025-10-03 10:30:00', 'ABERTA', NULL, NULL),
                                                                                                              (3, '2025-10-20 14:00:00', 'ABERTA', NULL, NULL),

                                                                                                              (4, '2025-11-03 08:45:00', 'ABERTA', NULL, NULL),
                                                                                                              (4, '2025-11-08 08:45:00', 'ORCADA', 1, NULL),

                                                                                                              (5, '2025-11-18 11:20:00', 'ABERTA', NULL, NULL),
                                                                                                              (5, '2025-11-22 11:20:00', 'ORCADA', 2, NULL),

                                                                                                              (6, '2025-12-04 16:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (6, '2025-12-09 16:00:00', 'ORCADA', 1, NULL),

                                                                                                              (7, '2025-12-14 09:15:00', 'ABERTA', NULL, NULL),
                                                                                                              (7, '2025-12-19 09:15:00', 'ORCADA', 2, NULL),
                                                                                                              (7, '2025-12-23 09:15:00', 'REJEITADA', NULL, 'Orçamento acima do esperado'),

                                                                                                              (8, '2025-12-28 13:40:00', 'ABERTA', NULL, NULL),
                                                                                                              (8, '2026-01-02 13:40:00', 'ORCADA', 1, NULL),
                                                                                                              (8, '2026-01-07 13:40:00', 'REJEITADA', NULL, 'Cliente decidiu não consertar agora'),

                                                                                                              (9, '2026-01-09 10:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (9, '2026-01-14 10:00:00', 'ORCADA', 2, NULL),
                                                                                                              (9, '2026-01-19 10:00:00', 'APROVADA', NULL, NULL),

                                                                                                              (10, '2026-01-21 15:30:00', 'ABERTA', NULL, NULL),
                                                                                                              (10, '2026-01-26 15:30:00', 'ORCADA', 1, NULL),
                                                                                                              (10, '2026-01-31 15:30:00', 'APROVADA', NULL, NULL),

                                                                                                              (11, '2026-02-05 09:50:00', 'ABERTA', NULL, NULL),
                                                                                                              (11, '2026-02-09 09:50:00', 'ORCADA', 2, NULL),
                                                                                                              (11, '2026-02-14 09:50:00', 'REJEITADA', NULL, 'Cliente pediu segunda opinião'),
                                                                                                              (11, '2026-02-19 09:50:00', 'APROVADA', NULL, NULL),

                                                                                                              (12, '2026-02-21 11:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (12, '2026-02-26 11:00:00', 'ORCADA', 1, NULL),
                                                                                                              (12, '2026-03-03 11:00:00', 'APROVADA', NULL, NULL),
                                                                                                              (12, '2026-03-10 11:00:00', 'REDIRECIONADA', 2, NULL),

                                                                                                              (13, '2026-03-05 14:20:00', 'ABERTA', NULL, NULL),
                                                                                                              (13, '2026-03-10 14:20:00', 'ORCADA', 2, NULL),
                                                                                                              (13, '2026-03-15 14:20:00', 'APROVADA', NULL, NULL),
                                                                                                              (13, '2026-03-22 14:20:00', 'REDIRECIONADA', 1, NULL),
                                                                                                              (13, '2026-03-27 14:20:00', 'REDIRECIONADA', 2, NULL),

                                                                                                              (14, '2026-03-22 08:30:00', 'ABERTA', NULL, NULL),
                                                                                                              (14, '2026-03-27 08:30:00', 'ORCADA', 1, NULL),
                                                                                                              (14, '2026-04-01 08:30:00', 'APROVADA', NULL, NULL),
                                                                                                              (14, '2026-04-15 08:30:00', 'ARRUMADA', 1, NULL),

                                                                                                              (15, '2026-04-06 10:10:00', 'ABERTA', NULL, NULL),
                                                                                                              (15, '2026-04-10 10:10:00', 'ORCADA', 2, NULL),
                                                                                                              (15, '2026-04-15 10:10:00', 'APROVADA', NULL, NULL),
                                                                                                              (15, '2026-04-29 10:10:00', 'ARRUMADA', 2, NULL),

                                                                                                              (16, '2026-04-22 13:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (16, '2026-04-27 13:00:00', 'ORCADA', 1, NULL),
                                                                                                              (16, '2026-05-02 13:00:00', 'APROVADA', NULL, NULL),
                                                                                                              (16, '2026-05-09 13:00:00', 'REDIRECIONADA', 2, NULL),
                                                                                                              (16, '2026-05-21 13:00:00', 'ARRUMADA', 2, NULL),

                                                                                                              (17, '2026-05-07 09:40:00', 'ABERTA', NULL, NULL),
                                                                                                              (17, '2026-05-11 09:40:00', 'ORCADA', 2, NULL),
                                                                                                              (17, '2026-05-16 09:40:00', 'APROVADA', NULL, NULL),
                                                                                                              (17, '2026-05-31 09:40:00', 'ARRUMADA', 2, NULL),
                                                                                                              (17, '2026-06-12 09:40:00', 'PAGA', NULL, NULL),

                                                                                                              (18, '2026-05-23 15:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (18, '2026-05-28 15:00:00', 'ORCADA', 1, NULL),
                                                                                                              (18, '2026-06-02 15:00:00', 'APROVADA', NULL, NULL),
                                                                                                              (18, '2026-06-16 15:00:00', 'ARRUMADA', 1, NULL),
                                                                                                              (18, '2026-06-28 15:00:00', 'PAGA', NULL, NULL),

                                                                                                              (19, '2026-06-09 11:30:00', 'ABERTA', NULL, NULL),
                                                                                                              (19, '2026-06-14 11:30:00', 'ORCADA', 2, NULL),
                                                                                                              (19, '2026-06-19 11:30:00', 'APROVADA', NULL, NULL),
                                                                                                              (19, '2026-07-03 11:30:00', 'ARRUMADA', 2, NULL),
                                                                                                              (19, '2026-07-15 11:30:00', 'PAGA', NULL, NULL),

                                                                                                              (20, '2026-06-24 10:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (20, '2026-06-28 10:00:00', 'ORCADA', 1, NULL),
                                                                                                              (20, '2026-07-03 10:00:00', 'APROVADA', NULL, NULL),
                                                                                                              (20, '2026-07-18 10:00:00', 'ARRUMADA', 1, NULL),
                                                                                                              (20, '2026-07-30 10:00:00', 'PAGA', NULL, NULL),
                                                                                                              (20, '2026-08-06 10:00:00', 'FINALIZADA', 1, NULL),

                                                                                                              (21, '2026-07-10 14:50:00', 'ABERTA', NULL, NULL),
                                                                                                              (21, '2026-07-15 14:50:00', 'ORCADA', 2, NULL),
                                                                                                              (21, '2026-07-20 14:50:00', 'APROVADA', NULL, NULL),
                                                                                                              (21, '2026-08-03 14:50:00', 'ARRUMADA', 2, NULL),
                                                                                                              (21, '2026-08-15 14:50:00', 'PAGA', NULL, NULL),
                                                                                                              (21, '2026-08-22 14:50:00', 'FINALIZADA', 2, NULL),

                                                                                                              (22, '2026-07-27 09:20:00', 'ABERTA', NULL, NULL),
                                                                                                              (22, '2026-08-01 09:20:00', 'ORCADA', 1, NULL),
                                                                                                              (22, '2026-08-06 09:20:00', 'APROVADA', NULL, NULL),
                                                                                                              (22, '2026-08-20 09:20:00', 'ARRUMADA', 1, NULL),
                                                                                                              (22, '2026-09-01 09:20:00', 'PAGA', NULL, NULL),
                                                                                                              (22, '2026-09-08 09:20:00', 'FINALIZADA', 1, NULL),

                                                                                                              (23, '2026-08-13 16:10:00', 'ABERTA', NULL, NULL),
                                                                                                              (23, '2026-08-18 16:10:00', 'ORCADA', 2, NULL),
                                                                                                              (23, '2026-08-22 16:10:00', 'APROVADA', NULL, NULL),
                                                                                                              (23, '2026-09-06 16:10:00', 'ARRUMADA', 2, NULL),
                                                                                                              (23, '2026-09-18 16:10:00', 'PAGA', NULL, NULL),
                                                                                                              (23, '2026-09-25 16:10:00', 'FINALIZADA', 2, NULL),

                                                                                                              (24, '2026-08-27 08:00:00', 'ABERTA', NULL, NULL),
                                                                                                              (24, '2026-09-01 08:00:00', 'ORCADA', 1, NULL),
                                                                                                              (24, '2026-09-06 08:00:00', 'REJEITADA', NULL, 'Cliente achou caro para o problema'),
                                                                                                              (24, '2026-09-11 08:00:00', 'APROVADA', NULL, NULL),
                                                                                                              (24, '2026-09-18 08:00:00', 'REDIRECIONADA', 2, NULL),
                                                                                                              (24, '2026-09-23 08:00:00', 'REDIRECIONADA', 1, NULL),
                                                                                                              (24, '2026-09-30 08:00:00', 'ARRUMADA', 1, NULL),
                                                                                                              (24, '2026-10-12 08:00:00', 'PAGA', NULL, NULL),
                                                                                                              (24, '2026-10-19 08:00:00', 'FINALIZADA', 1, NULL),

                                                                                                              (25, '2026-10-14 10:00:00', 'ABERTA', NULL, NULL),

                                                                                                              (26, '2026-10-04 09:30:00', 'ABERTA', NULL, NULL),
                                                                                                              (26, '2026-10-06 09:30:00', 'ORCADA', 2, NULL),

                                                                                                              (27, NOW(), 'ABERTA', NULL, NULL);