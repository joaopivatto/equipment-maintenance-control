# Plano de trabalho - Frontend

> **Objetivo:** concluir o protótipo e entregar o sistema integrado (Angular + REST/Spring Boot + MySQL), atendendo RF001–RF020 e aos requisitos não funcionais do enunciado.
>
> **Regra de uso:** marque uma tarefa com `[/]` quando alguém começar e com `[x]` somente quando ela estiver navegável, testada manualmente e sem quebrar as rotas existentes.

> **Status:** os itens P0/P1 abaixo preservam o histórico do protótipo; `[x]` nessa etapa não comprova integração com a API. O backlog de entrega final está na seção **Fase final**, com todos os itens ainda sujeitos a implementação/validação. O README da raiz atualmente não contém a tabela RF001–RF020 citada anteriormente.

## Visão geral

| Pessoa | Área principal               | Prioridade inicial                                  |
| ------ | ---------------------------- | --------------------------------------------------- |
| 1      | Fundação e autenticação      | Rotas, layouts, models, mocks, login e cadastro     |
| 2      | Área do cliente              | Solicitações, detalhes e histórico                  |
| 3      | Orçamento e ações do cliente | Orçar, aprovar, rejeitar, resgatar e pagar          |
| 4      | Área do funcionário          | Atendimento, manutenção, redirecionamento e filtros |
| 5      | Administração e qualidade    | Categorias, funcionários, relatórios e acabamento   |

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

- [x] **P0-03 - Consolidar rotas globais**
  - `/` redireciona para `/login`.
  - Criar rota curinga para URLs inexistentes.
  - Manter áreas de autenticação, cliente, funcionário e administração separadas.

- [x] **P0-04 - Consolidar models e enums compartilhados**
  - Status de solicitação.
  - Solicitação, histórico, orçamento, manutenção e redirecionamento.
  - Cliente, endereço, funcionário e categoria.

- [x] **P0-05 - Criar massa de dados mockada**
  - 2 funcionários: Maria e Mário.
  - 4 clientes: João, José, Joana e Joaquina.
  - 5 categorias: Notebook, Desktop, Impressora, Mouse e Teclado.
  - Pelo menos 20 solicitações com datas, estados e históricos variados.

- [x] **P0-06 - Criar sessão mockada**
  - Usuário atual, tipo de perfil e logout.
  - Login redireciona cliente e funcionário para áreas distintas.

- [x] **P0-07 - Implementar Login (RF002)**
  - Campos de e-mail e senha com validação.
  - Mensagem para credenciais inválidas.
  - Link para autocadastro.

- [x] **P0-08 - Implementar Autocadastro de cliente (RF001)**
  - CPF, nome, e-mail, telefone e endereço completo.
  - Validações e mensagens de erro.
  - Confirmação visual de cadastro realizado.

- [x] **P0-09 - Preenchimento de endereço por CEP**
  - Preencher endereço ao informar CEP válido.
  - Definir fallback mockado se a ViaCEP estiver indisponível.

---

## P0 - Fluxo de solicitações do cliente

**Responsável: João Guilherme Pivatto**

> Depende de P0-04 e P0-05.

- [x] **P0-10 - Criar solicitação de manutenção (RF004)**
  - Descrição do equipamento, categoria e defeito.
  - Criar data/hora e estado `ABERTA`.
  - Gravar no serviço mockado e retornar à lista.

- [x] **P0-11 - Página inicial do cliente (RF003)**
  - Lista ordenada por data/hora crescente.
  - Equipamento limitado a 30 caracteres.
  - Cores e ações corretas por status.

- [x] **P0-12 - Visualizar solicitação e histórico (RF008)**
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

- [x] **P0-18 - Revisar CRUD de categorias (RF017)**
  - Criar, listar_, editar e desativar categorias.
  - Confirmar remoção e impedir dados inválidos/duplicados.
  - Integrar seleção de categorias com a criação de solicitação.

- [x] **P0-19 - Revisar CRUD de funcionários (RF018)**
  - Criar, listar, editar e desativar funcionários.
  - Impedir remover a si mesmo e o único funcionário ativo.
  - Validar e-mail e data de nascimento.

---

## P1 - Estados avançados e relatórios

