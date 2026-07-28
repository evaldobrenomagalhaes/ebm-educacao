# Documento 03 - Modelo do Domínio

> Versão: 1.0
>
> Fase: Análise
>
> Status: Em construção

---

# 1. Objetivo

Este documento descreve o comportamento do domínio sob a perspectiva do negócio.

Seu objetivo é identificar como os conceitos do domínio interagem, quais responsabilidades cada um possui e quais regras de negócio governam essas interações.

Neste momento ainda não são consideradas decisões de implementação, tecnologias ou padrões arquiteturais.

O foco está exclusivamente na compreensão do funcionamento do domínio.

---

# 2. Visão Geral do Domínio

O principal objetivo do sistema é gerenciar o processo de matrícula de alunos em turmas.

Para que isso seja possível, o domínio é composto pelos seguintes conceitos:

- Aluno
- Curso
- Disciplina
- Período Letivo
- Turma
- Matrícula

Esses conceitos se relacionam para permitir que um aluno seja matriculado em uma turma respeitando as regras estabelecidas pela instituição.

---

# 3. Fluxos do Domínio

## 3.1 Fluxo — Realizar Matrícula

```text
Aluno

↓

seleciona uma turma

↓

é criada uma matrícula

↓

a matrícula inicia com status PENDENTE
```

---

## 3.2 Fluxo — Confirmar Matrícula

```text
Existe vaga disponível?

↓

Sim

↓

a matrícula é confirmada

↓

a turma reduz a quantidade de vagas disponíveis

↓

fim
```

Caso não existam vagas disponíveis, a matrícula não poderá ser confirmada.

---

## 3.3 Fluxo — Cancelar Matrícula

```text
Solicitação de cancelamento

↓

a matrícula é cancelada

↓

se estava confirmada

↓

a turma libera uma vaga

↓

fim
```

---

## 3.4 Fluxo — Consultar Matrículas por Aluno

```text
Informar o aluno

↓

o domínio retorna as matrículas vinculadas ao aluno

↓

fim
```

Essa consulta é requisito do domínio (RN-09) e faz parte do escopo da versão 1.0.

---

## 3.5 Fluxo — Consultar Matrículas por Turma

```text
Informar a turma

↓

o domínio retorna as matrículas vinculadas à turma

↓

fim
```

Essa consulta é requisito do domínio (RN-10) e faz parte do escopo da versão 1.0.

---

# 4. Responsabilidades do Domínio

## Aluno

Responsável por representar o estudante durante o processo de matrícula.

---

## Curso

Responsável por representar uma formação composta por disciplinas.

---

## Disciplina

Responsável por representar uma unidade curricular pertencente a um curso.

## Período Letivo

Responsável por representar uma unidade temporal da organização acadêmica utilizada para agrupar ofertas de disciplinas, matrículas e demais atividades acadêmicas.

---

## Turma

Responsável por:

- controlar sua capacidade;
- controlar vagas disponíveis;
- controlar sua disponibilidade para matrícula.

---

## Matrícula

Responsável por:

- representar o vínculo entre aluno e turma;
- controlar seu próprio estado;
- registrar a participação do aluno em uma turma.

---

# 5. Regras de Negócio

As seguintes regras foram identificadas durante a análise do domínio.

### RN-01

Uma matrícula somente poderá ser confirmada caso existam vagas disponíveis na turma.

---

### RN-02

Uma turma não poderá possuir quantidade de vagas negativas.

---

### RN-03

Uma turma não poderá exceder sua capacidade máxima.

---

### RN-04

Somente turmas abertas poderão receber novas matrículas.

---

### RN-05

Toda matrícula inicia com o status **PENDENTE**.

---

### RN-06

Ao confirmar uma matrícula, uma vaga deverá ser consumida.

---

### RN-07

Ao cancelar uma matrícula confirmada, uma vaga deverá ser liberada.

---

### RN-08

Um aluno não poderá possuir duas matrículas para a mesma turma.

---

### RN-09

O sistema deverá permitir consultar as matrículas de um aluno.

---

### RN-10

O sistema deverá permitir consultar as matrículas de uma turma.

---

# 6. Estados do Domínio

## Ciclo de Vida da Turma

```text
ABERTA
   │
   ▼
FECHADA
```

---

## Ciclo de Vida da Matrícula

```text
PENDENTE
     │
     ▼
CONFIRMADA
     │
     ▼
CANCELADA
```

> **Observação:** O desafio não especifica se uma matrícula cancelada pode retornar ao estado pendente ou ser reconfirmada. Até que esse requisito seja definido, considera-se o cancelamento como um estado final.

---

# 7. Invariantes Identificadas

As seguintes invariantes foram identificadas durante a modelagem do domínio.

## INV-01

A quantidade de vagas disponíveis nunca poderá ser negativa.

---

## INV-02

A quantidade de vagas disponíveis nunca poderá ser maior que a capacidade máxima da turma.

---

## INV-03

Uma turma fechada não poderá receber novas matrículas.

---

## INV-04

Não poderá existir mais de uma matrícula para o mesmo aluno na mesma turma.

---

## INV-05

O consumo de vagas somente poderá ocorrer durante a confirmação de uma matrícula.

---

## INV-06

A liberação de vagas somente poderá ocorrer quando uma matrícula confirmada for cancelada.

---

# 8. Premissas Adotadas

Durante a análise foram identificados requisitos que não estão explicitamente definidos no desafio.

Para permitir a continuidade da modelagem, foram adotadas as seguintes premissas:

### PA-01

Toda matrícula será criada inicialmente com o status **PENDENTE**.

---

### PA-02

A confirmação da matrícula é responsável por consumir uma vaga da turma.

---

### PA-03

O cancelamento de uma matrícula confirmada devolve uma vaga para a turma.

---

### PA-04

O cancelamento é considerado um estado final para a matrícula.

Essas premissas poderão ser revisadas caso novos requisitos sejam definidos.

---

# 9. Pontos de Atenção

Os seguintes aspectos deverão ser validados durante a evolução do projeto:

- O processo de confirmação da matrícula será manual ou automático?
- Uma matrícula cancelada poderá ser reativada?
- Existe prazo para cancelamento de matrícula?
- Turmas fechadas poderão ser reabertas?
- Existe limite de matrículas por aluno em disciplinas diferentes?

As consultas de matrículas por aluno e por turma **não** são pontos em aberto: são requisitos do domínio (RN-09 e RN-10) e fazem parte do MVP.

Essas questões não impactam a implementação inicial, mas poderão influenciar futuras evoluções do domínio.

---

# 10. Próximos Artefatos

Este documento servirá de base para a elaboração dos seguintes artefatos:

- Documento 04 — Entidades
- Documento 05 — Value Objects
- Documento 06 — Invariantes
- Documento 07 — Aggregate Roots
- Documento 08 — Eventos de Domínio
- Documento 09 — Casos de Uso

---

# 11. Considerações Finais

O Modelo do Domínio representa a compreensão atual do funcionamento do negócio.

As responsabilidades, regras de negócio, estados e invariantes aqui identificados servirão como fundamento para toda a modelagem do sistema.

Nenhuma decisão de implementação deverá contrariar as regras estabelecidas neste documento.