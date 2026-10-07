# Roadmap do Backend — Controle de Manutenção de Equipamentos

Escopo deste documento: **apenas o backend** (Spring Boot 4.1 / Java 21 / MySQL / JPA).
Nada de Angular, telas, máscaras ou cores aqui — esses itens ficam com o front-end.

Cada item é uma **task pequena**, pensada para ser pega por um integrante da equipe e concluída
isoladamente. Marque o `[ ]` quando terminar e coloque seu nome em "Responsável".

Legenda de status: `[ ]` a fazer · `[~]` em andamento · `[x]` concluído

---

## Situação atual (o que já existe)

| RF | Descrição | Status atual |
|----|-----------|--------------|
| RF001 | Autocadastro de cliente | ausente |
| RF002 | Login | pronto (`POST /auth/login`, SHA-256 + salt) |
| RF003 | Listar solicitações do cliente | parcial (lista todas, sem filtro/ordem) |
| RF004 | Criar solicitação | parcial (exige `equipmentId`, não descrição + categoria) |
| RF005 | Mostrar orçamento | parcial (`GET /maintenance-request/{id}`) |
| RF006–RF010 | Aprovar / Rejeitar / Visualizar / Resgatar / Pagar | ausentes |
| RF011 | Solicitações ABERTAS | pronto (`GET /maintenance-request/open`) |
| RF012 | Efetuar orçamento | ausente (entidade `Budget` existe) |
| RF013–RF016 | Filtros / Manutenção / Redirecionar / Finalizar | ausentes |
| RF017 | CRUD de categoria | parcial (falta remoção) |
| RF018 | CRUD de funcionários | ausente |
| RF019–RF020 | Relatórios em PDF | ausentes |

---

## Fase 0 — Fundações

> Bloqueiam as demais fases. Idealmente feitas por 1 ou 2 pessoas antes de dividir o resto.

- [X] **B-01 — Configurar CORS**
  Responsável: Samuel.
  Criar `config/WebConfig` implementando `WebMvcConfigurer`, liberando `http://localhost:4200`
  para `GET, POST, PUT, DELETE, PATCH, OPTIONS`.
  *Pronto quando:* o front-end consegue chamar `POST /auth/login` sem erro de CORS.

- [X] **B-02 — Ajustar entidades que faltam campos**
  Responsável: Samuel · Depende de: —
  - `MaintenanceRequest`: adicionar `rejectionReason` (String), `finalizedAt` (LocalDateTime),
    `finalizedBy` (Employee) e mudar `paymentDate` para `LocalDateTime`.
  - `Maintenance`: `createdAt` passa de `LocalDate` para `LocalDateTime`.
  - `Redirect`: adicionar `@ManyToOne MaintenanceRequest maintenanceRequest`.
  - `Profile.type`: anotar com `@Enumerated(EnumType.STRING)` (hoje grava número).
  *Pronto quando:* o projeto compila e a aplicação sobe.

- [X] **B-03 — Migration SQL das novas colunas**
  Responsável: Samuel · Depende de: B-02
  Criar `src/main/resources/db/migration_003_*.sql` com as colunas de B-02 +
  `active BOOLEAN NOT NULL DEFAULT TRUE` em `profile` e `equipment_type`.
  Atualizar também `schema.sql` para que uma instalação nova já venha correta.

- [X] **B-04 — Campo `active` nas entidades (remoção lógica)**
  Responsável: Samuel · Depende de: B-03
  Adicionar `active` em `Profile` e `EquipmentType`, com default `true`.
  *Pronto quando:* listagens passam a considerar apenas registros ativos.

- [ ] **B-05 — Máquina de estados da solicitação**
  Responsável: ____ · Depende de: B-02
  Criar um ponto único de transição, por exemplo
  `MaintenanceRequest.transitionTo(status, employee, dataHora)`, que valida a transição
  e **sempre** grava uma linha em `maintenance_request_history`.
  Transições válidas: ABERTA→ORCADA · ORCADA→APROVADA/REJEITADA · REJEITADA→APROVADA ·
  APROVADA/REDIRECIONADA→ARRUMADA/REDIRECIONADA · ARRUMADA→PAGA · PAGA→FINALIZADA.
  Transição inválida deve lançar `AppException`.