> Iniciar somente após os itens P0 estarem navegáveis.

- [x] **P1-01 - Resgatar serviço (RF009)** - Pessoa 3
  - `REJEITADA` para `APROVADA`, com evento no histórico.
  - A ação continua separada na tela, mas na integração consome o mesmo endpoint da aprovação
    (`POST /maintenance-request/{id}/approve`), que aceita `ORCADA` ou `REJEITADA` como origem.

- [x] **P1-02 - Pagar serviço (RF010)** - Pessoa 3
  - Exibir valor, confirmar pagamento, registrar data/hora e mudar para `PAGA`.

- [x] **P1-03 - Efetuar manutenção (RF014)** - Pessoa 4
  - Descrição da manutenção, orientações, funcionário responsável e estado `ARRUMADA`.

- [x] **P1-04 - Redirecionar manutenção (RF015)** - Pessoa 4
  - Escolher destino, impedir redirecionamento para si mesmo e registrar origem/destino.

- [x] **P1-05 - Finalizar solicitação (RF016)** - Pessoa 4
  - Estado `FINALIZADA`, data/hora e funcionário responsável.

- [x] **P1-06 - Lista avançada do funcionário (RF013)** - Pessoa 4
  - Filtros Hoje, Período e Todas.
  - Todos os estados com cores oficiais.
  - Regra de visibilidade das solicitações redirecionadas.

- [x] **P1-07 - Relatório de receita por período (RF019)** - Pessoa 5
  - Filtro de datas e agrupamento por dia.

- [x] **P1-08 - Receita por categoria (RF020)** - Pessoa 5
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

> Cronograma original do protótipo. Para a entrega integrada, seguir a sequência da Fase final; não há data final de entrega informada nesta análise.

| Dia   | Meta                                                                 |
| ----- | -------------------------------------------------------------------- |
| 1     | P0-01 a P0-05: base técnica, contratos e massa mockada definidos.    |
| 2     | P0-02, P0-03, P0-06: layouts, rotas e sessão mockada.                |
| 3 a 5 | Login, cadastro, solicitação do cliente, detalhes e orçamento.       |
| 6 e 7 | Dashboard do funcionário, orçamento, categorias e funcionários.      |
| 8     | Estados avançados e relatórios, se os itens P0 estiverem concluídos. |
| 9     | Integração entre fluxos, feedbacks, responsividade e correções.      |
| 10    | Firefox, testes finais e ensaio de apresentação.                     |

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

---

## Fase final - Diagnóstico e regras de conclusão

Análise estática em **13/10/2026**, confrontando `src/app` do frontend, controllers, DTOs, services, mappers e seed do backend, workflows e o enunciado `Manutenção de Equipamentos - 2024-2.pdf`. O PDF é referência de requisitos do sistema; esta atividade altera apenas o planejamento, sem implementar as funcionalidades descritas nele.

- `app.config.ts` registra quatro clientes mockados e não configura `provideHttpClient`. As telas existentes ainda não demonstram persistência no banco.
- RF011/RF012 já possuem código em `employee-home` e `budget`, apesar de P0-16/P0-17 continuarem abertos. Revisar e validar essas implementações, sem recriar telas.
- O backend já expõe rejeição, pagamento, resgate via aprovação, manutenção, redirecionamento e finalização. Alguns itens do `ROADMAP.md` estão desatualizados; usar o código como referência para os contratos atuais.
- `LoginResponse` retorna apenas `profileType` e `userId`; `SessionUser` exige `id`, `name` e `email`. `CurrentEmployeeService` ainda relaciona sessão e funcionário por e-mail.
- `MaintenanceRequest` no frontend não possui `customerId`; a lista do cliente usa `listAll()` e compara `customerName` com o nome da sessão.
- `BaseMapper` devolve datas em `dd/MM/yyyy HH:mm`/`dd/MM/yyyy`; componentes e relatórios usam `Date`, `DatePipe` e strings ISO. O backend serializa `ORCADA`, enquanto o enum do frontend usa `ORÇADA`.
- RF019/RF020 já têm geração de PDF no frontend (`jsPDF`), mas usam dados simulados; não existem controllers de relatórios no backend. RF019 hoje impede datas vazias e RF020 percorre apenas categorias retornadas pela listagem, que exclui categorias desativadas.
- `MaintenanceRequestHistoryMapper` devolve `fromEmployee` e `toEmployee` nulos. O detalhe não entrega todos os dados do cliente nem a data/hora da manutenção em `MaintenanceDetails`.
- `seed.sql` possui dois funcionários, somente dois clientes e cinco categorias, sem solicitações inseridas. Os mocks geram 25 solicitações, o que não substitui a massa persistida exigida na entrega final.
- Há testes automatizados existentes, inclusive de agrupamento de receitas; ampliar cobertura de integração e regras. A CI atual verifica apenas o backend.

