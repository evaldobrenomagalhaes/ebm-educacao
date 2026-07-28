# Documento 07 — Aggregate Roots

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento identifica os **Aggregates** e seus respectivos **Aggregate Roots** dentro do domínio do Sistema Acadêmico.

Seu objetivo é definir os limites de consistência do domínio, indicando quais entidades são responsáveis por proteger as invariantes identificadas anteriormente e garantir que o estado do domínio permaneça sempre consistente.

Este documento não aborda detalhes de persistência, banco de dados ou implementação utilizando frameworks.

---

# 2. Conceito de Aggregate

Segundo Eric Evans, um **Aggregate** é um conjunto de objetos do domínio tratados como uma única unidade de consistência.

Todas as alterações realizadas em um Aggregate devem preservar suas invariantes.

Um Aggregate estabelece um limite claro para:

- Consistência dos dados;
- Regras de negócio;
- Controle transacional;
- Responsabilidades do domínio.

Cada Aggregate possui exatamente um **Aggregate Root**.

---

# 3. Conceito de Aggregate Root

O **Aggregate Root** é a única entidade do Aggregate que pode ser acessada diretamente por outros objetos do domínio.

Todas as modificações nos objetos internos do Aggregate devem ocorrer por intermédio do Aggregate Root.

Essa abordagem evita alterações inconsistentes e garante que todas as invariantes sejam preservadas.

---

# 4. Aggregates Identificados

Após a análise do domínio, foram identificados os seguintes Aggregates.

| Aggregate | Aggregate Root | Objetivo |
|------------|----------------|----------|
| Turma | Turma | Controlar a oferta da disciplina e suas vagas |
| Matrícula | Matrícula | Controlar o vínculo entre aluno e turma |

Esses Aggregates representam limites naturais de consistência dentro do domínio.

---

# 5. Aggregate: Turma

## Objetivo

Representar uma oferta de disciplina e controlar sua disponibilidade para matrícula.

---

## Aggregate Root

```text
Turma
```

---

## Objetos relacionados

A Turma mantém referências para:

- Disciplina;
- Período Letivo;
- Matrículas (por referência ou relacionamento conceitual).

Esses objetos não pertencem necessariamente ao mesmo Aggregate.

---

## Responsabilidades

O Aggregate Turma é responsável por:

- Controlar a capacidade da turma;
- Controlar vagas disponíveis;
- Controlar sua disponibilidade;
- Garantir que apenas turmas abertas recebam matrículas.

---

## Invariantes protegidas

- INV-01 — Vagas nunca podem ser negativas;
- INV-02 — Vagas nunca podem ultrapassar a capacidade;
- INV-03 — Turmas fechadas não recebem matrículas.

---

## Operações do domínio

Durante a implementação, espera-se que o Aggregate Root ofereça operações como:

- abrir();
- fechar();
- consumirVaga();
- liberarVaga();

Essas operações representam comportamentos do domínio e são responsáveis por preservar as invariantes da Turma.

---

# 6. Aggregate: Matrícula

## Objetivo

Representar o vínculo acadêmico entre um aluno e uma turma.

---

## Aggregate Root

```text
Matrícula
```

---

## Objetos relacionados

A Matrícula mantém referências para:

- Aluno;
- Turma.

Essas referências representam relacionamentos entre Aggregates e não implicam dependência de ciclo de vida.

---

## Responsabilidades

O Aggregate Matrícula é responsável por:

- Controlar seu estado;
- Garantir transições válidas de estado;
- Representar o vínculo acadêmico entre aluno e turma.

---

## Invariantes protegidas

- O estado da matrícula deve permanecer válido durante todo seu ciclo de vida.

---

## Operações do domínio

Espera-se que a entidade ofereça operações como:

- confirmar();
- cancelar();

Essas operações deverão preservar o ciclo de vida válido da matrícula.

---

# 7. Relação entre Aggregates

Os Aggregates se relacionam apenas por referências de identidade.

Modelo conceitual:

```text
Aluno
   │
   ▼
Matrícula
   │
   ▼
Turma
   ▲
   │
Disciplina
```

Cada Aggregate mantém sua própria consistência e não modifica diretamente o estado interno de outro Aggregate.

Essa separação reduz o acoplamento e favorece a evolução independente dos conceitos do domínio.

---

# 8. Limites de Consistência

Cada Aggregate é responsável exclusivamente pelas regras que pertencem ao seu contexto.

| Aggregate | Responsável por |
|------------|-----------------|
| Turma | Vagas, disponibilidade e capacidade |
| Matrícula | Estado da matrícula |

Nenhum Aggregate deve alterar diretamente o estado interno de outro Aggregate.

Quando uma alteração em um Aggregate exigir uma reação em outro, essa comunicação deverá ocorrer por meio de **Eventos de Domínio**, preservando o baixo acoplamento.

---

# 9. Limites Transacionais

As operações realizadas dentro de um Aggregate devem ocorrer de forma consistente.

Exemplos:

### Dentro do Aggregate Turma

- Consumir vaga;
- Liberar vaga;
- Alterar disponibilidade.

---

### Dentro do Aggregate Matrícula

- Confirmar matrícula;
- Cancelar matrícula.

Essas operações devem preservar todas as invariantes do Aggregate antes da conclusão da transação.

---

# 10. Decisões de Modelagem

## MD-005 — Turma será um Aggregate Root

### Decisão

A entidade **Turma** será o Aggregate Root responsável pelas regras relacionadas à oferta da disciplina.

### Justificativa

As invariantes relacionadas à disponibilidade e ao controle de vagas pertencem naturalmente à Turma.

---

## MD-006 — Matrícula será um Aggregate Root

### Decisão

A entidade **Matrícula** será modelada como um Aggregate Root independente.

### Justificativa

A matrícula possui identidade própria, ciclo de vida próprio e regras específicas relacionadas ao seu estado.

Sua separação reduz o acoplamento entre os conceitos do domínio e facilita sua evolução futura.

---

## MD-007 — Aggregates serão referenciados por identidade

### Decisão

Os Aggregates manterão referências entre si utilizando apenas suas identidades.

### Justificativa

Essa abordagem reduz dependências entre Aggregates, evita carregamentos desnecessários e segue as recomendações de Eric Evans e Vaughn Vernon para modelagem de Aggregates.

---

## MD-008 — Comunicação entre Aggregates ocorrerá por Eventos de Domínio

### Decisão

Quando uma alteração em um Aggregate exigir ações em outro contexto, essa comunicação ocorrerá por meio de Eventos de Domínio.

### Justificativa

Essa estratégia reduz o acoplamento entre Aggregates e favorece uma arquitetura orientada ao domínio e preparada para evolução.

---

# 11. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Martin Fowler — *Patterns of Enterprise Application Architecture*

---

# 12. Considerações Finais

Os Aggregates definidos neste documento estabelecem os limites de consistência do domínio e determinam quais entidades são responsáveis por proteger suas invariantes.

Essa organização contribui para um modelo mais coeso, desacoplado e alinhado às práticas recomendadas pelo Domain-Driven Design.

Os próximos documentos utilizarão esses Aggregates como base para a identificação dos Eventos de Domínio e para a definição dos Casos de Uso da aplicação.

---

# 13. Próximos Passos

Este documento servirá de base para:

- Documento 08 — Eventos de Domínio;
- Documento 09 — Casos de Uso;
- Documento 10 — Arquitetura Hexagonal.