- [ ] **B-06 — Novos códigos de erro**
  Responsável: ____ · Depende de: —
  Acrescentar em `ErrorCode`: `EMPLOYEE_NOT_FOUND`, `INVALID_STATUS_TRANSITION`,
  `EMAIL_ALREADY_EXISTS`, `CPF_ALREADY_EXISTS`, `SELF_REDIRECT_NOT_ALLOWED`,
  `LAST_EMPLOYEE_CANNOT_BE_REMOVED`, `SELF_DELETE_NOT_ALLOWED`, `ZIP_CODE_NOT_FOUND`.

- [ ] **B-07 — Definir como o backend identifica quem age**
  Responsável: ____ · Depende de: —
  Combinar em equipe: `employeeId`/`customerId` no corpo da requisição **ou** header `X-User-Id`.
  Documentar a decisão neste arquivo e usar o mesmo padrão em todos os endpoints novos.
  *Pronto quando:* a convenção está escrita e acordada.

---

## Fase 1 — Requisitos mínimos para a defesa

> RF001, RF002, RF003, RF004, RF005, RF006, RF011, RF012, RF017, RF018.
> Prioridade máxima: sem eles a equipe não vai para a defesa.

- [ ] **B-08 — Gerador de senha aleatória de 4 dígitos**
  Responsável: Matheus · Depende de: —
  Classe em `domain/security` que gera 4 números aleatórios (`SecureRandom`) para o autocadastro.

- [ ] **B-09 — Serviço de envio de e-mail**
  Responsável: Matheus· Depende de: —
  Adicionar `spring-boot-starter-mail`, criar interface `EmailSender` e duas implementações:
  uma real (SMTP) e uma de desenvolvimento que só escreve no log.

- [ ] **B-10 — Cliente ViaCEP**
  Responsável: Matheus · Depende de: B-06
  `ViaCepClient` usando `RestClient` para `https://viacep.com.br/ws/{cep}/json/`,
  devolvendo logradouro, bairro, cidade e UF. CEP inexistente → `ZIP_CODE_NOT_FOUND`.

- [ ] **B-11 — RF001: endpoint de autocadastro**
  Responsável: Matheus · Depende de: B-08, B-09, B-10
  `POST /customers` com `{cpf, nome, email, telefone, cep, numero, complemento}`.
  Valida CPF e e-mail únicos, completa o endereço pelo ViaCEP (salvando tudo no banco),
  gera a senha de 4 dígitos, grava hash SHA-256 + salt e envia a senha por e-mail.
  *Pronto quando:* o cliente criado consegue fazer login com a senha recebida.

- [ ] **B-12 — RF004: criar solicitação com descrição + categoria**
  Responsável: ____ · Depende de: B-05
  Alterar `CreateMaintenanceRequest` para
  `{customerId, equipmentDescription, equipmentTypeId, defectDescription}`.
  O service cria o `Equipment` a partir da descrição + categoria, grava data/hora,
  estado ABERTA e a primeira linha do histórico.

- [ ] **B-13 — RF003: listar solicitações de um cliente**
  Responsável: ____ · Depende de: B-12
  `GET /customers/{id}/maintenance-requests`, ordenado por `createdAt` crescente.
  Usar `@Query` com `JOIN FETCH` (equipamento, categoria, orçamento) para evitar N+1.

- [ ] **B-14 — RF012: efetuar orçamento**
  Responsável: ____ · Depende de: B-05
  `POST /maintenance-request/{id}/budget` com `{employeeId, value}`.
  Só a partir de ABERTA. Cria `Budget` com valor, funcionário e data/hora;
  solicitação passa para ORCADA e o histórico registra o funcionário.