**Legenda:** P0 = bloqueia integração/fluxo mínimo; P1 = obrigatório para entrega completa; P2 = acabamento e entrega. Responsáveis indicam a trilha do grupo, sem atribuir novas pessoas nominalmente. Dependências `BF-*` são ajustes do backend acompanhados neste arquivo, sem alterar o roadmap dele.

**Pronto quando:** o critério de aceite da tarefa foi atendido com API e banco reais, os erros foram tratados e o fluxo foi verificado no Firefox. Registrar evidência no PR ou roteiro de testes. Não concluir tarefas apenas porque funcionam com mock. Build/testes e execução no navegador não foram realizados nesta análise documental.

### P0 - Contratos, infraestrutura e autenticação

- [ ] **F-01 - Configurar acesso HTTP e ambientes**
  - Responsável: Fundação e autenticação.
  - Configurar `provideHttpClient`, URL base da API e clientes reais para Auth, Employee, EquipmentCategory e MaintenanceRequest; selecionar mocks somente no ambiente de protótipo/testes.
  - Validar CORS local e URL/proxy de produção, sem endereços de desenvolvimento fixos no build final.
  - **Aceite:** uma chamada real funciona no navegador e uma alteração persiste após recarregar a página.

- [ ] **F-02 - Definir DTOs e adaptadores dos contratos reais**
  - Responsável: Fundação e autenticação; depende de F-01.
  - Mapear `userId` para a identidade da sessão, categoria `description` para `name`, `equipmentTypeId` na criação e `maintenance.description/instructions` para os campos usados pelas telas.
  - Acrescentar `customerId`, `customerEmail`, `updatedAt` e dados completos necessários aos models; tratar campos nulos, respostas sem corpo e HTTP 404 em vez de depender de `undefined` emitido pelo mock.
  - Separar status de transporte (`ORCADA`) do rótulo (`ORÇADA`). Definir datas ISO na API ou conversão explícita das datas brasileiras atuais, sem usar parsing implícito de `new Date('dd/MM/yyyy')`.
  - **Aceite:** fixtures dos DTOs atuais alimentam listas, detalhes, histórico e relatórios sem status desconhecido, data inválida ou campo perdido.

- [ ] **F-03 - Integrar login, sessão e logout (RF002)**
  - Responsável: Fundação e autenticação; depende de F-01/F-02 e BF-01.
  - Consumir `POST /auth/login`, usar perfil e ID reais e resolver nome/e-mail necessários ao layout. Revisar `CurrentEmployeeService` para usar a identidade real, sem pressupor IDs diferentes como nos mocks.
  - Tratar credenciais inválidas, usuário inativo, indisponibilidade, carregamento e duplo clique. Limpar sessão/dados ao sair e definir comportamento ao recarregar/expirar a sessão.
  - **Aceite:** Maria/Mário e clientes acessam suas áreas; trocar de usuário não reaproveita dados do anterior.

- [ ] **F-04 - Integrar autocadastro e ViaCEP (RF001)**
  - Responsável: Fundação e autenticação; depende de F-01/F-02.
  - Consumir `POST /auth/signup` (o endpoint atual não é `POST /customers`), que responde 201 sem corpo. Enviar CPF/telefone/CEP normalizados e endereço completo, com `number` numérico conforme o DTO.
  - Substituir `AddressMockFactory` por consulta real à ViaCEP; limpar dados de CEP anterior e tratar CEP inexistente, timeout e falha de conexão. Não preencher endereço fictício na entrega final.
  - Apresentar erros de CPF/e-mail duplicados e orientar sobre a senha de quatro números enviada por e-mail, sem expô-la na resposta/tela.
  - **Aceite:** endereço correto é persistido; cliente recebe a senha e consegue entrar. Validar configuração de envio com o backend.

