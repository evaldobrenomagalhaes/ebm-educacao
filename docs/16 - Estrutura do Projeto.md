# Documento 16 — Estrutura do Projeto

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a organização estrutural do Sistema Acadêmico.

Seu objetivo é estabelecer uma estrutura de pacotes consistente, baseada nos princípios do Domain-Driven Design (DDD), da Arquitetura Hexagonal e da Clean Architecture.

A estrutura proposta busca facilitar a manutenção, a evolução e a compreensão da aplicação, mantendo uma clara separação de responsabilidades entre as camadas.

---

# 2. Princípios

A organização do projeto seguirá os seguintes princípios.

- Separação entre Domínio, Aplicação e Infraestrutura;
- Baixo acoplamento entre camadas;
- Alta coesão entre componentes relacionados;
- Dependências apontando para o domínio;
- Organização por responsabilidade e não por tecnologia.

---

# 3. Estrutura Geral

Na raiz do repositório, o ambiente local é orquestrado pelo Docker Compose (MVP). A aplicação Java permanece sob `src/`.

```text
.
├── docker-compose.yml
├── Dockerfile              # multi-stage Maven → eclipse-temurin:21-jre
├── frontend/               # módulo frontend (a especificar) + Dockerfile; porta 4200
├── docs/
└── src
    ├── main
    │
    │   ├── java
    │   │
    │   │   └── br.com.academico
    │   │
    │   │       ├── domain
    │   │       ├── application
    │   │       ├── infrastructure
    │   │       └── shared
    │   │
    │   └── resources
    │       ├── application.yml
    │       ├── application-dev.yml
    │       ├── application-test.yml
    │       ├── application-prod.yml
    │       └── db/migration/      # scripts Flyway
    │
    └── test
```

Serviços previstos no Compose: `db` (PostgreSQL **18**), `backend` (Spring Boot **3.5.16**) e `frontend` (porta **4200**). O comando padrão de execução local é `docker compose up`.

Credenciais locais do banco: database `ebm-edu`, usuário `admin`, senha `admin@123` (via Compose / `.env`; em `prod` preferir variáveis de ambiente).

---

# 4. Camada de Domínio

A camada de domínio representa o coração da aplicação.

Ela contém exclusivamente regras de negócio.

```text
domain

├── model
│
├── repository
│
├── event
│
├── exception
│
├── service
│
└── valueobject
```

## Responsabilidades

- Entidades;
- Aggregate Roots;
- Value Objects;
- Domain Services;
- Eventos;
- Interfaces de Repository;
- Exceções do domínio.

---

# 5. Camada de Aplicação

Responsável por coordenar os Casos de Uso.

```text
application

├── usecase
│
├── command
│
├── query
│
└── dto
```

Não há pacote `mapper` na camada de aplicação. Conforme DA-013 e DA-011 (Documento 13), as entidades do domínio são as próprias entidades JPA; não será criada uma camada específica de conversão entre domínio e persistência.

## Responsabilidades

- Casos de Uso;
- Commands;
- Queries;
- DTOs;
- Orquestração da aplicação.

A camada de aplicação não implementa regras de negócio.

Ela coordena o domínio.

---

# 6. Camada de Infraestrutura

Responsável pelos detalhes técnicos.

```text
infrastructure

├── persistence
│
├── web
│
├── configuration
│
├── event
│
└── security
```

---

## Persistence

```text
persistence

├── repository
└── configuration
```

Responsável pela persistência dos dados. Scripts **Flyway** ficam em `src/main/resources/db/migration`.

---

## Web

```text
web

├── controller
│
├── request
│
├── response
│
└── exception
```

Responsável pela API REST (inclui `@RestControllerAdvice` com `ProblemDetail` e configuração de CORS no MVP).

---

## Configuration

```text
configuration
```

Responsável pelas configurações do Spring (`application.yml`, profiles `dev` / `test` / `prod`, CORS, springdoc).

---

## Event

```text
event
```

Responsável pelos listeners dos Domain Events (`@TransactionalEventListener(AFTER_COMMIT)`).

