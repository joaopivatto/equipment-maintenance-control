# Plano de trabalho - Frontend

> **Objetivo:** entregar o frontend com dados mockados em 10 dias, priorizando os requisitos mínimos de defesa.
>
> **Regra de uso:** marque uma tarefa com `[/]` quando alguém começar e com `[x]` somente quando ela estiver navegável, testada manualmente e sem quebrar as rotas existentes.

## Visão geral

| Pessoa | Área principal | Prioridade inicial |
|---|---|---|
| 1 | Fundação e autenticação | Rotas, layouts, models, mocks, login e cadastro |
| 2 | Área do cliente | Solicitações, detalhes e histórico |
| 3 | Orçamento e ações do cliente | Orçar, aprovar, rejeitar, resgatar e pagar |
| 4 | Área do funcionário | Atendimento, manutenção, redirecionamento e filtros |
| 5 | Administração e qualidade | Categorias, funcionários, relatórios e acabamento |

## Acordos do grupo

- Os dados mockados devem ficar em serviços, nunca diretamente nos componentes.
- Cada alteração de estado de uma solicitação deve gerar um item no histórico.
- Antes de criar uma nova tela, confira se a rota e o model já foram definidos.
- Cada pessoa deve trabalhar prioritariamente nos arquivos da própria feature.
- Antes de marcar uma tarefa como concluída, testar o fluxo no Firefox.
- Para evitar conflitos, a Pessoa 1 é responsável pelos arquivos globais (`app.routes.ts`, `app.config.ts`, layouts e models compartilhados).

---

## P0 - Fundação e autenticação

**Responsável: João Victor Dourado + Matheus Alves** 

- [x] **P0-01 - Validar o projeto atual**
  - Rodar build e testes.
  - Corrigir erros de import, rota ou template antes de novas implementações.

- [x] **P0-02 - Separar layout público e autenticado**
  - Login e autocadastro sem sidebar.
  - Cliente e funcionário com sidebar, perfil mockado e ação de logout.

- [X] **P0-03 - Consolidar rotas globais**
  - `/` redireciona para `/login`.
  - Criar rota curinga para URLs inexistentes.
  - Manter áreas de autenticação, cliente, funcionário e administração separadas.

- [X] **P0-04 - Consolidar models e enums compartilhados**
  - Status de solicitação.
  - Solicitação, histórico, orçamento, manutenção e redirecionamento.
  - Cliente, endereço, funcionário e categoria.

- [X] **P0-05 - Criar massa de dados mockada**
  - 2 funcionários: Maria e Mário.
  - 4 clientes: João, José, Joana e Joaquina.
  - 5 categorias: Notebook, Desktop, Impressora, Mouse e Teclado.
  - Pelo menos 20 solicitações com datas, estados e históricos variados.

- [/] **P0-06 - Criar sessão mockada**
  - Usuário atual, tipo de perfil e logout.
  - Login redireciona cliente e funcionário para áreas distintas.

- [/] **P0-07 - Implementar Login (RF002)**
  - Campos de e-mail e senha com validação.
  - Mensagem para credenciais inválidas.
  - Link para autocadastro.

- [/] **P0-08 - Implementar Autocadastro de cliente (RF001)**
  - CPF, nome, e-mail, telefone e endereço completo.
  - Validações e mensagens de erro.
  - Confirmação visual de cadastro realizado.

- [/] **P0-09 - Preenchimento de endereço por CEP**
  - Preencher endereço ao informar CEP válido.
  - Definir fallback mockado se a ViaCEP estiver indisponível.

---

## P0 - Fluxo de solicitações do cliente

**Responsável: Pessoa 2**

> Depende de P0-04 e P0-05.

- [ ] **P0-10 - Criar solicitação de manutenção (RF004)**
  - Descrição do equipamento, categoria e defeito.
  - Criar data/hora e estado `ABERTA`.
  - Gravar no serviço mockado e retornar à lista.

- [ ] **P0-11 - Página inicial do cliente (RF003)**
  - Lista ordenada por data/hora crescente.
  - Equipamento limitado a 30 caracteres.
  - Cores e ações corretas por status.

- [ ] **P0-12 - Visualizar solicitação e histórico (RF008)**
  - Dados completos de solicitação, cliente, equipamento e categoria.
  - Linha do tempo com status, data/hora e responsável.
  - Ações disponíveis conforme o status atual.

---

## P0 - Orçamento e ações do cliente

**Responsável: Pessoa 3**

> Depende de P0-05 e P0-12.

- [x] **P0-13 - Mostrar orçamento (RF005)**
  - Dados completos da solicitação.
  - Valor em destaque.
  - Botões Aprovar e Rejeitar.

- [x] **P0-14 - Aprovar serviço (RF006)**
  - Atualizar estado para `APROVADA`.
  - Mostrar confirmação com valor.
  - Registrar evento no histórico.

