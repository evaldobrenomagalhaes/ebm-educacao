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

# 5. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 10 | Arquitetura Hexagonal e organização por camada |
| Documento 16 | Estrutura de pacotes do projeto |
| Documento 20 | Segurança (referência a este ADR) |
| Documento 21 | Plano de evolução e gatilho de reavaliação do ADR-001 |

---

# 6. Considerações Finais

Os ADRs documentam decisões que afetam a estrutura e a evolução do sistema. Novos registros devem ser adicionados quando houver escolha arquitetural relevante, com status explícito (Aceito, Superado, etc.).

---

# 7. Próximos Passos

Este documento complementa a documentação de arquitetura. A visão geral e o índice completo permanecem no `README.md`.