- [ ] **B-15 — RF005: detalhe da solicitação com orçamento**
  Responsável: ____ · Depende de: B-14
  Garantir que `GET /maintenance-request/{id}` devolva dados completos da solicitação,
  do cliente, do equipamento e o valor orçado.

- [ ] **B-16 — RF006: aprovar serviço**
  Responsável: ____ · Depende de: B-14
  `POST /maintenance-request/{id}/approve` com o cliente dono da solicitação.
  ORCADA → APROVADA, com histórico. Resposta traz o valor aprovado.

- [/] **B-17 — RF017: remover categoria (desativação)**
  Responsável: JOAO VICTOR · Depende de: B-04
  `DELETE /equipment-type/{id}` marcando `active = false`;
  `GET /equipment-type` passa a listar só ativos. Validar descrição não vazia e única.

- [ ] **B-18 — RF018: CRUD de funcionários (leitura e criação)**
  Responsável: JOAO VICTOR · Depende de: B-04, B-06
  Criar `EmployeeService` + `EmployeeController`:
  `GET /employees` (só ativos), `GET /employees/{id}` e
  `POST /employees` com `{nome, email, dataNascimento, senha}` — e-mail único, senha com
  hash SHA-256 + salt, perfil `FUNCIONARIO`.

- [ ] **B-19 — RF018: atualizar e remover funcionário**
  Responsável: JOAO VICTOR · Depende de: B-18
  `PUT /employees/{id}` (senha só é alterada se enviada) e `DELETE /employees/{id}`
  com desativação lógica. Regras: não pode remover a si mesmo; não pode remover se for
  o único funcionário ativo.

- [ ] **B-20 — Teste manual do fluxo mínimo**
  Responsável: ____ · Depende de: B-11 … B-19
  Subir a aplicação e validar por `curl`/Postman:
  autocadastro → login → criar solicitação → orçar → aprovar, além dos CRUDs.

---

## Fase 2 — Fluxo do cliente

- [ ] **B-21 — RF007: rejeitar serviço**
  Responsável: ____ · Depende de: B-16
  `POST /maintenance-request/{id}/reject` com `{customerId, reason}`.
  Motivo obrigatório, gravado em `rejectionReason`. ORCADA → REJEITADA.

- [ ] **B-22 — RF009: resgatar serviço**
  Responsável: ____ · Depende de: B-21
  `POST /maintenance-request/{id}/rescue`. REJEITADA → APROVADA, com data/hora no histórico.

- [ ] **B-23 — RF010: pagar serviço**
  Responsável: ____ · Depende de: B-05
  `POST /maintenance-request/{id}/pay`. ARRUMADA → PAGA, gravando a data/hora do pagamento.

- [ ] **B-24 — RF008: detalhe completo com histórico**
  Responsável: ____ · Depende de: B-21, B-23
  Revisar `MaintenanceRequestDetails` e os mappers para devolver orçamento, manutenção,
  motivo da rejeição, pagamento, finalização e o histórico completo
  (data/hora + funcionário de cada passo) em ordem cronológica.

---

## Fase 3 — Fluxo do funcionário

- [ ] **B-25 — RF014: efetuar manutenção**
  Responsável: ____ · Depende de: B-05
  `POST /maintenance-request/{id}/maintenance` com
  `{employeeId, description, customerInstructions}`.
  A partir de APROVADA ou REDIRECIONADA. Grava data/hora e funcionário; estado vira ARRUMADA.

- [ ] **B-26 — RF015: redirecionar manutenção**
  Responsável: ____ · Depende de: B-02, B-05
  `POST /maintenance-request/{id}/redirect` com `{sourceEmployeeId, destinationEmployeeId}`.
  Proibir destino igual à origem (`SELF_REDIRECT_NOT_ALLOWED`); permitir redirecionamentos
  infinitos. Cria `Redirect` ligado à solicitação; estado REDIRECIONADA; histórico registra
  data/hora, funcionário origem e destino.

