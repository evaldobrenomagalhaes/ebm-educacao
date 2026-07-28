# Documento 05 — Value Objects

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento identifica os **Value Objects** presentes no domínio do Sistema Acadêmico.

Seu objetivo é distinguir os conceitos que representam apenas um valor daqueles que possuem identidade própria, permitindo uma modelagem mais expressiva, coesa e alinhada aos princípios do Domain-Driven Design (DDD).

Este documento descreve apenas a modelagem conceitual dos Value Objects, sem abordar detalhes de implementação.

---

# 2. Conceito de Value Object

Segundo Eric Evans, um **Value Object** é um objeto do domínio cuja identidade não possui relevância.

Seu significado é determinado exclusivamente pelos valores que representa.

Diferentemente das entidades, dois Value Objects com os mesmos valores são considerados iguais.

Exemplo:

```text
Email("joao@email.com")

=

Email("joao@email.com")
```

Embora sejam objetos diferentes em memória, representam exatamente o mesmo conceito do domínio.

---

# 3. Características dos Value Objects

Os Value Objects adotados neste projeto seguem as seguintes características:

- Não possuem identidade própria;
- São comparados por valor;
- Devem ser imutáveis;
- Representam conceitos do domínio;
- Podem encapsular validações e comportamentos relacionados ao próprio valor.

Essas características tornam o modelo mais expressivo e reduzem inconsistências na aplicação.

---

# 4. Value Objects Identificados

Após a análise do domínio, foram identificados os seguintes candidatos a Value Objects.

| Value Object | Finalidade |
|--------------|------------|
| Email | Representar um endereço eletrônico válido |
| Status da Matrícula | Representar o estado atual de uma matrícula |
| Status da Turma | Representar o estado atual de uma turma |

Outros Value Objects poderão surgir durante a evolução do domínio.

---

# 5. Email

## Definição

Representa o endereço eletrônico de um aluno.

Seu objetivo é garantir que um endereço de e-mail seja sempre tratado como um conceito do domínio, e não apenas como uma sequência de caracteres.

---

## Possíveis atributos

| Atributo | Descrição |
|-----------|-----------|
| Endereço | Valor textual do e-mail |

---

## Regras associadas

- Deve possuir formato válido.
- Não pode ser vazio.
- Deve representar um endereço eletrônico válido.

---

## Igualdade

Dois objetos Email são iguais quando possuem exatamente o mesmo endereço eletrônico.

Exemplo:

```text
Email("ana@email.com")

=

Email("ana@email.com")
```

---

# 6. Status da Matrícula

## Definição

Representa o estado atual de uma matrícula durante seu ciclo de vida.

---

## Valores identificados

- PENDENTE
- CONFIRMADA
- CANCELADA

---

## Observação de Modelagem

Na implementação inicial, este conceito será representado por um **Enum**, por se tratar de um conjunto finito de estados sem comportamento próprio.

Caso o domínio evolua e esse conceito passe a encapsular regras ou comportamentos, poderá ser refatorado para um Value Object sem alterar o modelo conceitual.

---

# 7. Status da Turma

## Definição

Representa a disponibilidade de uma turma para receber novas matrículas.

---

## Valores identificados

- ABERTA
- FECHADA

---

## Observação de Modelagem

Assim como o Status da Matrícula, será implementado inicialmente como um **Enum**, podendo evoluir para um Value Object caso novas regras sejam incorporadas.

---

# 8. Value Objects Considerados e Não Adotados

Durante a modelagem foram analisados outros possíveis candidatos.

## Nome

Embora represente um valor, não possui regras específicas no domínio atual que justifiquem sua modelagem como Value Object.

Será tratado inicialmente como um atributo simples.

---

## Código da Disciplina

Atualmente representa apenas um identificador de negócio.

Não foram identificadas regras suficientes para justificar sua modelagem como Value Object.

---

## Código da Turma

Também será tratado inicialmente como atributo simples.

Caso passe a possuir validações específicas ou comportamento próprio, poderá evoluir para um Value Object.

---

# 9. Benefícios da Utilização de Value Objects

A utilização de Value Objects proporciona diversos benefícios ao modelo do domínio.

- Maior expressividade do código;
- Encapsulamento de validações;
- Redução de duplicação de regras;
- Maior segurança na manipulação dos dados;
- Imutabilidade;
- Facilidade para testes.

Esses benefícios contribuem para um domínio mais consistente e alinhado aos princípios do DDD.

---

# 10. Decisões de Modelagem

## MD-001 — Status serão implementados inicialmente como Enum

### Decisão

Os conceitos **Status da Matrícula** e **Status da Turma** serão implementados inicialmente como **Enums**.

### Justificativa

Os estados identificados representam conjuntos finitos de valores e, neste momento, não possuem comportamento próprio.

A utilização de Enums mantém a implementação simples e adequada ao escopo do projeto.

Caso esses conceitos passem a possuir regras específicas, poderão evoluir naturalmente para Value Objects.

---

## MD-002 — Apenas conceitos com significado próprio serão modelados como Value Objects

### Decisão

Nem todo atributo será transformado em Value Object.

### Justificativa

A modelagem deve refletir a complexidade real do domínio.

Criar Value Objects sem necessidade aumenta a complexidade da solução sem agregar valor ao modelo.

Essa decisão segue o princípio **YAGNI (You Aren't Gonna Need It)**, evitando abstrações prematuras.

---

# 11. Considerações Finais

Os Value Objects identificados neste documento representam conceitos do domínio cujo significado é definido exclusivamente pelos seus valores.

A adoção desse padrão contribui para um modelo mais rico, expressivo e consistente, reduzindo a dispersão de validações e fortalecendo o encapsulamento das regras de negócio.

Novos Value Objects poderão ser identificados conforme o domínio evoluir e novas necessidades surgirem.

---

# 12. Próximos Passos

Este documento servirá de base para os próximos artefatos da modelagem:

- Documento 06 — Invariantes do Domínio;
- Documento 07 — Aggregate Roots;
- Documento 08 — Eventos de Domínio;
- Documento 09 — Casos de Uso.