---

## Security

```text
security
```

Reservado para evolução futura.

---

# 7. Shared

Componentes reutilizáveis.

```text
shared

├── util
│
├── validation
│
└── configuration
```

A utilização dessa camada deve ocorrer apenas quando realmente necessário.

Ela não deve se tornar um repositório de código genérico.

---

# 8. Estrutura Completa

```text
br.com.academico

├── domain
│   ├── model
│   ├── repository
│   ├── valueobject
│   ├── event
│   ├── service
│   └── exception
│
├── application
│   ├── usecase
│   ├── command
│   ├── query
│   └── dto
│
├── infrastructure
│   ├── persistence
│   │   ├── repository
│   │   └── configuration
│   │
│   ├── web
│   │   ├── controller
│   │   ├── request
│   │   ├── response
│   │   └── exception
│   │
│   ├── configuration
│   ├── security
│   └── event
│
└── shared
    ├── util
    ├── validation
    └── configuration
```

Recursos Spring (`src/main/resources`): `application.yml`, `application-{dev|test|prod}.yml` e `db/migration/` (Flyway).

---

# 9. Dependências Entre Camadas

A direção das dependências seguirá o princípio da inversão de dependência.

```text
Infrastructure

↓

Application

↓

Domain
```

O domínio não depende de nenhuma outra camada.

---

# 10. Regras de Organização

As seguintes regras deverão ser respeitadas.

## Domínio

Não conhece:

- Spring Boot;
- JPA;
- Controllers;
- HTTP;
- JSON.

---

## Aplicação

Conhece:

- Domínio;
- Repositórios.

Não conhece:

- Banco de Dados;
- Controllers;
- Hibernate.

---

## Infraestrutura

Conhece:

- Spring;
- Banco;
- REST;
- Configurações.

Implementa os contratos definidos pelo domínio.

---

# 11. Decisões de Arquitetura

## DA-025 — Organização por camadas

### Decisão

A aplicação será organizada em Domain, Application e Infrastructure.

### Justificativa

Facilita a separação de responsabilidades e reduz o acoplamento.

---

## DA-026 — O domínio é independente

### Decisão

O domínio não dependerá de frameworks ou bibliotecas de infraestrutura.

### Justificativa

Preserva as regras de negócio e facilita testes.

---

## DA-027 — Infraestrutura implementa as Ports

### Decisão

Os contratos definidos pelo domínio serão implementados exclusivamente na infraestrutura.

### Justificativa

Segue os princípios da Arquitetura Hexagonal.

---

## DA-028 — Organização por responsabilidade

### Decisão

Os pacotes serão organizados conforme responsabilidades e não por tecnologia.

### Justificativa

Melhora a coesão e facilita a manutenção.

---

# 12. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 09 | Casos de Uso |
| Documento 10 | Arquitetura Hexagonal |
| Documento 11 | Repositórios |
| Documento 12 | Persistência |
| Documento 13 | Tratamento de Exceções |
| Documento 14 | Estratégia de Testes |

---

# 13. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Alistair Cockburn — *Hexagonal Architecture*
- Martin Fowler — *Patterns of Enterprise Application Architecture*

---

# 14. Considerações Finais

A estrutura proposta busca equilibrar simplicidade e organização, permitindo que o domínio permaneça independente das tecnologias utilizadas.

A separação entre Domain, Application e Infrastructure favorece a manutenção, os testes automatizados e a evolução da aplicação ao longo do tempo.

Essa organização servirá como base para toda a implementação do projeto.

---

# 15. Próximos Passos

Com este documento, conclui-se a fase de Arquitetura.

Os próximos documentos abordarão aspectos relacionados à engenharia de software e à sustentação do projeto:

- Documento 17 — Pipeline CI/CD;
- Documento 18 — Qualidade de Código;
- Documento 19 — Convenções do Projeto;
- Documento 20 — Segurança;
- Documento 21 — Plano de Evolução do Sistema;
- `README.md` — visão geral e entrega final da documentação do projeto.