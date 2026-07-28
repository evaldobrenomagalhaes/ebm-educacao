# Documento 02 - Linguagem Ubíqua

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a **Linguagem Ubíqua (Ubiquitous Language)** utilizada durante todo o desenvolvimento do projeto.

Seu objetivo é garantir que especialistas do domínio, desenvolvedores e documentação utilizem exatamente os mesmos termos para representar os conceitos do negócio.

A utilização de uma linguagem única reduz ambiguidades, melhora a comunicação entre os envolvidos e faz com que o código reflita fielmente o domínio da aplicação.

Este documento não descreve implementações, responsabilidades técnicas ou comportamentos. Seu propósito é apenas estabelecer o vocabulário oficial do domínio.

---

# 2. Referências

Este documento é fundamentado nas seguintes referências:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*

---

# 3. Conceitos do Domínio

## 3.1 Aluno

### Definição

Pessoa apta a participar das atividades acadêmicas e realizar matrículas na instituição.

---

## 3.2 Curso

### Definição

Conjunto organizado de disciplinas oferecido pela instituição.

---

## 3.3 Período Letivo

### Definição

Unidade temporal da organização acadêmica utilizada para agrupar ofertas de disciplinas, matrículas e demais atividades acadêmicas.

---

## 3.4 Disciplina

### Definição

Unidade curricular pertencente a um curso.

Uma disciplina representa o conteúdo acadêmico que poderá ser ofertado em diferentes turmas.

---

## 3.4 Turma

### Definição

Oferta de uma disciplina para um grupo de alunos em um período letivo especifíco.

Uma turma possui capacidade limitada e poderá ou não estar disponível para receber novas matrículas.

---

## 3.5 Matrícula

### Definição

Representa o vínculo entre um aluno e uma turma.

Seu objetivo é registrar a participação do aluno em uma determinada oferta da disciplina.

---

## 3.6 Vaga

### Definição

Capacidade disponível para que novos alunos sejam matriculados em uma turma.

---

# 4. Relacionamentos Conceituais

Os principais relacionamentos identificados no domínio são:

```text
Curso
    │
    └── possui
            │
            ▼
      Disciplina
            │
            └── possui
                    │
                    ▼
                  Turma
                    │
                    └── recebe
                            │
                            ▼
                        Matrícula
                            │
                            └── pertence a
                                    │
                                    ▼
                                  Aluno
```

Este diagrama representa apenas os conceitos do domínio e seus relacionamentos naturais.

Ele não representa decisões de implementação ou modelagem técnica.

---

# 5. Estados do Domínio

## Turma

Uma turma poderá assumir um dos seguintes estados:

- ABERTA
- FECHADA

Esses estados representam sua disponibilidade para receber novas matrículas.

---

## Matrícula

Uma matrícula poderá assumir um dos seguintes estados:

- PENDENTE
- CONFIRMADA
- CANCELADA

Esses estados representam as fases do processo de matrícula.

---

# 6. Glossário do Domínio

| Termo | Definição |
|--------|-----------|
| Aluno | Pessoa apta a realizar matrículas na instituição. |
| Curso | Formação acadêmica composta por disciplinas. |
| Disciplina | Unidade curricular pertencente a um curso. |
| Turma | Oferta de uma disciplina em determinado período letivo. |
| Matrícula | Vínculo entre um aluno e uma turma. |
| Vaga | Capacidade disponível em uma turma. |
| Turma Aberta | Turma disponível para receber novas matrículas. |
| Turma Fechada | Turma indisponível para novas matrículas. |
| Matrícula Pendente | Matrícula criada, mas ainda não confirmada. |
| Matrícula Confirmada | Matrícula efetivada. |
| Matrícula Cancelada | Matrícula encerrada. |

---

# 7. Termos Oficiais

Durante todo o projeto deverão ser utilizados os seguintes termos.

| Utilizar | Evitar |
|-----------|---------|
| Aluno | Estudante* |
| Curso | Formação |
| Disciplina | Matéria |
| Turma | Classe |
| Matrícula | Inscrição |
| Vaga | Assento |
| Confirmada | Efetivada |
| Cancelada | Excluída |

> **Observação:** Caso a instituição utilize oficialmente outra nomenclatura, toda a aplicação deverá adotar esse vocabulário de forma consistente. O princípio da Linguagem Ubíqua não exige um termo específico, mas sim consistência em todo o domínio.

---

# 8. Convenções de Nomenclatura

Todos os elementos da aplicação deverão utilizar os termos definidos neste documento.

Exemplos:

### Classes

```text
Aluno

Curso

Disciplina

Turma

Matricula
```

### Casos de Uso

```text
CadastrarAluno

CadastrarCurso

CadastrarDisciplina

CadastrarTurma

RealizarMatricula

ConfirmarMatricula

CancelarMatricula
```

### Eventos de Domínio

```text
MatriculaRealizada

MatriculaConfirmada

MatriculaCancelada

TurmaAberta

TurmaFechada
```

O evento de criação de matrícula chama-se **MatriculaRealizada** (alinhado ao caso de uso `RealizarMatricula`). Não utilizar o sinônimo `MatriculaCriada`.

Os nomes deverão sempre representar conceitos do domínio e nunca detalhes técnicos.

---

# 9. Evolução da Linguagem

A Linguagem Ubíqua é um artefato vivo.

Sempre que um novo conceito do domínio surgir durante o desenvolvimento, este documento deverá ser atualizado antes da implementação.

Dessa forma, garante-se que documentação, código e domínio permaneçam alinhados durante todo o ciclo de vida do projeto.

---

# 10. Considerações Finais

Este documento define exclusivamente o vocabulário do domínio.

As responsabilidades dos conceitos, seus comportamentos, invariantes, Aggregate Roots e regras de negócio serão detalhados nos documentos posteriores.

A Linguagem Ubíqua servirá como referência para toda a modelagem do sistema, garantindo que a implementação reflita corretamente os conceitos do negócio.