- [ ] **F-05 - Restringir rotas e ações por perfil e proprietário**
  - Responsável: Fundação e autenticação; depende de F-03 e BF-01.
  - Incluir proteção de funcionário nas rotas `employee-home`, `maintenance` e `redirect`, e de cliente em `new`. A rota compartilhada `budget` deve permitir orçar só ao funcionário e aprovar/rejeitar só ao cliente dono.
  - Revisar acesso direto a detalhes, botões e menus; validar IDs ausentes/inválidos e respostas 403/404. Guards não substituem autorização no servidor.
  - **Aceite:** mudar URL ou ID não permite operar como outro perfil nem consultar solicitação de outro cliente.

### P0 - Integração do fluxo mínimo

- [ ] **F-06 - Persistir solicitação e listar por cliente (RF003/RF004)**
  - Responsável: Área do cliente; depende de F-02/F-03.
  - Enviar `{customerId, equipmentDescription, equipmentTypeId, defectDescription}` a `POST /maintenance-request`; não usar nomes como identidade.
  - Consumir `GET /customers/{id}/maintenance-requests` na área do cliente, removendo filtro por nome e carregamento de todas as solicitações. Usar categorias ativas da API.
  - **Aceite:** clientes homônimos não compartilham pedidos; nova solicitação volta como ABERTA com data/hora/histórico do servidor e permanece após reload.

- [ ] **F-07 - Validar dashboard do funcionário e persistir orçamento (RF011/RF012)**
  - Responsável: Área do funcionário; depende de F-02/F-03.
  - Aproveitar telas existentes; consumir `GET /maintenance-request/open` e `POST /maintenance-request/{id}/budget` com `{employeeId, value}` do funcionário logado.
  - Exibir aberturas em ordem crescente e descrição limitada a 30 caracteres; mostrar dados completos de solicitação/cliente e impedir valor zero/negativo ou envio repetido.
  - **Aceite:** orçamento muda ABERTA para ORÇADA e registra valor, autor e horário reais; validar P0-16/P0-17 antes de marcá-los.

- [ ] **F-08 - Integrar categorias e funcionários (RF017/RF018)**
  - Responsável: Administração; depende de F-02/F-03.
  - Implementar GET/POST/PUT/DELETE de `/equipment-type` e `/employees`, mapeando os DTOs e respostas 204. Enviar `currentEmployeeId` na query de remoção de funcionário conforme controller atual.
  - Tratar duplicidade, desativação, remoção de si mesmo e do último funcionário também por respostas da API; atualizar opções de categoria/destino depois de alterações.
  - Conferir senha na criação e decidir edição de senha com BF-05: `EmployeeUpdateRequest` atual não aceita senha.
  - **Aceite:** CRUDs persistem, remoções são confirmadas, registros inativos somem das opções e os vínculos históricos permanecem.

### P1 - Fluxo completo e histórico

- [ ] **F-09 - Integrar aprovação, rejeição e resgate (RF005/RF006/RF007/RF009)**
  - Responsável: Orçamento e ações do cliente; depende de F-02/F-05/F-06/F-07.
  - Aprovar com `{customerId}`; rejeitar com `{customerId, reason}`; resgatar usando o mesmo `/approve` a partir de REJEITADA, sem criar endpoint fictício.
  - Respeitar estado/perfil/proprietário e manter motivo da rejeição no histórico após resgate. Ajustar confirmação de aprovação ao enunciado: sucesso com valor e retorno à lista após OK; corrigir texto que afirma que rejeição não pode ser desfeita.
  - **Aceite:** cada ação usa retorno do servidor e gera um único evento persistido; erro/conflito não aparece como sucesso.

