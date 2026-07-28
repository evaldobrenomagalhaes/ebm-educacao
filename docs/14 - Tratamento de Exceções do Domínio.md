# Documento 14 — Tratamento de Exceções do Domínio

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estratégia para tratamento de exceções no Sistema Acadêmico.

O objetivo é estabelecer uma forma consistente de representar situações excepcionais do domínio, mantendo a separação entre regras de negócio, aplicação e infraestrutura.

A estratégia adotada segue os princípios do Domain-Driven Design (DDD), da Clean Architecture e da Arquitetura Hexagonal.

---

# 2. Motivação

Durante a execução dos Casos de Uso podem ocorrer situações que impedem a continuidade da operação.

Exemplos:

- matrícula duplicada;
- turma encerrada;
- ausência de vagas;
- aluno inexistente;
- período letivo encerrado.

Essas situações representam regras do negócio e devem ser tratadas como exceções do domínio.

---

# 3. Princípios

O tratamento de exceções seguirá os seguintes princípios.

## 3.1 O domínio define seus próprios erros

O domínio não utilizará exceções genéricas para representar violações de regras de negócio.

Cada exceção deverá possuir significado para o domínio.

---

## 3.2 Exceções representam situações excepcionais

Exceções serão utilizadas apenas quando uma operação não puder continuar.

Validações simples de entrada não fazem parte dessa categoria e deverão ser tratadas na camada de aplicação.

---

## 3.3 O domínio desconhece HTTP

O domínio não conhece:

- HTTP;
- códigos de resposta;
- JSON;
- REST;
- Spring MVC.

Ele apenas comunica que determinada regra foi violada.

---

# 4. Organização das Exceções

As exceções do domínio serão organizadas conforme a estrutura abaixo.

```text
domain

└── exception

    ├── DomainException
    ├── EntityNotFoundException
    ├── BusinessRuleViolationException
    ├── DuplicateMatriculaException
    ├── TurmaEncerradaException
    ├── SemVagasException
    └── PeriodoLetivoEncerradoException
```

---

# 5. Hierarquia

```text
RuntimeException
        │
        ▼
DomainException
        │
        ├───────────────┐
        │               │
        ▼               ▼
BusinessRule      EntityNotFound
Violation
        │
        ▼
Exceções específicas
```

Essa organização facilita o tratamento uniforme das exceções.

---

# 6. Responsabilidade das Camadas

## Domínio

Responsável por:

- identificar violações das regras de negócio;
- lançar exceções específicas.

Não conhece infraestrutura.

---

## Aplicação

Responsável por:

- coordenar os Casos de Uso;
- propagar exceções do domínio.

---

## Infraestrutura

Responsável por:

- registrar logs;
- traduzir exceções para respostas HTTP;
- tratar detalhes técnicos.

---

# 7. Fluxo de Tratamento

```text
Controller

↓

Caso de Uso

↓

Aggregate Root

↓

Violação de Regra

↓

DomainException

↓

Global Exception Handler

↓

HTTP Response
```

O domínio permanece desacoplado do protocolo de comunicação.

---

# 8. Exceções Identificadas

As seguintes exceções fazem parte do domínio.

| Exceção | Situação |
|----------|----------|
| EntityNotFoundException | Entidade inexistente |
| DuplicateMatriculaException | Matrícula duplicada |
| SemVagasException | Turma sem vagas |
| TurmaEncerradaException | Matrícula em turma encerrada |
| PeriodoLetivoEncerradoException | Período letivo encerrado |
| BusinessRuleViolationException | Violação genérica de regra de negócio |

Outras exceções poderão ser adicionadas conforme o domínio evoluir.

---

# 9. Tratamento na API

A camada de apresentação será responsável por converter exceções do domínio em respostas apropriadas.

Exemplo conceitual:

| Exceção | Resposta HTTP |
|----------|---------------|
| EntityNotFoundException | 404 Not Found |
| DuplicateMatriculaException | 409 Conflict |
| SemVagasException | 409 Conflict |
| BusinessRuleViolationException | 422 Unprocessable Entity |
| IllegalArgumentException | 400 Bad Request |
| Exception | 500 Internal Server Error |

Essa conversão será realizada por um componente específico da infraestrutura.

---

# 10. Estratégia de Logging

As exceções serão registradas de acordo com sua natureza.

## Erros de negócio

Registro informativo.

Exemplo:

```text
Aluno tentou realizar matrícula em turma sem vagas.
```

---

## Erros técnicos

Registro completo contendo:

- mensagem;
- stack trace;
- contexto da operação.

Esses registros auxiliam na investigação de falhas.

---

# 11. Decisões de Arquitetura

## DA-016 — Exceções específicas do domínio

### Decisão

Cada violação relevante de regra de negócio possuirá sua própria exceção.

### Justificativa

Melhora a legibilidade e facilita o tratamento pela aplicação.

---

## DA-017 — O domínio não conhece HTTP

### Decisão

As exceções do domínio não conterão informações relacionadas ao protocolo HTTP.

### Justificativa

Preserva a independência do domínio.

---

## DA-018 — Conversão para HTTP ocorre na infraestrutura

### Decisão

A tradução entre exceções e respostas HTTP ocorrerá exclusivamente na camada de infraestrutura.

### Justificativa

Mantém separadas as responsabilidades entre domínio e apresentação.

---

## DA-019 — Exceções representam regras violadas

### Decisão

As exceções serão utilizadas apenas para representar situações que impedem a continuidade da operação.

### Justificativa

Evita o uso inadequado de exceções para controle de fluxo.

---

# 12. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 07 | Invariantes protegidas pelas entidades |
| Documento 09 | Casos de Uso |
| Documento 10 | Arquitetura Hexagonal |
| Documento 11 | Repositórios |
| Documento 12 | Persistência |

---

# 13. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Patterns of Enterprise Application Architecture*

---

# 14. Considerações Finais

O tratamento de exceções adotado mantém o domínio independente de tecnologias de apresentação e infraestrutura.

As regras de negócio são comunicadas por meio de exceções específicas, enquanto a tradução para respostas HTTP ocorre exclusivamente na camada de infraestrutura.

Essa abordagem favorece a manutenção, os testes e a evolução da aplicação.

---

# 15. Próximos Passos

Este documento servirá como base para:

- Documento 15 — Estratégia de Testes;
- Documento 16 — Estrutura do Projeto.