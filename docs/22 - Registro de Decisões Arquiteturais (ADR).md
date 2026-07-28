# Documento 22 — Registro de Decisões Arquiteturais (ADR)

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento registra as **Decisões Arquiteturais (Architecture Decision Records — ADR)** relevantes do Sistema Acadêmico.

Cada ADR documenta o contexto, a decisão tomada, as alternativas consideradas e as consequências, permitindo rastrear o “porquê” das escolhas ao longo da evolução do projeto.

As Decisões de Arquitetura (DA-*) espalhadas pelos demais documentos permanecem válidas; este ADR complementa decisões de maior impacto estrutural ou que possam ser reavaliadas no futuro.

---

# 2. Formato

Cada registro segue a estrutura:

| Seção | Conteúdo |
|-------|----------|
| Contexto | Situação e problema que motivam a decisão |
| Decisão | Escolha adotada |
| Alternativas consideradas | Opções descartadas ou adiadas |
| Consequências | Impactos imediatos e gatilhos de reavaliação |

---

# 3. Índice de ADRs

| ADR | Título | Status |
|-----|--------|--------|
| ADR-001 | Organização de Pacotes: Camada vs. Módulo de Negócio | Aceito |
| ADR-002 | Stack e Decisões Técnicas do MVP | Aceito |
| ADR-003 | `@Transactional` nos Casos de Uso (MVP) | Aceito |

---

# 4. ADR-001 — Organização de Pacotes: Camada vs. Módulo de Negócio

**Status:** Aceito

**Data:** 2026-07-28

## Contexto

Em projetos baseados em Domain-Driven Design (DDD), os pacotes podem ser organizados de formas distintas:

- **Por camada** (`domain` / `application` / `infrastructure`), com o domínio em um único pacote `model`;
- **Por módulo de negócio** (feature), por exemplo `aluno`, `turma`, `matricula`, cada um com suas subcamadas;
- **Por subdomínio dentro do domínio**, por exemplo `domain/aluno`, `domain/turma`, `domain/matricula`.

A estrutura do projeto já está documentada nos Documentos 10 (Arquitetura Hexagonal) e 16 (Estrutura do Projeto). É necessário registrar formalmente a escolha e o critério de reavaliação futura.

## Decisão

Manter a **organização por camada** para este desafio, incluindo, no domínio, um único pacote `model` (sem subdivisão por subdomínio ou módulo de negócio).

Pacotes de alto nível:

```text
br.com.academico
├── domain
│   ├── model
│   ├── valueobject
│   ├── event
│   ├── repository
│   ├── service
│   └── exception
├── application
├── infrastructure
└── shared
```

## Alternativas consideradas

1. **Organização por módulo/feature** (`br.com.academico.aluno.domain`, `.turma.domain`, etc.) — recomendada por Vaughn Vernon (*Implementing Domain-Driven Design*) para domínios que crescem e precisam de limites mais explícitos entre contextos.
2. **Domínio dividido por subdomínio** (`domain/aluno`, `domain/turma`, `domain/matricula`) — agrupa conceitos por área de negócio, mantendo application/infrastructure por camada.

## Justificativa da escolha atual

Para o escopo do desafio, a organização por camada torna os conceitos de DDD e de Arquitetura Hexagonal mais explícitos e fáceis de avaliar: o avaliador enxerga com clareza o núcleo de domínio, a orquestração da aplicação e os adapters de infraestrutura.

## Consequências

- A estrutura atual permanece alinhada aos Documentos 10 e 16.
- O pacote `domain/model` concentra entidades e Aggregate Roots; não há `domain/entity` nem `domain/aggregate` separados.
- Se o domínio crescer (novos módulos como Notas e Frequência — Documento 21, §4.2), a migração para organização por módulo de negócio **deve ser reavaliada**. Esse gatilho está registrado no Documento 21.

---

# 5. ADR-002 — Stack e Decisões Técnicas do MVP

**Status:** Aceito

**Data:** 2026-07-28

## Contexto

A documentação descrevia a stack em termos amplos (`Spring Boot 3.x`, PostgreSQL sem versão, OpenAPI sem biblioteca, migrações sem ferramenta). Para implementação e ambiente Docker reproduzível, era necessário fechar versões e mecanismos transversais (schema, eventos, erros, CORS, logging, config).

## Decisão

| Item | Escolha |
|------|---------|
| Spring Boot | **3.5.16** (última estável da linha 3.x alinhada aos docs) |
| PostgreSQL | **18** (`postgres:18`) |
| OpenAPI | **springdoc-openapi** (`springdoc-openapi-starter-webmvc-ui`); UI em `/swagger-ui.html` → `/swagger-ui/index.html`; spec `/v3/api-docs` |
| Schema | **Flyway** (`src/main/resources/db/migration`); `ddl-auto=validate` |
| Validação de entrada | **Jakarta Bean Validation** |
| Domain Events | `ApplicationEventPublisher` + `@TransactionalEventListener(AFTER_COMMIT)` |
| Erros HTTP | **RFC 7807 `ProblemDetail`** |
| CORS (MVP) | Origin `http://localhost:4200` liberada |
| Docker backend | Multi-stage Maven → **`eclipse-temurin:21-jre`** |
| Logging | **SLF4J** (Logback via Spring Boot) |
| Configuração | `application.yml` + profiles **`dev`**, **`test`**, **`prod`** |
| Porta frontend | **4200** |
| Banco local | database `ebm-edu`, user `admin`, password `admin@123` |

## Alternativas consideradas