- [ ] **F-10 - Completar detalhe e histórico reais (RF008/RF012/RF014/RF015/RF016)**
  - Responsável: Área do cliente + Área do funcionário; depende de F-02 e BF-03.
  - Consumir `/maintenance-request/{id}`, exibir dados completos de cliente/solicitação, orçamento, manutenção, orientações, pagamento e finalização; incluir autores e datas/horas.
  - Ordenar histórico cronologicamente e mostrar origem/destino de cada redirecionamento, inclusive sucessivos. Preservar rejeição anterior quando o pedido já foi resgatado.
  - **Aceite:** timeline coincide com banco/API após reload, sem fabricar autor ou redirecionamento no frontend.

- [ ] **F-11 - Integrar pagamento e revisar sua apresentação (RF010)**
  - Responsável: Orçamento e ações do cliente; depende de F-09/F-10 e BF-01.
  - Confirmar pagamento por `POST /maintenance-request/{id}/pay` (atualmente sem corpo). Mostrar dados da solicitação e valor em destaque na tela/diálogo antes de confirmar.
  - Bloquear envio repetido, tratar conflito de estado e atualizar pagamento/status/histórico pela resposta real. Não há integração financeira exigida pelo enunciado.
  - **Aceite:** somente o dono paga pedido ARRUMADA; PAGA e data/hora persistem e alimentam receitas uma única vez.

- [ ] **F-12 - Integrar manutenção, redirecionamento e finalização (RF014/RF015/RF016)**
  - Responsável: Área do funcionário; depende de F-05/F-10/F-11.
  - Enviar manutenção `{employeeId, description, customerInstructions}`, redirecionamento `{sourceEmployeeId, destinationEmployeeId}` e finalização `{employeeId}` aos endpoints existentes.
  - Validar campos, destino ativo diferente do funcionário logado e origem/responsável real; permitir redirecionamentos sucessivos. Somente PAGA pode ser finalizada.
  - **Aceite:** fluxo APROVADA/REDIRECIONADA → ARRUMADA → PAGA → FINALIZADA funciona entre usuários com autores e horários persistidos.

- [ ] **F-13 - Integrar filtros e visibilidade do funcionário (RF013)**
  - Responsável: Área do funcionário; depende de F-02/F-03/F-12 e BF-02.
  - Manter Hoje/Período/Todas, validar início/fim e ordenar por abertura crescente. Definir filtros na API com BF-02; o GET atual não recebe parâmetros.
  - Exibir REDIRECIONADA apenas ao destino do último redirecionamento, inclusive após múltiplos encaminhamentos; não reutilizar essa listagem restrita como fonte de toda a receita da empresa.
  - **Aceite:** Maria e Mário veem resultados corretos e limites de dia/período incluem os horários esperados.

### P1 - Relatórios e PDFs

- [ ] **F-14 - Permitir datas opcionais no relatório (RF019)**
  - Responsável: Administração e qualidade; depende de F-02.
  - Adaptar tela, models, `ReportsService` e cabeçalho do PDF para data inicial/final opcionais: ambas vazias, só início, só fim ou ambas preenchidas.
  - Validar ordem quando ambas existirem; agrupar receita por data de pagamento, incluir PAGA/FINALIZADA e conferir arredondamento monetário.
  - **Aceite:** quatro combinações de filtros funcionam com resultados conhecidos, sem erro de data nem exclusão de receita após finalização.

- [ ] **F-15 - Conectar relatórios a receitas persistidas (RF019/RF020)**
  - Responsável: Administração e qualidade; depende de F-11/F-14 e BF-04.
  - Acordar fonte de dados com backend. A geração via `jsPDF` já existe e pode ser mantida com dados reais; se adotados endpoints PDF, consumir como Blob e tratar falhas de download. Geração no servidor é decisão técnica, não exigência do PDF.
  - Receita por categoria deve considerar todo o histórico, independente do filtro diário, incluindo receitas de categorias desativadas. Corrigir dependência exclusiva de `categoryApiClient.listAll()`.
  - **Aceite:** PDF e tela conferem com valores do banco; Maria/Mário recebem totais da empresa iguais, sem perdas causadas pela regra de redirecionamento.

