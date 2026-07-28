# Documento 10 — Arquitetura Hexagonal

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a arquitetura adotada para o Sistema Acadêmico.

A solução será baseada na **Arquitetura Hexagonal (Ports and Adapters)**, utilizando princípios da **Clean Architecture** e do **Domain-Driven Design (DDD)**.

O objetivo é manter o domínio independente de frameworks, banco de dados e tecnologias externas, permitindo maior desacoplamento, facilidade de testes e evolução da aplicação.

## Stack Tecnológica

| Tecnologia | Versão |
|---|---|
| Java | 21 LTS |
| Spring Boot | 3.5.16 |
| Spring Data JPA | via BOM do Spring Boot |
| Hibernate | 6.x (via Spring Boot) |
| Maven | 3.9+ |
| PostgreSQL | 18 |
| Flyway | Versionamento do schema |
| Docker / Docker Compose | Ambiente local |
| Imagem Docker (backend) | `eclipse-temurin:21-jre` (multi-stage Maven) |
| OpenAPI | springdoc-openapi |
| JUnit 5 | Testes |
| Mockito | Testes |
| Testcontainers | Testes de integração |

Esta tabela é um espelho resumido da stack adotada. O `README.md` permanece a fonte de verdade mais detalhada (inclui qualidade, CORS, profiles e credenciais locais). Decisões consolidadas: [Documento 22 — ADR-002](22%20-%20Registro%20de%20Decisões%20Arquiteturais%20(ADR).md).

---

# 2. Motivação

Em muitas aplicações tradicionais, as regras de negócio ficam espalhadas entre Controllers, Services e Repositórios.

Isso gera diversos problemas:

- Alto acoplamento com frameworks;
- Regras duplicadas;
- Dificuldade para testar;
- Baixa reutilização;
- Evolução mais custosa.

A Arquitetura Hexagonal busca resolver esses problemas colocando o domínio no centro da aplicação.

---

# 3. Princípios Arquiteturais

A arquitetura adotada seguirá os seguintes princípios.

## 3.1 O domínio é o centro da aplicação

Toda regra de negócio deve permanecer dentro do domínio.

O domínio não conhece:

- Spring Boot;
- JPA;
- Hibernate;
- Banco de Dados;
- REST;
- Controllers.

O domínio conhece apenas conceitos do negócio.

---

## 3.2 Dependências apontam para o domínio

Todas as demais camadas dependem do domínio.

Nunca o contrário.

```text
Infraestrutura
      │
      ▼
Aplicação
      │
      ▼
Domínio
```

Essa regra reduz o acoplamento e facilita a substituição de tecnologias.

---

## 3.3 Comunicação através de abstrações

A comunicação entre o domínio e a infraestrutura ocorrerá por meio de interfaces (Ports).

As implementações concretas ficarão na camada de infraestrutura (Adapters).

Essa separação permite alterar tecnologias sem modificar o domínio.

---

# 4. Estrutura da Arquitetura

A aplicação será organizada nas seguintes camadas.

```text
                Entrada

      REST Controller
      CLI
      Testes

            │
            ▼

      Casos de Uso

            │
            ▼

         Domínio

            ▲
            │

    Repositórios (Ports)

            ▲
            │

 Implementações JPA
 Eventos
 Banco de Dados
 APIs externas

             Saída
```

---

# 5. Camadas da Aplicação

## 5.1 Domínio

É o núcleo da aplicação.

Contém:

- Entidades;
- Value Objects;
- Aggregate Roots;
- Eventos de Domínio;
- Interfaces de Repositório;
- Regras de Negócio.

O domínio não depende de nenhuma tecnologia.

---

## 5.2 Aplicação

Responsável por executar os Casos de Uso.

Contém:

- Use Cases;
- Commands;
- Responses;
- Orquestração.

A camada de aplicação coordena o domínio, mas não implementa regras de negócio.

---

## 5.3 Infraestrutura

Responsável pelas integrações externas.

Contém:

- Spring Boot;
- JPA;
- Hibernate;
- Controllers;
- Implementações dos Repositórios;
- Banco de Dados;
- Envio de E-mails;
- Publicação de Eventos.

Toda dependência tecnológica fica concentrada nesta camada.

---

# 6. Ports e Adapters

A comunicação entre aplicação e infraestrutura ocorrerá através de **Ports** e **Adapters**.

## Ports

Representam contratos definidos pelo domínio ou pela aplicação.

Exemplo:

```java
public interface TurmaRepository {

    Optional<Turma> buscarPorId(TurmaId id);

    void salvar(Turma turma);

}
```

O domínio conhece apenas essa interface.

---

## Adapters

São implementações concretas dos Ports.

Exemplo:

```java
public class JpaTurmaRepository implements TurmaRepository {

}
```

A implementação utiliza JPA, mas o domínio não conhece essa tecnologia.

