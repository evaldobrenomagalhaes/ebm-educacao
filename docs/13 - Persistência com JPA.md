# Documento 13 — Persistência com JPA

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estratégia de persistência adotada pelo Sistema Acadêmico.

A persistência será implementada utilizando **Java Persistence API (JPA)**, tendo o **Hibernate** como provedor de persistência e o **Spring Data JPA** como mecanismo de integração com a aplicação.

O objetivo é manter a camada de persistência desacoplada das regras de negócio, preservando os princípios do Domain-Driven Design (DDD), da Arquitetura Hexagonal e da Clean Architecture.

---

# 2. Motivação

O domínio representa os conceitos do negócio e deve permanecer independente da infraestrutura.

A utilização do JPA permite:

- Persistir objetos do domínio de forma transparente;
- Reduzir código repetitivo;
- Aproveitar recursos do Hibernate;
- Simplificar operações de acesso ao banco de dados.

Neste projeto, optou-se por utilizar as próprias entidades do domínio como entidades JPA, evitando duplicação de modelos e complexidade desnecessária.

Essa abordagem é adequada para sistemas de pequeno e médio porte e é amplamente utilizada em aplicações baseadas em Spring Boot e DDD.

---

# 3. Princípios da Persistência

A persistência seguirá os seguintes princípios.

## 3.1 O domínio continua sendo o centro da aplicação

As entidades do domínio permanecem responsáveis pelas regras de negócio.

As anotações do JPA serão utilizadas apenas como mecanismo de mapeamento para persistência.

---

## 3.2 Persistência é um detalhe técnico

Embora as entidades estejam anotadas com JPA, toda a lógica de persistência continua isolada por meio dos Repositórios.

Os Casos de Uso nunca acessam diretamente APIs do Hibernate ou do EntityManager.

---

## 3.3 Persistência ocorre por meio dos Repositórios

Toda comunicação com o banco ocorrerá através das interfaces de Repository definidas no domínio.

Os Casos de Uso conhecem apenas essas abstrações.

---

# 4. Arquitetura da Persistência

```text
Caso de Uso

↓

Repository (Port)

↓

JpaRepository (Adapter)

↓

Spring Data JPA

↓

Hibernate

↓

Banco de Dados
```

Essa estrutura preserva a independência entre domínio e infraestrutura.

---

# 5. Organização da Camada de Persistência

```text
infrastructure

└── persistence

    ├── repository
    │
    ├── configuration
    │
    └── migration
```

Descrição:

| Diretório | Responsabilidade |
|------------|------------------|
| repository | Implementações dos Repositórios |
| configuration | Configurações do JPA, Hibernate e Flyway |
| migration | (legado conceitual) scripts SQL ficam em `src/main/resources/db/migration` |

As entidades do domínio permanecem na camada **domain**.

Os scripts de migração **Flyway** residem em `src/main/resources/db/migration` (convenção Spring Boot), não como classes Java.

---

# 6. Entidades do Domínio e JPA

As entidades do domínio também serão responsáveis pelo mapeamento JPA.

Exemplo conceitual:

```text
Turma

↓

@Entity

↓

Tabela LY_TURMA
```

Essa decisão elimina duplicidade de modelos e mantém o domínio como representação única dos conceitos do negócio.

As anotações JPA são consideradas detalhes de persistência e não alteram as responsabilidades das entidades.

---

# 7. Persistência dos Value Objects

Os Value Objects serão persistidos utilizando os recursos oferecidos pelo JPA.

Quando apropriado, serão utilizados:

- `@Embeddable`;
- `@Embedded`;
- `@Enumerated`;
- `@AttributeConverter`.

A escolha dependerá das características de cada Value Object.

---

# 8. Fluxo de Persistência

## Gravação

```text
Controller

↓

Caso de Uso

↓

Aggregate Root

↓

Repository

↓

Spring Data JPA

↓

Hibernate

↓

Banco
```

---

## Leitura

```text
Banco

↓

Hibernate

↓

Aggregate Root

↓

Caso de Uso
```

---

# 9. Estratégia de Transações

As operações que modificarem o estado do domínio serão executadas dentro de uma transação.

Fluxo:

```text
Início

↓

Executa Caso de Uso

↓

Executa regras do domínio

↓

Persistência

↓

Commit

↓

Publicação dos Eventos de Domínio
```

Caso ocorra qualquer falha, toda a operação será revertida.

---