- [ ] **F-16 - Validar PDFs dos dois relatórios**
  - Responsável: Administração e qualidade; depende de F-14/F-15.
  - Testar agrupamento por dia/categoria, período sem receita, datas abertas, nomes acentuados, categorias inativas e volume que produza múltiplas páginas.
  - Revisar cabeçalhos, R$, datas brasileiras, paginação, quebra de tabelas e nome de download; decidir/documentar PDF único com seções ou arquivos separados.
  - **Aceite:** RF019/RF020 identificáveis e legíveis, sem cortes, valores incorretos ou datas inválidas.

### P1/P2 - Qualidade, operação e entrega

- [ ] **F-17 - Padronizar erros HTTP e estados de carregamento (P1)**
  - Responsável: Fundação + todas as features; depende de F-01.
  - Mapear `GlobalExceptionHandler`/`ErrorCode` para mensagens em português; distinguir validação, credenciais inválidas, proibição, registro inexistente, conflito e indisponibilidade.
  - Nas subscriptions de listas/formulários/ações, garantir encerramento do loading/submitting também em erro, manter dados digitados e permitir nova tentativa. Desabilitar botões durante envio e evitar sucesso antes da resposta.
  - **Aceite:** API fora do ar ou resposta 400/403/404/409/500 não deixa tela travada nem produz confirmação falsa.

- [ ] **F-18 - Concluir feedbacks, validações e máscaras (P1)**
  - Responsável: Administração e qualidade + todas as features; relaciona P1-09/P1-10.
  - Substituir `confirm()` nativo ainda usado em `category-list` por ConfirmDialog; manter confirmação de toda desativação.
  - Alinhar campos aos DTOs: descrições de solicitação/manutenção/orientações até 255, rejeição até 500, senha de login/criação de funcionário com quatro caracteres e regra numérica definida em conjunto.
  - Aplicar CPF/telefone/CEP, calendários em pt-BR e moeda; normalizar somente no envio, sem perder máscara na tela. Datas de nascimento no formato de transporte acordado.
  - **Aceite:** campos inválidos são sinalizados antes do envio e erros do servidor aparecem junto ao contexto correto.

- [ ] **F-19 - Revisar cores, ações e limites de descrição (P1)**
  - Responsável: Área do cliente + Área do funcionário.
  - Centralizar escala RF013: cinza ABERTA, marrom ORÇADA, vermelho REJEITADA, amarelo APROVADA, roxo REDIRECIONADA, azul ARRUMADA, alaranjado PAGA e verde FINALIZADA.
  - Conferir consistência das listas, detalhes, timeline e legendas dos gráficos; evitar paletas divergentes. Limitar rótulo do equipamento a 30 caracteres, contando reticências.
  - **Aceite:** estado é reconhecível por texto e cor; ações oferecidas correspondem ao estado/perfil em cada entrada do fluxo.

- [ ] **F-20 - Responsividade e acessibilidade (P2)**
  - Responsável: Administração e qualidade; relaciona P1-11.
  - Revisar sidebar, tabelas, formulários, relatórios, diálogos e timeline horizontal em telas menores. Garantir labels, foco, teclado, contraste e mensagens acessíveis.
  - **Aceite:** fluxos completos funcionam em desktop e mobile sem campos/botões cortados, inclusive pelo teclado.

- [ ] **F-21 - Ampliar testes de contratos e regras críticas (P1)**
  - Responsável: Todas as trilhas; depende das integrações correspondentes; relaciona P1-12.
  - Testar clientes HTTP/adaptadores, status ORCADA, datas reais da API, identidade por ID, respostas 201/204, erros e limpeza de loading.
  - Cobrir perfil/proprietário, rejeição/resgate, pagamento duplicado, redirecionamentos sucessivos, filtros de fronteira, desativação e relatórios com datas opcionais/categoria inativa. Aproveitar testes existentes.
  - **Aceite:** testes exercitam comportamento e cenários de falha com fixtures determinísticas, além da criação de componentes.

- [ ] **F-22 - Adicionar verificações do frontend à CI (P1)**
  - Responsável: Fundação e autenticação.
  - Definir instalação reproduzível com lockfile, versão compatível de Node, build e `npm test -- --watch=false`; adicionar job do frontend em `.github/workflows/ci.yml`.
  - Conferir formatação e limites de bundle conforme configuração do projeto.
  - **Aceite:** PR com erro de compilação/teste do frontend falha na CI; verificações locais documentadas e passando.

