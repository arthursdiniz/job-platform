# Job Platform — API + frontend Vértice

Plataforma completa de empregos com API Spring Boot e frontend React. O projeto foi construído para portfólio e vai além de um CRUD: inclui autenticação, autorização por perfil, ownership, filtros combináveis, fluxo de candidatura, favoritos, recomendação por regras, dashboards, migrations, testes, documentação interativa e uma interface responsiva em tema claro/escuro.

O frontend está em [`frontend/`](./frontend) e possui seu próprio [guia de execução](./frontend/README.md).

## Principais funcionalidades

- Cadastro e login de candidatos e empresas com JWT e BCrypt
- Perfis `CANDIDATE`, `COMPANY` e `ADMIN` com permissões distintas
- CRUD de vagas com ownership derivado do usuário autenticado
- Busca pública paginada por título, localidade, modalidade, nível, tipo e skill
- Candidatura única por candidato/vaga e transições controladas de status
- Vagas favoritas
- Job Match e recomendações sem APIs ou IA pagas
- Dashboards de candidato e empresa
- Erros e validações padronizados
- Swagger/OpenAPI com autenticação Bearer
- PostgreSQL versionado pelo Flyway
- Execução local ou via Docker Compose
- Frontend React com áreas completas de candidato e empresa

## Tecnologias

Java 21, Spring Boot 3, Spring Web, Spring Data JPA, Spring Security, JWT (JJWT), PostgreSQL, Flyway, Bean Validation, Lombok, Maven, JUnit 5, Mockito, H2 para testes de integração, Testcontainers disponível para evolução da suíte, Springdoc OpenAPI, Docker e Docker Compose.

## Arquitetura

O código usa uma arquitetura em camadas, mantendo transporte, regras de negócio e persistência separados:

```text
HTTP / JSON
    │
    ▼
Controllers ── validação de entrada e códigos HTTP
    │
    ▼
Services ───── regras, autorização de ownership e transações
    │
    ▼
Repositories ─ Spring Data JPA e Specifications
    │
    ▼
PostgreSQL ─── esquema controlado exclusivamente pelo Flyway
```

```text
src/main/java/com/portfolio/jobplatform/
├── config/       segurança, OpenAPI, JPA e seed de desenvolvimento
├── controller/   endpoints REST
├── dto/          contratos de entrada e saída
├── entity/       modelo de domínio persistente
├── exception/    exceções e tratamento global
├── mapper/       conversão de entidades para DTOs
├── repository/   acesso a dados
├── security/     geração e validação de JWT
└── service/      regras de negócio
```

## Modelo de dados

```text
User 1──1 CandidateProfile N──N Skill
  │
  └──1──1 CompanyProfile 1──N Job N──N Skill
                             │
CandidateProfile 1──N Application N──1 Job
CandidateProfile 1──N FavoriteJob N──1 Job
```

O banco também garante unicidade de email, candidatura e favorito, além de validar enums, valores de salário e intervalos. Índices cobrem os campos usados nas pesquisas e relacionamentos principais.

## Execução rápida com Docker

Requisitos: Docker Desktop ou Docker Engine com Compose.

```bash
docker compose up --build
```

A API ficará disponível em `http://localhost:8080` e o Swagger em `http://localhost:8080/swagger-ui.html`.

O perfil `dev` cria dados demonstrativos somente quando o banco está vazio:

| Perfil | Email | Senha |
|---|---|---|
| Candidato | `candidate@example.com` | `Portfolio123!` |
| Empresa | `company@example.com` | `Portfolio123!` |
| Admin | `admin@example.com` | `Portfolio123!` |

Essas credenciais são exclusivamente locais e não são carregadas sem o perfil `dev`.

Para encerrar:

```bash
docker compose down
```

Para apagar também o volume local do PostgreSQL:

```bash
docker compose down -v
```

## Execução pela IDE ou Maven

Requisitos: Java 21+, Maven 3.9+ e PostgreSQL 16+.

Crie o banco e usuário local:

```sql
CREATE USER jobplatform WITH PASSWORD 'jobplatform';
CREATE DATABASE jobplatform OWNER jobplatform;
```

Copie `.env.example` para o mecanismo de variáveis de ambiente da sua IDE e ajuste os valores. Depois execute `JobPlatformApplication` ou:

```bash
mvn spring-boot:run
```

O Flyway cria e atualiza o esquema automaticamente. A aplicação usa `ddl-auto=validate`; Hibernate nunca cria nem altera o banco em produção.

## Variáveis de ambiente

| Variável | Descrição | Exemplo local |
|---|---|---|
| `DATABASE_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/jobplatform` |
| `DATABASE_USERNAME` | Usuário do banco | `jobplatform` |
| `DATABASE_PASSWORD` | Senha do banco | `jobplatform` |
| `JWT_SECRET` | Segredo HMAC com no mínimo 32 bytes | use um valor aleatório em produção |
| `JWT_EXPIRATION_MS` | Validade do token em milissegundos | `3600000` |
| `ALLOWED_ORIGINS` | Origens CORS separadas por vírgula | `http://localhost:3000` |
| `PORT` | Porta HTTP opcional | `8080` |