# 10. Estratégia de Mapeamento

| Conceito | Estratégia |
|----------|------------|
| Entity | `@Entity` |
| Aggregate Root | Persistido pelo Repository correspondente |
| Value Object | `@Embeddable`, `@Embedded` ou `@AttributeConverter` |
| Enum | `@Enumerated` |
| Domain Event | Não persistido nesta versão |

---

# 11. Estratégia de Identificadores

Cada Aggregate Root possuirá um identificador único.

Exemplos:

- AlunoId
- CursoId
- DisciplinaId
- TurmaId
- MatriculaId

A estratégia de geração dos identificadores será definida na infraestrutura.

---

# 12. Migração do Banco de Dados

A evolução do banco ocorrerá através de **Flyway**, com scripts versionados em `src/main/resources/db/migration`.

As migrações serão responsáveis por:

- criação de tabelas;
- alteração de estruturas;
- criação de índices;
- carga inicial de dados quando necessária.

Configuração alinhada ao MVP:

- `spring.flyway.enabled=true`;
- `spring.jpa.hibernate.ddl-auto=validate` (não usar `update` / `create` em ambientes controlados).

Essa estratégia garante rastreabilidade e evolução segura do esquema do banco.

---

# 13. Decisões de Arquitetura

## DA-011 — Entidades do domínio serão entidades JPA

### Decisão

As próprias entidades do domínio serão anotadas com JPA.

### Justificativa

Evita duplicação de modelos, reduz complexidade e mantém um único modelo representando o domínio.

Essa abordagem é amplamente adotada em aplicações Spring Boot baseadas em DDD.

---

## DA-012 — Persistência ocorre exclusivamente através dos Repositórios

### Decisão

Os Casos de Uso utilizarão apenas os Repositórios definidos no domínio.

### Justificativa

Mantém a separação entre aplicação e infraestrutura.

---

## DA-013 — Não haverá camada de Mapper

### Decisão

Não será criada uma camada específica para conversão entre domínio e persistência.

### Justificativa

O domínio e o modelo de persistência representam os mesmos conceitos neste projeto.

A criação de uma camada adicional aumentaria a complexidade sem trazer benefícios proporcionais.

---

## DA-014 — Aggregate Roots são persistidos integralmente

### Decisão

Os Repositórios recuperarão e persistirão Aggregates completos.

### Justificativa

Preserva as invariantes e a consistência do domínio.

---

## DA-015 — Eventos publicados após Commit

### Decisão

Eventos de Domínio serão publicados apenas após a conclusão bem-sucedida da transação, via `ApplicationEventPublisher` e `@TransactionalEventListener(phase = AFTER_COMMIT)` (ver Documento 08 e ADR-002).

### Justificativa

Evita inconsistências entre estado persistido e eventos publicados.

---

## DA-051 — Versionamento do schema com Flyway

### Decisão

O schema do PostgreSQL será versionado com **Flyway**. Hibernate `ddl-auto` permanece em `validate` no MVP.

### Justificativa

Migrações explícitas, repetíveis e auditáveis; evita drift silencioso do schema gerado automaticamente.

---

# 14. Relação com os Documentos Anteriores

Este documento complementa as decisões definidas anteriormente.

| Documento | Contribuição |
|-----------|--------------|
| Documento 07 | Aggregate Roots |
| Documento 08 | Eventos de Domínio |
| Documento 09 | Casos de Uso |
| Documento 10 | Arquitetura Hexagonal |
| Documento 11 | Repositórios |

---

# 15. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Patterns of Enterprise Application Architecture*
- Gavin King — *Hibernate Documentation*
- Jakarta Persistence Specification (JPA)
- Flyway Documentation
- Documento 22 — ADR-002 (stack e decisões técnicas)

---

# 16. Considerações Finais

A estratégia adotada busca equilibrar simplicidade e boas práticas arquiteturais.

O domínio permanece como o centro da aplicação, enquanto o JPA é utilizado apenas como mecanismo de persistência.

A separação entre domínio e infraestrutura continua sendo garantida pelos Repositórios, Casos de Uso e princípios da Arquitetura Hexagonal, evitando complexidade desnecessária para o escopo do projeto.

---

# 17. Próximos Passos

Este documento servirá como base para:

- Documento 14 — Tratamento de Exceções do Domínio;
- Documento 15 — Estratégia de Testes;
- Documento 16 — Estrutura do Projeto.