- [ ] **F-23 - Preparar massa persistida e roteiro de demonstração (P1)**
  - Responsável: Todas as trilhas; depende de BF-06 e F-06 a F-15.
  - Validar 2 funcionários Maria/Mário, 4 clientes João/José/Joana/Joaquina, 5 categorias exigidas e pelo menos 20 solicitações com estados, datas e históricos coerentes no banco.
  - Tornar dados de testes reproduzíveis; evitar depender apenas de fábricas aleatórias dos mocks. Incluir rejeição/resgate, vários redirecionamentos e pagamentos em dias distintos.
  - **Aceite:** instalação limpa permite demonstrar RF001–RF020 com contas e resultados conhecidos.

- [ ] **F-24 - Validar sistema completo no Firefox (P1)**
  - Responsável: Grupo; depende de F-01 a F-23; relaciona P1-13.
  - Testar cadastro → e-mail → login → abertura → orçamento → aprovação → redirecionamento/manutenção → pagamento → finalização, e ramo rejeição → resgate.
  - Conferir menus/URLs, guards, refresh, troca de usuários, CRUDs, filtros, downloads, erros e persistência após reinício. Registrar resultados e corrigir falhas.
  - **Aceite:** todos os RFs têm evidência de execução real, sem botões sem efeito ou sucesso apenas simulado.

- [ ] **F-25 - Revisar publicação e documentação de entrega (P2)**
  - Responsável: Fundação + grupo; depende de F-22/F-24.
  - Validar build de produção, URL/proxy da API, CORS e fallback para `index.html` ao abrir rotas diretamente no servidor usado por `deploy.sh`.
  - Atualizar README com execução/configuração, contas de demonstração e situação real dos RFs; corrigir referências a tabela inexistente também no roadmap quando ele for revisado.
  - Preparar arquivo `.doc/.odt` exigido com suposições: sessão/autorização, datas/fuso, desativação, relatórios e compatibilidade da versão Angular usada com a exigência de standalone/v17 do enunciado.
  - **Aceite:** outra pessoa sobe o sistema com a documentação; entrega inclui código, banco/massa, documento de suposições e roteiro de defesa.

### Dependências do backend a acompanhar

Estas tasks são necessárias ao resultado do frontend, mas sua implementação pertence ao backend. O acompanhamento aqui evita concluir uma tela quando o contrato ainda não atende ao requisito.

- [ ] **BF-01 - Identidade, sessão e autorização** (bloqueia F-03/F-05/F-11)
  - Acordar contrato para recuperar nome/e-mail e validar identidade no servidor. Login atual não estabelece token/sessão verificável nos controllers analisados; IDs enviados pelo cliente não comprovam autoria.
  - Proteger acesso por perfil/proprietário, especialmente detalhe, listagens e pagamento sem cliente no corpo; verificar origem real de redirecionamento e impedir login de desativados.
  - **Aceite:** chamada direta com identidade/ID adulterados é rejeitada; dados da sessão bastam para os fluxos reais.

- [ ] **BF-02 - Filtros, ordenação e visibilidade de solicitações** (bloqueia F-13)
  - Implementar contrato de filtro Hoje/Período/Todas e último destino de REDIRECIONADA; GET atual apenas chama `findAll()`. Conferir ordenação e consultas com JOINs para detalhes/histórico, evitando N+1.
  - **Aceite:** resultados por funcionário/período corretos e previsíveis, com consulta adequada no banco.

- [ ] **BF-03 - Completar dados de detalhe e histórico** (bloqueia F-10)
  - Retornar telefone/endereço/CPF necessários aos dados completos do cliente, data/hora de manutenção e origem/destino de cada redirecionamento. Ordenar histórico e resolver corretamente último evento quando houver repetição de estado.
  - **Aceite:** RF008/RF012/RF014/RF015 podem ser apresentados usando somente dados reais da resposta.

- [ ] **BF-04 - Fonte confiável de relatórios** (bloqueia F-15)
  - Acordar endpoints JSON agregados ou PDFs (ainda não existem controllers de relatórios), com limites de data opcionais e receita de todo o histórico/categorias desativadas.
  - **Aceite:** totais conferem com pagamentos persistidos, sem depender da lista operacional restrita de cada funcionário.

