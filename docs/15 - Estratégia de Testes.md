# Documento 15 — Estratégia de Testes

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estratégia de testes adotada pelo Sistema Acadêmico.

Seu objetivo é garantir que as regras de negócio, os Casos de Uso e a integração entre os componentes sejam validados de forma automatizada, aumentando a confiabilidade da aplicação e reduzindo a probabilidade de regressões.

A estratégia segue os princípios do Domain-Driven Design (DDD), da Clean Architecture e das boas práticas de testes automatizados.

---

# 2. Objetivos da Estratégia

Os testes deverão garantir:

- Correção das regras de negócio;
- Preservação das invariantes do domínio;
- Funcionamento dos Casos de Uso;
- Integração entre aplicação e infraestrutura;
- Segurança para futuras refatorações.

---

# 3. Pirâmide de Testes

A estratégia seguirá a Pirâmide de Testes.

```text
               Testes E2E
                   ▲
                   │
          Testes de Integração
                   ▲
                   │
            Testes Unitários
```

A maior parte dos testes será composta por testes unitários.

---

# 4. Tipos de Testes

## 4.1 Testes Unitários

Validam classes isoladamente.

Características:

- rápidos;
- independentes;
- sem banco de dados;
- sem Spring Boot;
- sem infraestrutura.

Serão utilizados principalmente para testar:

- Entities;
- Value Objects;
- Domain Services;
- Casos de Uso.

---

## 4.2 Testes de Integração

Validam a integração entre componentes.

Exemplos:

- Repository + Banco;
- Spring Boot + JPA;
- Persistência.

Esses testes utilizarão banco de dados real em ambiente isolado.

---

## 4.3 Testes End-to-End

Não fazem parte da primeira versão do projeto.

Poderão ser adicionados futuramente para validar o fluxo completo da aplicação.

---

# 5. Ferramentas

| Ferramenta | Finalidade |
|------------|------------|
| JUnit 5 | Execução dos testes |
| Mockito | Simulação de dependências |
| Testcontainers | Banco de dados para integração |
| Spring Boot Test | Inicialização do contexto |
| JaCoCo | Cobertura de código |

---

# 6. Estratégia para o Domínio

As regras de negócio serão testadas diretamente nas entidades.

Exemplo:

```text
Turma

↓

confirmarMatricula()

↓

Verificar:

• vagas
• status
• invariantes
```

Os testes do domínio não dependerão do Spring.

---

# 7. Estratégia para os Casos de Uso

Os Casos de Uso serão testados isoladamente.

As dependências serão simuladas utilizando Mockito.

Fluxo:

```text
Use Case

↓

Repository (Mock)

↓

Validação do comportamento
```

O objetivo é validar exclusivamente a lógica da aplicação.

---

# 8. Estratégia para os Repositórios

Os Repositórios serão testados através de testes de integração.

Fluxo:

```text
Repository

↓

Spring Data

↓

Banco de Dados

↓

Validação da Persistência
```

Não serão utilizados mocks nessa camada.

---

# 9. Testcontainers

Os testes de integração utilizarão Testcontainers.

Fluxo:

```text
JUnit

↓

Testcontainers

↓

Banco de Dados

↓

Repository
```

Essa abordagem garante que os testes utilizem um banco real, isolado e reproduzível.

---

# 10. Cobertura de Código

A cobertura será monitorada utilizando JaCoCo.

Objetivos iniciais:

| Camada | Cobertura mínima |
|----------|----------------:|
| Domínio | 90% |
| Casos de Uso | 85% |
| Infraestrutura | Conforme necessidade |

A cobertura não substitui a qualidade dos testes.

Ela será utilizada apenas como indicador.

---

# 11. Organização dos Testes

Estrutura prevista.

```text
src

├── main
│
└── test

    ├── domain
    │
    ├── application
    │
    ├── infrastructure
    │
    └── integration
```

Os testes seguirão a mesma organização da aplicação.

---

# 12. Princípios

Os testes deverão seguir os seguintes princípios.

## Independência

Um teste não depende de outro.

---

## Legibilidade

Os testes devem ser simples de compreender.

---

## Determinismo

Um teste deve produzir sempre o mesmo resultado.

---

## Rapidez

A maior parte da suíte deverá executar em poucos segundos.

---

## Isolamento

Cada teste valida apenas um comportamento.

---

# 13. Decisões de Arquitetura

## DA-020 — Domínio será testado isoladamente

### Decisão

As entidades e Value Objects serão testados sem dependência do Spring.

### Justificativa

As regras de negócio pertencem ao domínio.

---

## DA-021 — Casos de Uso utilizarão Mockito

### Decisão

Os Repositórios serão simulados nos testes dos Casos de Uso.

### Justificativa

Permite validar apenas a lógica da aplicação.

---

## DA-022 — Repositórios serão testados com banco real

### Decisão

A persistência será validada através de Testcontainers.

### Justificativa

Aumenta a confiabilidade dos testes de integração.

---

## DA-023 — Cobertura será monitorada por JaCoCo

### Decisão

A cobertura de código será acompanhada continuamente.

### Justificativa

Auxilia na identificação de áreas pouco testadas.

---

## DA-024 — Testes seguirão a Pirâmide de Testes

### Decisão

A maior parte da suíte será composta por testes unitários.

### Justificativa

Reduz tempo de execução e custo de manutenção.

---

# 14. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 04 | Entidades |
| Documento 05 | Value Objects |
| Documento 06 | Aggregate Roots |
| Documento 07 | Invariantes |
| Documento 09 | Casos de Uso |
| Documento 11 | Repositórios |
| Documento 12 | Persistência |
| Documento 13 | Tratamento de Exceções |

---

# 15. Referências

Este documento foi elaborado com base nas seguintes obras:

- Kent Beck — *Test Driven Development: By Example*
- Gerard Meszaros — *xUnit Test Patterns*
- Robert C. Martin — *Clean Architecture*
- Eric Evans — *Domain-Driven Design*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Martin Fowler — *Patterns of Enterprise Application Architecture*

---

# 16. Considerações Finais

A estratégia de testes busca garantir que o domínio permaneça confiável e evolua com segurança.

A separação entre testes unitários e de integração reduz o tempo de execução da suíte, facilita a manutenção e aumenta a confiança durante refatorações.

Os testes serão tratados como parte integrante da arquitetura da aplicação.

---

# 17. Próximos Passos

Este documento servirá como base para:

- Documento 16 — Estrutura do Projeto;
- Documento 17 — Pipeline CI/CD;
- Documento 18 — Qualidade de Código.