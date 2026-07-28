# Documento 06 — Invariantes do Domínio

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento identifica as **invariantes do domínio** do Sistema Acadêmico.

Seu objetivo é definir as regras que **devem permanecer verdadeiras durante todo o ciclo de vida da aplicação**, independentemente da tecnologia utilizada ou da forma como os casos de uso são implementados.

As invariantes representam restrições fundamentais do negócio e servirão de base para a definição dos **Aggregate Roots**, responsáveis por garantir sua consistência.

---

# 2. Conceito de Invariante

No contexto do Domain-Driven Design (DDD), uma **invariante** é uma condição que deve ser preservada antes e após qualquer operação realizada no domínio.

Diferentemente de uma regra de processo ou de um fluxo de negócio, uma invariante representa uma verdade que nunca pode ser violada.

Exemplos:

- Uma turma nunca pode possuir vagas negativas.
- Um aluno não pode estar matriculado duas vezes na mesma turma.
- Uma turma fechada não pode receber novas matrículas.

A preservação dessas regras é essencial para manter a consistência do domínio.

---

# 3. Classificação das Invariantes

Para facilitar sua compreensão e futura implementação, as invariantes foram classificadas em três categorias.

## 3.1 Invariantes de Integridade

Garantem que os dados permaneçam válidos durante todo o ciclo de vida das entidades.

Exemplo:

- Quantidade de vagas nunca pode ser negativa.

---

## 3.2 Invariantes de Consistência

Garantem a coerência entre diferentes conceitos do domínio.

Exemplo:

- Um aluno não pode possuir duas matrículas para a mesma turma.

---

## 3.3 Invariantes de Estado

Garantem que determinadas operações somente possam ocorrer quando o objeto estiver em um estado válido.

Exemplo:

- Uma turma fechada não pode receber novas matrículas.

---

# 4. Invariantes Identificadas

## INV-01 — Quantidade de vagas nunca pode ser negativa

### Descrição

A quantidade de vagas disponíveis em uma turma nunca poderá assumir um valor inferior a zero.

### Justificativa

Não existe cenário de negócio em que uma turma possua menos de zero vagas.

### Tipo

Integridade.

### Origem

Regra de negócio.

### Consequência da violação

Inconsistência na disponibilidade da turma.

---

## INV-02 — Quantidade de vagas disponíveis não pode exceder a capacidade da turma

### Descrição

O número de vagas disponíveis nunca poderá ser maior que a capacidade máxima da turma.

### Justificativa

A disponibilidade sempre deve respeitar o limite físico definido para a turma.

### Tipo

Integridade.

### Origem

Regra de negócio.

### Consequência da violação

Informações incorretas sobre disponibilidade.

---

## INV-03 — Turmas fechadas não aceitam novas matrículas

### Descrição

Uma matrícula somente poderá ser realizada em uma turma aberta.

### Justificativa

Turmas indisponíveis não devem permitir novas inscrições.

### Tipo

Estado.

### Origem

Regra de negócio.

### Consequência da violação

Alunos poderiam ingressar em turmas indisponíveis.

---

## INV-04 — Não pode existir matrícula duplicada

### Descrição

Um mesmo aluno não poderá possuir mais de uma matrícula para a mesma turma.

### Justificativa

O vínculo entre aluno e turma deve ser único.

### Tipo

Consistência.

### Origem

Regra de negócio.

### Consequência da violação

Duplicidade de vínculos acadêmicos.

---

## INV-05 — Cancelamento devolve uma vaga

### Descrição

Sempre que uma matrícula confirmada for cancelada, uma vaga deverá retornar para a turma.

### Justificativa

A capacidade disponível deve refletir corretamente o número de alunos matriculados.

### Tipo

Consistência.

### Origem

Premissa de modelagem adotada durante a análise.

### Consequência da violação

A turma poderá indicar menos vagas do que realmente possui.

---

## INV-06 — Confirmação consome uma vaga

### Descrição

A confirmação de uma matrícula reduz em uma unidade a quantidade de vagas disponíveis da turma.

### Justificativa

Cada matrícula confirmada ocupa uma vaga disponível.

### Tipo

Consistência.

### Origem

Premissa de modelagem adotada durante a análise.

### Consequência da violação

A turma poderá aceitar mais alunos do que sua capacidade.

---

# 5. Responsabilidade pela Proteção

Neste momento da análise, as invariantes foram apenas identificadas.

A definição de quais entidades serão responsáveis por protegê-las será realizada no documento **Aggregate Roots**.

Essa separação mantém o processo de modelagem incremental e alinhado às práticas recomendadas pelo Domain-Driven Design.

---

# 6. Decisões de Modelagem

## MD-003 — As invariantes pertencem ao domínio

### Decisão

As invariantes identificadas não deverão ser implementadas exclusivamente em Controllers, Services, validações de interface ou banco de dados.

### Justificativa

As regras fundamentais do negócio devem ser protegidas pelo próprio modelo de domínio, garantindo sua validade independentemente do meio pelo qual uma operação seja executada.

---

## MD-004 — O banco de dados complementa, mas não substitui o domínio

### Decisão

Restrições de banco de dados (constraints, índices únicos e chaves estrangeiras) poderão reforçar as invariantes, mas não serão consideradas seu mecanismo principal de proteção.

### Justificativa

O domínio deve permanecer consistente mesmo antes da persistência dos dados.

---

# 7. Relação com os Próximos Artefatos

As invariantes identificadas neste documento servirão como entrada para os próximos documentos.

| Documento | Objetivo |
|-----------|----------|
| Documento 07 — Aggregate Roots | Definir quais entidades protegerão as invariantes. |
| Documento 08 — Eventos de Domínio | Identificar eventos gerados após mudanças válidas de estado. |
| Documento 09 — Casos de Uso | Orquestrar as operações respeitando as invariantes do domínio. |

---

# 8. Referências

Este documento foi elaborado com base nas seguintes referências:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Martin Fowler — *Patterns of Enterprise Application Architecture*

---

# 9. Considerações Finais

As invariantes representam as regras fundamentais que garantem a consistência do domínio.

Toda operação realizada no sistema deverá preservar essas regras, independentemente da tecnologia utilizada.

A correta identificação das invariantes permitirá definir Aggregate Roots menores, mais coesos e responsáveis apenas pelas regras que realmente precisam proteger, seguindo as recomendações da literatura de Domain-Driven Design.