---

# 7. Organização dos Pacotes

A estrutura prevista para o projeto será:

```text
br.com.academico

├── domain
│
│   ├── model
│   ├── valueobject
│   ├── event
│   ├── repository
│   └── exception
│
├── application
│
│   ├── usecase
│   ├── command
│   ├── response
│   └── service
│
├── infrastructure
│
│   ├── persistence
│   ├── repository
│   ├── controller
│   ├── configuration
│   ├── event
│   └── integration
│
└── shared
```

Entidades e Aggregate Roots ficam no pacote único `domain/model` (não há subdivisão `entity` / `aggregate`). Essa organização favorece a separação de responsabilidades e a evolução independente das camadas. Detalhamento e justificativa: Documento 16 e ADR-001 (Documento 22).

---

# 8. Fluxo Arquitetural

Exemplo de execução de um Caso de Uso.

```text
Cliente

↓

Controller

↓

ConfirmarMatriculaUseCase

↓

TurmaRepository

↓

JpaTurmaRepository

↓

Banco de Dados

↓

Aggregate Root

↓

Evento de Domínio

↓

Notificação
```

Cada componente possui uma responsabilidade específica.

---

# 9. Relação com os Eventos de Domínio

Após a conclusão de um Caso de Uso, os Eventos de Domínio poderão ser publicados.

Fluxo conceitual:

```text
Caso de Uso

↓

Executa regras do domínio

↓

Persistência

↓

Publica Evento

↓

Consumidores

↓

Auditoria

↓

Notificação

↓

Integrações Futuras
```

Os consumidores permanecem desacoplados da lógica principal da aplicação.

---

# 10. Benefícios da Arquitetura

A adoção da Arquitetura Hexagonal proporciona:

- Baixo acoplamento;
- Alta coesão;
- Facilidade para testes;
- Independência de frameworks;
- Facilidade para trocar banco de dados;
- Evolução gradual da aplicação;
- Melhor organização do código.

---

# 11. Decisões de Arquitetura

## DA-001 — Spring Boot será utilizado apenas na infraestrutura

### Decisão

O domínio e os Casos de Uso não dependerão do Spring Framework.

### Justificativa

O framework deve ser um detalhe de implementação e não influenciar a modelagem do domínio.

---

## DA-002 — Repositórios serão definidos por interfaces

### Decisão

Todos os contratos de persistência serão definidos por interfaces.

### Justificativa

Permite desacoplamento entre domínio e tecnologia de persistência.

---

## DA-003 — Comunicação entre Aggregates ocorrerá por Casos de Uso

### Decisão

Os Casos de Uso serão responsáveis por coordenar operações envolvendo múltiplos Aggregates.

### Justificativa

Preserva a consistência transacional e evita dependências diretas entre Aggregates.

---

## DA-004 — Eventos de Domínio serão utilizados para efeitos secundários

### Decisão

Eventos de Domínio serão publicados apenas após operações concluídas com sucesso.

### Justificativa

Permite adicionar funcionalidades como auditoria, notificações e integrações sem alterar o núcleo do domínio.

---

## DA-005 — Dependências sempre apontam para o domínio

### Decisão

Toda camada externa dependerá do domínio, nunca o contrário.

### Justificativa

Essa regra segue o **Dependency Inversion Principle (DIP)** e os princípios da Arquitetura Hexagonal.

---

# 12. Relação com os Documentos Anteriores

A Arquitetura Hexagonal foi construída sobre as decisões definidas anteriormente.

| Documento | Contribuição |
|-----------|--------------|
| Documento 04 | Entidades do domínio |
| Documento 05 | Value Objects |
| Documento 06 | Invariantes |
| Documento 07 | Aggregate Roots |
| Documento 08 | Eventos de Domínio |
| Documento 09 | Casos de Uso |

Esses documentos definem o comportamento do domínio. A Arquitetura Hexagonal define como esses elementos serão organizados na aplicação.

---

# 13. Referências

Este documento foi elaborado com base nas seguintes obras:

- Alistair Cockburn — *Hexagonal Architecture*
- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Patterns of Enterprise Application Architecture*

---

# 14. Considerações Finais

A Arquitetura Hexagonal permite que o domínio permaneça isolado das tecnologias utilizadas pela aplicação.

Essa abordagem favorece um código mais desacoplado, testável e preparado para evolução, além de facilitar a substituição de frameworks e mecanismos de persistência sem impactar as regras de negócio.

As decisões arquiteturais registradas neste documento servirão como base para a implementação das camadas da aplicação.

---

# 15. Próximos Passos

Este documento servirá como base para:

- Documento 11 — Repositórios (Ports e Adapters);
- Documento 13 — Persistência com JPA;
- Documento 15 — Estratégia de Testes;
- Documento 16 — Estrutura do Projeto.