- [ ] **BF-05 - Revisar validações e contratos administrativos** (acompanha F-04/F-08/F-18)
  - Validar CPF/e-mail únicos, inclusive conflito de e-mail entre perfis, senhas, datas, campos e mensagens em português. Decidir edição de senha: DTO atual de atualização não possui esse campo, apesar do roadmap prever.
  - Padronizar datas de transporte com F-02 e verificar persistência de endereço completo/envio real de senha no cadastro.
  - **Aceite:** frontend e backend aceitam/rejeitam os mesmos dados e regras não podem ser contornadas por chamada direta.

- [ ] **BF-06 - Completar seed e configuração de instalação** (bloqueia F-23)
  - Acrescentar Joana/Joaquina e 20+ solicitações com relações/históricos coerentes. Alinhar nome do banco em seed/schema/configuração e documentar inicialização sem apagar dados existentes.
  - Validar SHA-256 + salt, integridade/3FN, desativação, configuração por ambiente e testes do núcleo/backend conforme enunciado.
  - **Aceite:** instalação limpa carrega massa completa e backend passa verificações de build/formatação/testes.

### Ordem sugerida até a entrega

| Etapa | Tasks | Resultado esperado |
| --- | --- | --- |
| 1 | F-01 a F-05; BF-01/BF-05 | Contratos reais, identidade, cadastro e acesso funcionando. |
| 2 | F-06 a F-08 | Fluxo mínimo e CRUDs persistidos. |
| 3 | F-09 a F-13; BF-02/BF-03 | Ciclo completo com histórico e visibilidade corretos. |
| 4 | F-14 a F-16; BF-04 | Receitas e PDFs conferidos com o banco. |
| Contínua | F-17 a F-22; BF-06 | Tratamento de erros, qualidade, testes e massa de dados. |
| Final | F-23 a F-25 | Demonstração reproduzível, Firefox e pacote de entrega. |

### Checklist de aceite da entrega final

**Ajustes de interface implementados em 14/10/2026 (partes de F-09/F-18/F-19):**

- Confirmação de remoção de categoria pelo ConfirmDialog, com botões Remover e Cancelar.
- Descrição do equipamento nas duas listas limitada a 30 caracteres, incluindo reticências.
- Descrições do equipamento, defeito, manutenção e orientações limitadas a 255 caracteres; motivo de rejeição a 500, com ajuda nos campos e validação antes da confirmação.
- Cores RF013 aplicadas aos indicadores de cliente/funcionário, detalhes, histórico, orçamento, manutenção, redirecionamento e gráfico de estados.
- Mensagem de rejeição corrigida para informar a possibilidade de resgate posterior.

Verificações: build de produção aprovado e 21 testes existentes aprovados. Corrigido o provider de autenticação ausente no teste da lista de funcionários. Ainda falta a revisão manual no Firefox.

Os itens maiores continuam pendentes: estes ajustes não concluem integração, máscaras, regras de acesso ou o aceite final no Firefox.

**Ajuste adicional de F-17:** a página inicial do funcionário encerra o carregamento em caso de erro, apresenta uma mensagem acessível e oferece o botão “Tentar novamente”. Uma nova tentativa limpa o erro anterior e mantém o indicador de carregamento até a resposta. A tarefa F-17 completa continua pendente nas demais telas.

- [ ] RF001–RF020 verificados com frontend, backend e banco reais.
- [ ] Nenhum cliente mockado registrado no ambiente final.
- [ ] Perfis, proprietário e funcionário responsável validados também na API.
- [ ] Alterações persistem após refresh/reinício e histórico corresponde às ações executadas.
- [ ] CRUDs desativam com confirmação e preservam vínculos históricos.
- [ ] Relatórios permitem datas opcionais e incluem receitas de categorias desativadas.
- [ ] Massa mínima exigida disponível no banco; roteiro usa dados reproduzíveis.
- [ ] Build/testes frontend e backend aprovados; fluxos validados no Firefox.
- [ ] Produção abre rotas diretamente e acessa a API configurada.
- [ ] README, documento de suposições `.doc/.odt` e roteiro de apresentação entregues.