- [x] **P0-15 - Rejeitar serviço (RF007)**
  - Coletar motivo da rejeição.
  - Atualizar estado para `REJEITADA`.
  - Registrar motivo e evento no histórico.

---

## P0 - Fluxo do funcionário

**Responsável: Pessoa 4**

> Depende de P0-04 e P0-05.

- [ ] **P0-16 - Página inicial do funcionário (RF011)**
  - Mostrar solicitações `ABERTA`.
  - Exibir data/hora, cliente e equipamento.
  - Oferecer ação para efetuar orçamento.

- [ ] **P0-17 - Efetuar orçamento (RF012)**
  - Exibir solicitação e dados do cliente.
  - Registrar valor, funcionário e data/hora.
  - Atualizar estado para `ORCADA` e criar histórico.

---

## P0 - Administração

**Responsável: João Guilherme Pivatto**

- [ X ] **P0-18 - Revisar CRUD de categorias (RF017)**
  - Criar, listar_, editar e desativar categorias.
  - Confirmar remoção e impedir dados inválidos/duplicados.
  - Integrar seleção de categorias com a criação de solicitação.

- [ X ] **P0-19 - Revisar CRUD de funcionários (RF018)**
  - Criar, listar, editar e desativar funcionários.
  - Impedir remover a si mesmo e o único funcionário ativo.
  - Validar e-mail e data de nascimento.

---

## P1 - Estados avançados e relatórios

> Iniciar somente após os itens P0 estarem navegáveis.

- [X] **P1-01 - Resgatar serviço (RF009)** - Pessoa 3
  - `REJEITADA` para `APROVADA`, com evento no histórico.

- [X] **P1-02 - Pagar serviço (RF010)** - Pessoa 3
  - Exibir valor, confirmar pagamento, registrar data/hora e mudar para `PAGA`.

- [X] **P1-03 - Efetuar manutenção (RF014)** - Pessoa 4
  - Descrição da manutenção, orientações, funcionário responsável e estado `ARRUMADA`.

- [X] **P1-04 - Redirecionar manutenção (RF015)** - Pessoa 4
  - Escolher destino, impedir redirecionamento para si mesmo e registrar origem/destino.

- [X] **P1-05 - Finalizar solicitação (RF016)** - Pessoa 4
  - Estado `FINALIZADA`, data/hora e funcionário responsável.

- [X] **P1-06 - Lista avançada do funcionário (RF013)** - Pessoa 4
  - Filtros Hoje, Período e Todas.
  - Todos os estados com cores oficiais.
  - Regra de visibilidade das solicitações redirecionadas.

- [ ] **P1-07 - Relatório de receita por período (RF019)** - Pessoa 5
  - Filtro de datas e agrupamento por dia.

- [ ] **P1-08 - Receita por categoria (RF020)** - Pessoa 5
  - Agrupar receitas desde sempre por categoria.

---

## P1 - Qualidade e apresentação

**Responsável principal: Pessoa 5, com apoio do grupo**

- [ ] **P1-09 - Padronizar feedbacks**
  - Substituir `alert` e `confirm` por `Toast` e `ConfirmDialog` do PrimeNG.

- [ ] **P1-10 - Formatação brasileira e máscaras**
  - CPF, telefone, CEP, data/hora e valores monetários.

- [ ] **P1-11 - Responsividade e acessibilidade**
  - Desktop e mobile.
  - Rótulos, mensagens de erro, navegação por teclado e textos alternativos.

- [ ] **P1-12 - Testes de fluxos críticos**
  - Login, cadastro, criar solicitação, orçar, aprovar e CRUDs.

- [ ] **P1-13 - Validação final no Firefox**
  - Testar todas as rotas, botões, formulários, filtros e confirmações.

---

## Cronograma de 10 dias

| Dia | Meta |
|---|---|
| 1 | P0-01 a P0-05: base técnica, contratos e massa mockada definidos. |
| 2 | P0-02, P0-03, P0-06: layouts, rotas e sessão mockada. |
| 3 a 5 | Login, cadastro, solicitação do cliente, detalhes e orçamento. |
| 6 e 7 | Dashboard do funcionário, orçamento, categorias e funcionários. |
| 8 | Estados avançados e relatórios, se os itens P0 estiverem concluídos. |
| 9 | Integração entre fluxos, feedbacks, responsividade e correções. |
| 10 | Firefox, testes finais e ensaio de apresentação. |

## Checklist de entrega

- [ ] Todas as rotas principais são acessíveis pelo menu e pela URL.
- [ ] Não há botões que terminam apenas em `alert` ou `console.log`.
- [ ] A massa mockada permite demonstrar todos os estados e fluxos principais.
- [ ] Login e cadastro funcionam visualmente.
- [ ] Cliente abre, visualiza e responde a um orçamento.
- [ ] Funcionário visualiza solicitações abertas e cria orçamento.
- [ ] CRUD de categorias e funcionários está completo.
- [ ] Remoções são confirmadas e usam desativação.
- [ ] O projeto compila e funciona no Firefox.