1. **Spring Boot 4.1.x** — estável mais recente absoluta; descartada por enquanto para manter coerência com a documentação “Spring Boot 3” e reduzir risco de migração no desafio.
2. **Liquibase** ou `ddl-auto=update` — Flyway escolhido por scripts SQL versionados e integração direta com Spring Boot; `update` rejeitado no MVP.
3. **springfox** — incompatível / obsoleto frente ao Spring Boot 3; springdoc é o padrão atual.
4. **Envelope de erro proprietário** — `ProblemDetail` padroniza o contrato e é nativo no Spring 6 / Boot 3.

## Consequências

- README e Documentos 08, 10, 13, 14, 16, 19 e 20 devem refletir este ADR.
- Credenciais locais são apenas para Compose/`dev`; `prod` usa variáveis de ambiente.
- Nome do banco com hífen (`ebm-edu`) exige aspas em SQL cru.
- Porta frontend **4200** fixada; framework detalhado no **ADR-004** e Documento 23.

---

# 6. ADR-003 — `@Transactional` nos Casos de Uso (MVP)

**Status:** Aceito

**Data:** 2026-07-28

## Contexto

A MD-013 (Documento 09) estabelece que os Casos de Uso não devem depender diretamente do Spring Framework, preservando a camada de aplicação independente da tecnologia. No MVP Spring Boot, no entanto, o gerenciamento de transações via `@Transactional` nos use cases é o padrão idiomático e já está aplicado de forma consistente na implementação.

Remover `@Transactional` da aplicação exigiria reescrever dezenas de use cases (boundary transacional em adapter, aspect externo ou wrapper), sem ganho funcional para o escopo do desafio.

## Decisão

No **MVP**, os Casos de Uso **podem** usar `@Transactional` (Spring) para delimitar a unidade de trabalho.

Restrições que permanecem:

- O **domínio** (`domain/`) continua **livre de Spring** (sem anotações ou APIs do framework).
- Regras de negócio e invariantes permanecem nas entidades / Aggregate Roots (MD-014).
- Persistência continua via ports do domínio (MD-015); `@Transactional` apenas orquestra a transação.

Esta decisão é uma **exceção pragmática** à letra estrita da MD-013, limitada ao acoplamento transacional na camada de aplicação.

## Alternativas consideradas

1. **Remover `@Transactional` dos use cases** e empurrar a boundary para adapters/controllers ou um decorator — alinhamento estrito à MD-013; custo alto (rewrite de ~33 use cases) sem benefício no MVP.
2. **Programmatic `TransactionTemplate`** injetado — ainda acopla a Spring e aumenta verbosidade.
3. **Manter `@Transactional` nos use cases** — adotada para o MVP.

## Consequências

- Testes unitários de use cases que exercitam persistência real ou proxies Spring precisam do contexto transacional (ou mocks das ports).
- A camada de aplicação fica acoplada a Spring Transaction apenas no MVP; isso **deve ser reavaliado** se surgirem adapters ou runtimes **não-Spring** (CLI, worker sem Boot, outro framework).
- Documentos 09 (MD-013) e 10 devem ser lidos em conjunto com este ADR.

---

# 7. ADR-004 — Arquitetura do Frontend (Angular)

**Status:** Aceito

**Data:** 2026-07-28

## Contexto

O Compose e o ADR-002 já fixavam a porta do frontend (`4200`) e o CORS correspondente, mas o framework e a organização da SPA permaneciam “a especificar”. Era necessário fechar a stack da UI para os Documentos 23–29 e a implementação do módulo `frontend/`.

## Decisão

| Item | Escolha |
|------|---------|
| Framework | **Angular** (TypeScript; versão estável na implementação) |
| Organização | Por **feature** de domínio + `core/` + `shared/` + `layout/` |
| HTTP | **HttpClient** + services manuais por feature |
| Estado | **Local** por tela/feature (sem NgRx no MVP) |
| Contrato | OpenAPI/Swagger como consulta; **sem** codegen de cliente no MVP |
| Entrega | Build estático + **nginx** no Compose (`4200:80`) |

A UI não duplica regras de domínio; autenticação permanece fora do MVP (Documento 20).

## Alternativas consideradas

1. **React + Vite** ou **Vue + Vite** — viáveis; descartadas no MVP por maior carga de escolha de libs (router, forms, HTTP).
2. **Organização só por tipo técnico** (`components/`, `services/`) — rejeitada por dispersar o domínio.
3. **Cliente gerado a partir do OpenAPI** — adiado ao roadmap (Documento 29).
4. **Store global (NgRx)** — desnecessário sem auth nem estado compartilhado complexo.

## Consequências

- Documento 23 é a fonte de verdade da arquitetura do frontend.
- README, ADR-002 (consequência de framework) e o índice em `docs/README.md` devem refletir Angular.
- Documentos 24–29 detalham navegação, UX, erros, API, pastas e testes/roadmap.

---

# 8. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 08 | Publicação de eventos (MD-011 / mecanismo Spring) |
| Documento 09 | Casos de uso e MD-013 (exceção pragmática via ADR-003) |
| Documento 10 | Arquitetura Hexagonal e stack |
| Documento 13 | Persistência JPA e Flyway (DA-051) |
| Documento 14 | Exceções e ProblemDetail (DA-018) |
| Documento 16 | Estrutura do projeto e resources |
| Documento 19 | Convenções, logging e profiles |
| Documento 20 | Segurança, Bean Validation e CORS |
| Documento 21 | Plano de evolução e gatilho de reavaliação do ADR-001 |
| Documento 23 | Arquitetura do frontend (ADR-004) |

---

# 9. Considerações Finais

Os ADRs documentam decisões que afetam a estrutura e a evolução do sistema. Novos registros devem ser adicionados quando houver escolha arquitetural relevante, com status explícito (Aceito, Superado, etc.).

---

# 10. Próximos Passos

Este documento complementa a documentação de arquitetura. A visão geral e o índice completo permanecem no `README.md`.
