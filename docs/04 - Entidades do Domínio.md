# Documento 04 — Entidades do Domínio


> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

## 1. Objetivo

Este documento define as principais entidades do domínio do Sistema Acadêmico, suas responsabilidades, atributos conceituais, relacionamentos e regras de negócio.

Seu objetivo é estabelecer uma base consistente para a implementação orientada ao domínio (DDD), garantindo que cada entidade represente corretamente um conceito do negócio e seja responsável por proteger seu próprio estado e comportamento.

Este documento não aborda detalhes técnicos de persistência, banco de dados, frameworks ou implementação.

---

# 2. Conceito de Entidade

Uma entidade é um objeto do domínio que possui:

- Identidade própria;
- Ciclo de vida;
- Estado que pode sofrer alterações ao longo do tempo;
- Regras de negócio associadas.

A identidade de uma entidade permanece a mesma durante todo o seu ciclo de vida, mesmo que seus atributos sejam alterados.

Exemplo:

Um aluno pode alterar seu nome ou seus dados de contato, porém continua sendo o mesmo aluno dentro do sistema.

---

# 3. Entidades Identificadas

As principais entidades do domínio acadêmico são:

| Entidade | Responsabilidade |
|---|---|
| Aluno | Representar uma pessoa vinculada ao sistema acadêmico |
| Curso | Representar uma formação acadêmica oferecida pela instituição |
| Disciplina | Representar uma unidade curricular pertencente a um curso |
| Período Letivo | Representar um ciclo acadêmico utilizado para organizar as ofertas de turmas |
| Turma | Representar a oferta de uma disciplina em um determinado período letivo |
| Matrícula | Representar o vínculo entre um aluno e uma turma |

---

# 4. Entidade: Aluno

## Definição

Representa uma pessoa cadastrada no sistema acadêmico que pode participar dos processos acadêmicos, como matrícula e acompanhamento de sua trajetória acadêmica.

---

## Responsabilidades

A entidade Aluno é responsável por:

- Manter sua identidade acadêmica;
- Representar sua situação acadêmica;
- Controlar suas informações cadastrais;
- Participar do processo de matrícula.

---

## Identidade

A identidade do aluno deve ser única dentro do sistema.

Exemplo:

```
Aluno #10234
```

---

## Possíveis atributos conceituais

| Atributo | Descrição |
|---|---|
| Identificador | Identidade única do aluno |
| Nome | Nome completo |
| Situação Acadêmica | Estado atual do aluno |

---

## Regras associadas

- Um aluno pode possuir várias matrículas.
- Um aluno deve possuir uma identidade única.
- A situação acadêmica influencia as operações que podem ser realizadas.

---

# 5. Entidade: Curso

## Definição

Representa uma formação acadêmica oferecida pela instituição de ensino.

---

## Responsabilidades

A entidade Curso é responsável por:

- Representar uma formação acadêmica;
- Agrupar as disciplinas pertencentes ao curso;
- Definir a estrutura acadêmica da formação.

---

## Identidade

Cada curso possui identidade própria.

Exemplo:

```
Curso #15
```

---

## Possíveis atributos conceituais

| Atributo | Descrição |
|---|---|
| Identificador | Identidade única do curso |
| Nome | Nome do curso |
| Situação | Estado atual do curso |

---

## Relacionamentos

Um curso possui várias disciplinas.

```text
Curso
   │
   ▼
possui
   │
   ▼
Disciplina
```

---

## Regras associadas

- Um curso pode possuir várias disciplinas.
- Toda disciplina pertence a um único curso.

---

# 6. Entidade: Disciplina

## Definição

Representa uma unidade curricular pertencente a um curso.

Uma disciplina define o conteúdo acadêmico que poderá ser ofertado aos alunos.

---

## Responsabilidades

A entidade Disciplina é responsável por:

- Representar uma unidade curricular;
- Manter sua identidade acadêmica;
- Definir o conteúdo acadêmico que poderá ser ofertado por meio de turmas.

---

## Identidade

Cada disciplina possui identidade própria.

Exemplo:

```
Disciplina #501
```

---

## Possíveis atributos conceituais

| Atributo | Descrição |
|---|---|
| Identificador | Identidade única |
| Nome | Nome da disciplina |
| Código | Código acadêmico |

---

## Relacionamentos

Uma disciplina pode possuir várias turmas.

```text
Disciplina
     │
     ▼
gera ofertas
     │
     ▼
Turma
```

---

## Regras associadas

- Uma disciplina pode ser ofertada várias vezes.
- Cada oferta ocorre por meio de uma turma.

---

# 7. Entidade: Período Letivo

## Definição

Representa um ciclo acadêmico utilizado para organizar as atividades da instituição, especialmente a oferta de turmas.

Exemplos:

- 2026.1
- 2026.2
- 2027.1

---

## Responsabilidades

A entidade Período Letivo é responsável por:

