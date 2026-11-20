# Arena Cidadã — Plataforma de Gestão de Espaços Esportivos Públicos

**Global Solution 2026/02 — Microservice and Web Engineering

---

## Integrantes do Grupo


| Nome Completo          | RM         |
|:-----------------------|:-----------|
| Luciano D'Oliveira     | RM558975   |
| Matheus Rocha          | RM559201   |

---

##  O Problema e a Solução

### O Problema
Quadras e campos esportivos públicos de bairro frequentemente enfrentam dois extremos: a **precariedade estrutural** (alambrados rompidos, iluminação queimada e pisos danificados sem canal ágil de manutenção) ou a **monopolização informal** por grupos específicos. Para o cidadão comum, falta transparência sobre a disponibilidade real de horários, condições de uso e um meio justo para agendar partidas sem conflitos de horário (*double-booking*).

### A Solução
O **Arena Cidadã** é uma plataforma *CitizenTech* de gestão compartilhada e controle de acesso que atua em duas frentes integradas:
1. **Zeladoria Comunitária (`ms-quadras`):** Cadastro e gestão do catálogo de espaços esportivos públicos, disponibilidade estrutural e registro/acompanhamento de ocorrências de manutenção reportadas pelos cidadãos.
2. **Agendamento Justo e Acesso (`ms-reservas`):** Gerenciamento do ciclo de vida das reservas de horários e passes de acesso, validando em tempo real a disponibilidade da quadra e sincronizando alterações de status entre os microsserviços.

### Conexão com os ODS (Agenda 2030 — ONU)
* **ODS 3 (Saúde e Bem-Estar):** Incentiva a prática regular de atividades físicas e combate o sedentarismo ao democratizar o acesso a espaços esportivos públicos.
* **ODS 11 (Cidades e Comunidades Sustentáveis):** Promove a gestão participativa e a zeladoria urbana, conectando diretamente o cidadão à conservação dos equipamentos públicos da sua comunidade.

---

## Arquitetura e Tecnologias

A solução foi construída em arquitetura de **microsserviços com comunicação síncrona**, organizada em camadas (`Controller`, `Service`, `Repository`, `Entity` e `DTO`) e bancos de dados relacionais isolados por serviço.

### Stack Tecnológica
* **Linguagem e Framework:** Java 25 e Spring Boot 4.0.9+
* **Service Discovery:** Spring Cloud Netflix Eureka (`eureka-server` na porta `8761`)
* **API Gateway:** Spring Cloud Gateway (`api-gateway` na porta `8082`)
* **Comunicação Síncrona e Balanceamento:** OpenFeign + Spring Cloud LoadBalancer (sem URLs fixas)
* **Tolerância a Falhas:** Resilience4j (Circuit Breaker e Fallback)
* **Persistência e Versionamento de Banco:** MySQL 8.0 (bancos independentes) + Spring Data JPA + Flyway Migrations (`V1` estrutura e `V2` carga inicial)
* **Documentação Interativa:** Springdoc OpenAPI 3 / Swagger UI
* **Containerização:** Docker e Docker Compose

### Visão Geral dos Componentes

| Componente | Responsabilidade | Porta           | Banco de Dados |
| :--- | :--- |:----------------| :--- |
| **`eureka-server`** | Servidor de registro e descoberta de instâncias | `8761` (fixa)   | — |
| **`api-gateway`** | Ponto único de entrada e roteamento dinâmico | `8082` (fixa)   | — |
| **`ms-quadras`** | Gestão de quadras esportivas e ocorrências de zeladoria | Aleatória (`0`) | MySQL (`quadras` — porta `3306`) |
| **`ms-reservas`** | Gestão de reservas e integração síncrona via OpenFeign | Aleatória (`0`) | MySQL (`reservas` — porta `3307`) |

---

## Estrutura do Repositório (Monorepo)

```text
├── api-gateway/          # Roteamento centralizado e resiliência na porta 8082
├── eureka-server/        # Service Registry na porta 8761
├── ms-quadras/           # Microsserviço 1: Quadras e Ocorrências de Zeladoria
├── ms-reservas/          # Microsserviço 2: Reservas e Cliente OpenFeign + Fallback
├── docker-compose.yml    # Orquestração dos bancos MySQL independentes
└── README.md             # Documentação técnica e guia de execução
```

Pré-requisitos
Para executar a solução localmente a partir de um ambiente limpo, certifique-se de ter instalado:

```text
JDK 25 configurado na variável de ambiente JAVA_HOME

Maven 3.9+ (ou utilização do Maven Wrapper ./mvnw incluso nos projetos)

Docker e Docker Compose ativos na máquina
```

Como Executar a Solução (Passo a Passo)
1. Subir os Bancos de Dados MySQL com Docker
Na raiz do repositório (onde está localizado o arquivo docker-compose.yml), execute o comando abaixo para iniciar os dois containers MySQL independentes (mysql-quadras na porta 3306 e mysql-reservas na porta 3307):

```Bash
docker compose up -d
Verifique se ambos os containers estão em execução:
```
Bash
docker ps
Nota: Não é necessário criar tabelas ou inserir dados manualmente. Ao iniciar cada microsserviço, o Flyway executará automaticamente as migrations V1 (criação das tabelas) e V2 (carga inicial de registros).

2. Iniciar o Eureka Server (Porta 8761)
Em um terminal, acesse a pasta do eureka-server e inicie a aplicação:

```Bash
cd eureka-server
./mvnw spring-boot:run
```
Aguarde a inicialização e confirme o painel no navegador: http://localhost:8761

3. Iniciar o API Gateway (Porta 8082)
Em um segundo terminal, acesse a pasta do api-gateway e inicie a aplicação:

