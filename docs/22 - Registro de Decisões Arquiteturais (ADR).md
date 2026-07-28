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
- Frontend (framework) permanece **a especificar**; a porta 4200 já está fixada.

---

# 6. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 08 | Publicação de eventos (MD-011 / mecanismo Spring) |
| Documento 10 | Arquitetura Hexagonal e stack |
| Documento 13 | Persistência JPA e Flyway (DA-051) |
| Documento 14 | Exceções e ProblemDetail (DA-018) |
| Documento 16 | Estrutura do projeto e resources |
| Documento 19 | Convenções, logging e profiles |
| Documento 20 | Segurança, Bean Validation e CORS |
| Documento 21 | Plano de evolução e gatilho de reavaliação do ADR-001 |

---

# 7. Considerações Finais

Os ADRs documentam decisões que afetam a estrutura e a evolução do sistema. Novos registros devem ser adicionados quando houver escolha arquitetural relevante, com status explícito (Aceito, Superado, etc.).

---

# 8. Próximos Passos

Este documento complementa a documentação de arquitetura. A visão geral e o índice completo permanecem no `README.md`.