- Representar um ciclo acadêmico;
- Agrupar as turmas ofertadas;
- Definir o contexto temporal das atividades acadêmicas.

---

## Identidade

Cada período letivo possui identidade própria.

Exemplo:

```
Período Letivo #2026.1
```

---

## Possíveis atributos conceituais

| Atributo | Descrição |
|---|---|
| Identificador | Identidade única do período letivo |
| Código | Identificação do período (ex.: 2026.1) |
| Data de Início | Data de início do período |
| Data de Término | Data de encerramento do período |
| Situação | Estado atual do período letivo |

---

## Relacionamentos

Um período letivo pode possuir várias turmas.

```text
Período Letivo
       │
       ▼
    possui
       │
       ▼
     Turma
```

Cada turma pertence a um único período letivo.

---

## Regras associadas

- Um período letivo pode possuir várias turmas.
- Toda turma pertence a um único período letivo.
- Um período letivo possui data de início e data de término.

---

# 8. Entidade: Turma

## Definição

Representa a oferta de uma disciplina em um determinado período letivo.

Uma turma é a execução concreta de uma disciplina para um grupo de alunos.

---

## Responsabilidades

A entidade Turma é responsável por:

- Representar uma oferta acadêmica;
- Controlar a disponibilidade para matrícula;
- Representar a execução de uma disciplina dentro de um período letivo.

---

## Identidade

Cada turma possui identidade própria.

Exemplo:

```
Turma #20261-001
```

---

## Possíveis atributos conceituais

| Atributo | Descrição |
|---|---|
| Identificador | Identidade única da turma |
| Código | Identificação da turma |
| Situação | Estado atual da turma |
| Capacidade Máxima | Número máximo de alunos que a turma pode receber |
| Vagas Disponíveis | Quantidade de vagas ainda não ocupadas na turma |

---

## Relacionamentos

Uma turma pertence a uma disciplina.

```text
Disciplina
     │
     ▼
   possui
     │
     ▼
    Turma
```

Uma turma pertence a um período letivo.

```text
Período Letivo
       │
       ▼
    possui
       │
       ▼
     Turma
```

Uma turma pode possuir várias matrículas.

```text
Turma
   │
   ▼
possui
   │
   ▼
Matrículas
```

---

## Regras associadas

- Toda turma pertence a uma única disciplina.
- Toda turma pertence a um único período letivo.
- Uma turma pode possuir vários alunos matriculados.
- Uma turma representa uma oferta concreta de uma disciplina.
- Capacidade e vagas seguem RN-02/RN-03 (Documento 03) e INV-01/INV-02 (Documento 06): vagas nunca negativas e nunca excedem a capacidade máxima.

---

# 9. Entidade: Matrícula

## Definição

Representa o vínculo acadêmico entre um aluno e uma turma.

A matrícula controla a participação de um aluno em uma determinada oferta de disciplina.

---

## Responsabilidades

A entidade Matrícula é responsável por:

- Representar o vínculo entre aluno e turma;
- Controlar seu próprio estado;
- Aplicar as regras relacionadas à participação acadêmica.

---

## Identidade

Cada matrícula possui identidade própria.

Exemplo:

```
Matrícula #900001
```

---

## Possíveis atributos conceituais

| Atributo | Descrição |
|---|---|
| Identificador | Identidade da matrícula |
| Situação | Estado atual da matrícula |

---

## Relacionamentos

Uma matrícula pertence a:

- Um aluno;
- Uma turma.

```text
Aluno
   │
   ▼
possui
   │
   ▼
Matrícula
   │
   ▼
pertence a
   │
   ▼
Turma
```

---

## Regras associadas

- Toda matrícula pertence a um único aluno.
- Toda matrícula pertence a uma única turma.
- Um aluno não pode possuir duas matrículas na mesma turma.
- Uma matrícula possui um estado durante todo o seu ciclo de vida.

---

# 10. Relacionamento Geral Entre Entidades

Modelo conceitual:

```text
Curso
   │
   ▼
Disciplina
   │
   ▼
Turma
   ▲
   │
Período Letivo

Aluno
   │
   ▼
Matrícula
   │
   ▼
Turma
```

---

# 11. Considerações de Modelagem

## Entidades e Identidade

Todas as entidades possuem identidade própria.

Elas não devem ser tratadas apenas como conjuntos de atributos, mas como objetos responsáveis por manter seu estado consistente e proteger suas regras de negócio.

---

## Responsabilidades

Cada entidade deve proteger suas próprias regras.

Exemplo:

A entidade **Matrícula** deve controlar seu próprio estado, impedindo alterações inválidas em seu ciclo de vida.

A lógica de negócio não deve ficar espalhada pela aplicação, mas concentrada nas próprias entidades do domínio.

---

# 12. Próximos Passos

Este documento servirá como base para os próximos documentos da modelagem:

- Documento 05 — Value Objects;
- Documento 06 — Invariantes do Domínio;
- Documento 07 — Aggregate Roots;
- Documento 08 — Eventos de Domínio.