Nunca versione o arquivo `.env`. Em deploy, configure os valores no gerenciador de secrets da plataforma.

## Autenticação

Cadastro de candidato:

```bash
curl -X POST http://localhost:8080/api/auth/register/candidate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Dev",
    "email": "ana@example.com",
    "password": "Password123!",
    "headline": "Java Backend Developer",
    "experienceLevel": "JUNIOR"
  }'
```

Login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@example.com","password":"Password123!"}'
```

Use o `accessToken` retornado nas rotas protegidas:

```text
Authorization: Bearer <accessToken>
```

No Swagger, clique em **Authorize** e informe o token.

## Endpoints principais

### Autenticação e perfis

| Método | Rota | Acesso |
|---|---|---|
| POST | `/api/auth/register/candidate` | Público |
| POST | `/api/auth/register/company` | Público |
| POST | `/api/auth/login` | Público |
| GET/PUT | `/api/candidates/me/profile` | Candidato |
| GET/PUT | `/api/companies/me/profile` | Empresa |

### Vagas

| Método | Rota | Acesso |
|---|---|---|
| GET | `/api/jobs` | Público |
| GET | `/api/jobs/{id}` | Público |
| POST | `/api/jobs` | Empresa |
| PUT | `/api/jobs/{id}` | Empresa proprietária |
| PATCH | `/api/jobs/{id}/close` | Empresa proprietária |
| DELETE | `/api/jobs/{id}` | Empresa proprietária ou admin |

Exemplo de busca combinada:

```text
GET /api/jobs?skill=Java&experienceLevel=JUNIOR&remote=true&page=0&size=10&sort=createdAt,desc
```

### Candidaturas, favoritos e match

| Método | Rota | Acesso |
|---|---|---|
| POST | `/api/jobs/{jobId}/applications` | Candidato |
| GET | `/api/candidates/me/applications` | Candidato |
| DELETE | `/api/applications/{id}` | Candidato proprietário |
| GET | `/api/jobs/{jobId}/applications` | Empresa proprietária |
| PATCH | `/api/applications/{id}/status` | Empresa proprietária |
| POST/DELETE | `/api/jobs/{jobId}/favorite` | Candidato |
| GET | `/api/candidates/me/favorites` | Candidato |
| GET | `/api/jobs/{jobId}/match` | Candidato |
| GET | `/api/candidates/me/recommended-jobs` | Candidato |
| GET | `/api/candidates/me/dashboard` | Candidato |
| GET | `/api/companies/me/dashboard` | Empresa |
| GET | `/api/companies/me/jobs` | Empresa |

## Job Match

O score é determinístico e explicável:

- 70 pontos: proporção de skills exigidas presentes no perfil
- 15 pontos: compatibilidade do nível de experiência
- 10 pontos: proximidade de cidade, estado ou país
- 5 pontos: vaga remota

O retorno mostra o score, skills encontradas e skills ausentes. Recomendações consideram apenas vagas abertas e não expiradas, ordenadas pelo maior score.

## Regras de segurança

- Senhas são persistidas apenas como hash BCrypt.
- A API não recebe `candidateId` ou `companyId` para operações sensíveis.
- O usuário e o perfil são sempre determinados pelo JWT.
- Services verificam ownership antes de ler ou alterar dados privados.
- Tokens são stateless e sessões de servidor não são criadas.
- CORS é configurável e CSRF é desabilitado somente porque a autenticação usa Bearer token, não cookie de sessão.
- Respostas nunca incluem hash de senha.

## Erros

Erros seguem um formato consistente:

```json
{
  "timestamp": "2026-08-31T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Job not found",
  "path": "/api/jobs/999"
}
```

Erros de validação também incluem `fieldErrors`. A API usa `400`, `401`, `403`, `404`, `409` e `422` conforme a natureza do problema.

## Testes e build

```bash
mvn test
mvn clean package
```

A suíte cobre autenticação HTTP e regras de negócio importantes: candidatura válida, duplicidade, vaga fechada, expiração, ownership de vagas e cálculo do match. O perfil de teste usa H2 em modo PostgreSQL para ser rápido; as dependências do Testcontainers já estão disponíveis para uma suíte PostgreSQL completa em ambientes com Docker.

## Migrations

As migrations estão em `src/main/resources/db/migration`:

1. usuários;
2. perfis de candidato;
3. perfis de empresa;
4. skills;
5. vagas;
6. candidaturas;
7. favoritos;
8. catálogo inicial de skills.

## Deploy

O `Dockerfile` produz uma imagem em dois estágios e roda com usuário sem privilégios. Para publicar em qualquer serviço compatível com containers:

1. provisione um PostgreSQL;
2. configure as variáveis de ambiente;
3. gere uma chave JWT forte;
4. defina as origens CORS do frontend;
5. publique a imagem usando o `Dockerfile`.

O Flyway executará as migrations no início. Não ative o perfil `dev` em produção.

## Roadmap

- Renovação de token e recuperação de senha
- Upload de currículo para storage compatível com S3
- Notificações assíncronas de mudança de status
- Auditoria administrativa e soft delete
- Testes Testcontainers no pipeline de CI
- Métricas, tracing e rate limiting