- [ ] **B-27 — RF016: finalizar solicitação**
  Responsável: ____ · Depende de: B-23
  `POST /maintenance-request/{id}/finish` com `{employeeId}`.
  PAGA → FINALIZADA, gravando data/hora e responsável.

- [ ] **B-28 — RF013: listagem de solicitações com filtros**
  Responsável: ____ · Depende de: B-26
  `GET /maintenance-request?filter=TODAY|PERIOD|ALL&start=&end=&employeeId=`,
  ordenado por `createdAt` crescente.
  Regra importante: solicitações REDIRECIONADAS só aparecem para o funcionário que é o
  destino do último redirecionamento. Implementar com `JOIN` na consulta, não em memória.

---

## Fase 4 — Relatórios em PDF

- [ ] **B-29 — Adicionar biblioteca de PDF**
  Responsável: ____ · Depende de: —
  Incluir OpenPDF/iText no `pom.xml` e criar um helper de geração
  (cabeçalho, tabela, rodapé) reaproveitável pelos dois relatórios.

- [ ] **B-30 — RF019: relatório de receitas por período**
  Responsável: ____ · Depende de: B-23, B-29
  `GET /reports/revenue?start=&end=` (ambas as datas opcionais), devolvendo
  `application/pdf` com as receitas das solicitações pagas **agrupadas por dia**.
  Consulta agregada no banco; valores e datas no formato brasileiro (R$ e dd/MM/yyyy).

- [ ] **B-31 — RF020: relatório de receitas por categoria**
  Responsável: ____ · Depende de: B-29
  `GET /reports/revenue-by-category`, PDF com a receita total de sempre agrupada por
  categoria de equipamento, usando `JOIN` + `GROUP BY` no banco.

---

## Fase 5 — Dados, validação e qualidade

- [ ] **B-32 — Ampliar a massa de testes do seed**
  Responsável: ____ · Depende de: B-03
  `seed.sql` deve ter 2 funcionários (Maria e Mário), 4 clientes (João, José, Joana, Joaquina),
  5 categorias (Notebook, Desktop, Impressora, Mouse, Teclado) e **no mínimo 20 solicitações**
  (hoje são 8), cobrindo os 8 estados, com datas distintas, clientes e funcionários variados,
  orçamentos, manutenções, redirecionamentos, pagamentos e histórico coerente.

- [ ] **B-33 — Validação de todos os DTOs**
  Responsável: ____ · Depende de: Fases 1–3
  Aplicar Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@Positive`, `@PastOrPresent`,
  regex de CPF/CEP/telefone) em todos os records de entrada e conferir se o
  `GlobalExceptionHandler` devolve mensagens em português.

- [ ] **B-34 — Testes automatizados do núcleo**
  Responsável: ____ · Depende de: Fases 1–3
  `@DataJpaTest` para as consultas de listagem e relatórios; `@SpringBootTest` + MockMvc
  para o fluxo feliz completo e para transições de estado inválidas.

- [ ] **B-35 — Limpeza final**
  Responsável: ____ · Depende de: todas
  Rodar `./mvnw spotless:apply verify`, tirar a senha do banco de `application.properties`
  (usar variável de ambiente) e revisar nomes de pacotes/classes.

- [ ] **B-36 — Documentar as suposições**
  Responsável: ____ · Depende de: todas
  Registrar no arquivo .doc/.odt exigido pelo enunciado tudo o que a equipe decidiu por conta
  própria no backend (identificação do usuário, regras de transição, remoção lógica, etc.).

---

## Sugestão de divisão entre a equipe

| Integrante | Trilha sugerida |
|------------|-----------------|
| 1 | B-01 a B-07 (fundações) e depois B-28 |
| 2 | B-08 a B-11 (autocadastro, ViaCEP, e-mail) |
| 3 | B-12 a B-16 (solicitação, orçamento, aprovação) |
| 4 | B-17 a B-19 (CRUDs) e depois B-29 a B-31 (relatórios) |
| 5 | B-21 a B-27 (demais transições de estado) |
| Todos | B-32 a B-36 na reta final |