```Bash
cd api-gateway
./mvnw spring-boot:run
```
4. Iniciar os Microsserviços (ms-quadras e ms-reservas)
Cada microsserviço está configurado com server.port=0, subindo em portas dinâmicas e registrando-se automaticamente no Eureka Server (permitindo inclusive subir múltiplas instâncias simultâneas para validação de Load Balancer).

Terminal 3 — Iniciar ms-quadras:

```Bash
cd ms-quadras
./mvnw spring-boot:run
```
Terminal 4 — Iniciar ms-reservas:

```Bash
cd ms-reservas
./mvnw spring-boot:run
```
Execução via IntelliJ IDEA: Caso prefira rodar pela IDE, abra a raiz do repositório, aguarde o carregamento dos projetos Maven e execute as classes principais (*Application.java) exatamente nesta ordem: 1º EurekaServerApplication, 2º ApiGatewayApplication, 3º MsQuadrasApplication e 4º MsReservasApplication.

Documentação OpenAPI / Swagger e Endpoints (v1)
Após subir todos os serviços, acesse http://localhost:8761 para visualizar as portas dinâmicas atribuídas a MS-QUADRAS e MS-RESERVAS.

Eureka Dashboard: http://localhost:8761

Swagger UI (ms-quadras): http://localhost:<PORTA_MS_QUADRAS>/swagger-ui/index.html

Swagger UI (ms-reservas): http://localhost:<PORTA_MS_RESERVAS>/swagger-ui/index.html

*IMPORTANTE*: É preciso inserir manualmente alguma ocorrência, tente:
{
  "titulo": "Refletor queimado na lateral norte",
  "descricao": "Dois refletores da lateral norte da quadra estão queimados, prejudicando a visibilidade nos jogos noturnos.",
  "fotoUrl": "https://arenacidada.com.br/fotos/ocorrencia-refletor-01.jpg",
  "solicitanteNome": "Lucas Almeida Santos",
  "quadraId": 1
}

Principais Endpoints Versionados (/api/v1)
ms-quadras (Direto ou via Gateway em http://localhost:8082/ms-quadras/api/v1/...)
GET /api/v1/quadras — Lista todas as quadras cadastradas

GET /api/v1/quadras/{id} — Busca uma quadra por ID

POST /api/v1/quadras — Cadastra uma nova quadra (201 Created + header Location)

PUT /api/v1/quadras/{id} — Atualiza os dados de uma quadra

DELETE /api/v1/quadras/{id} — Remove uma quadra (204 No Content)

PATCH /api/v1/quadras/{id}/disponibilizar — Altera o status da quadra para DISPONIVEL

PATCH /api/v1/quadras/{id}/manutencao — Altera o status da quadra para EM_MANUTENCAO

PATCH /api/v1/quadras/{id}/interditar — Altera o status da quadra para INTERDITADA

GET /api/v1/ocorrencias — Lista todas as ocorrências de zeladoria

GET /api/v1/ocorrencias/{id} — Busca ocorrência por ID

POST /api/v1/ocorrencias — Abre uma nova ocorrência de zeladoria vinculada a uma quadra

PUT /api/v1/ocorrencias/{id} — Atualiza uma ocorrência

DELETE /api/v1/ocorrencias/{id} — Exclui uma ocorrência

PATCH /api/v1/ocorrencias/{id}/analise — Altera o status da ocorrência para EM_ANALISE

PATCH /api/v1/ocorrencias/{id}/resolver — Conclui a ocorrência (RESOLVIDA)

ms-reservas (Direto ou via Gateway em http://localhost:8082/ms-reservas/api/v1/...)
GET /api/v1/reservas — Lista todas as reservas

GET /api/v1/reservas/{id} — Busca uma reserva por ID

POST /api/v1/reservas — Cria uma reserva validando síncronamente a quadra no ms-quadras via OpenFeign

PUT /api/v1/reservas/{id} — Atualiza uma reserva confirmada

DELETE /api/v1/reservas/{id} — Exclui uma reserva (204 No Content)

PATCH /api/v1/reservas/{id}/... — Operações de transição de status integradas entre os microsserviços

Roteiro de Validação e Testes
Verificação de Registro e Balanceamento:

Acesse http://localhost:8761 e confirme que API-GATEWAY, MS-QUADRAS e MS-RESERVAS constam com status UP.

Opcionalmente, inicie uma segunda instância de ms-quadras para comprovar o registro múltiplo em portas aleatórias e o balanceamento pelo Load Balancer.

Teste de CRUD e Migrations Flyway:

Execute GET http://localhost:8082/ms-quadras/api/v1/quadras e GET http://localhost:8082/ms-reservas/api/v1/reservas para confirmar o retorno 200 OK com os dados iniciais carregados pelo Flyway (V2).

Teste consultas por ID inexistente (ex.: /999) para validar o tratamento customizado de exceções (404 Not Found).

Teste de Comunicação Síncrona (OpenFeign) e Alteração de Status:

Realize uma requisição POST para criar uma reserva vinculada a uma quadra existente (DISPONIVEL) e confirme a integração entre os serviços.

Altere o status da quadra para INTERDITADA ou EM_MANUTENCAO no ms-quadras e tente realizar uma nova reserva para ela no ms-reservas, verificando o bloqueio pela regra de negócio (400 Bad Request).

Teste de Resiliência (Circuit Breaker e Fallback):

Interrompa temporariamente as instâncias do ms-quadras e acione o endpoint de criação/consulta integrada no ms-reservas para validar o acionamento do Circuit Breaker e a resposta controlada do método de Fallback.
