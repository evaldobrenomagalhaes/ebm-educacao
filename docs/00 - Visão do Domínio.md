# Documento 00 - Visão do Domínio

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento tem como objetivo compreender o problema de negócio antes da definição da arquitetura ou da implementação da solução.

Seguindo os princípios do Domain-Driven Design (DDD), as decisões técnicas serão tomadas somente após a compreensão do domínio, da linguagem utilizada pelo negócio e das responsabilidades de cada conceito envolvido.

O domínio será a principal fonte de decisões arquiteturais deste projeto.

---

# 2. Escopo do Sistema

O sistema tem como finalidade realizar o gerenciamento acadêmico de uma instituição de ensino.

Embora existam funcionalidades administrativas como o CRUD de alunos, cursos, disciplinas, períodos letivos e turmas, o principal processo do domínio é o gerenciamento de matrículas (realizar, confirmar, cancelar e consultar).

Todas as demais funcionalidades existem para dar suporte a esse fluxo principal.

---

# 3. Objetivos do Sistema

O sistema deverá permitir:

- cadastrar, atualizar, listar (com filtros avançados), buscar e excluir alunos;
- cadastrar, atualizar, listar (com filtros avançados), buscar e excluir cursos;
- cadastrar, atualizar, listar (com filtros avançados), buscar e excluir disciplinas;
- cadastrar, atualizar, listar (com filtros avançados), buscar e excluir turmas;
- cadastrar, atualizar, listar (com filtros avançados), buscar e excluir períodos letivos;
- consultar turmas disponíveis;
- realizar matrículas;
- confirmar matrículas;
- cancelar matrículas;
- consultar matrículas por aluno (com filtros avançados);
- consultar matrículas por turma (com filtros avançados).

---

# 4. Referências Bibliográficas

As decisões arquiteturais e de modelagem serão fundamentadas em literatura consolidada.

| Referência | Aplicação |
|------------|-----------|
| Eric Evans – Domain-Driven Design | Modelagem do domínio, Linguagem Ubíqua, Entidades, Value Objects, Aggregates e Domain Services |
| Vaughn Vernon – Implementing Domain-Driven Design | Modelagem de Aggregates e limites do domínio |
| Robert C. Martin – Clean Architecture | Organização da arquitetura e separação de responsabilidades |
| Martin Fowler – Patterns of Enterprise Application Architecture | Repository, DTO, Mapper e padrões de persistência |
| Joshua Bloch – Effective Java | Imutabilidade e boas práticas de modelagem |
| Documentação Oficial do Spring | Implementação da infraestrutura técnica |

---

# 5. Linguagem Ubíqua

Durante todo o desenvolvimento será utilizada uma linguagem única entre documentação e código.

Os nomes utilizados nas classes, métodos, casos de uso e regras de negócio deverão refletir exatamente os conceitos do domínio.

Glossário inicial:

| Termo | Definição |
|--------|-----------|
| Aluno | Pessoa apta a realizar matrículas. |
| Curso | Formação oferecida pela instituição. |
| Disciplina | Unidade curricular pertencente a um curso. |
| Período Letivo | Unidade temporal da organização acadêmica (ex.: 2026.1), usada para agrupar ofertas de turmas e matrículas. Extensão além do mínimo do desafio técnico, mantida no modelo do projeto. |
| Turma | Oferta de uma disciplina em um determinado período letivo, com capacidade limitada de vagas. |
| Matrícula | Relação entre um aluno e uma turma. |
| Vaga | Capacidade disponível de uma turma. |
| Matrícula Confirmada | Matrícula que consome uma vaga da turma. |
| Matrícula Cancelada | Matrícula que devolve uma vaga para a turma. |
| Turma Aberta | Turma disponível para novas matrículas. |
| Turma Fechada | Turma indisponível para novas matrículas. |

Este glossário poderá evoluir conforme novas regras de negócio forem descobertas.

Observação:
Este glossário representa uma visão inicial do domínio e será refinado durante a fase de modelagem. Novos conceitos poderão ser adicionados e definições existentes poderão evoluir conforme o entendimento do negócio amadurecer.

---

# 6. Responsabilidades do Domínio

Nesta etapa buscamos identificar qual conceito do domínio é responsável por cada regra de negócio.

O objetivo não é definir como essas responsabilidades serão implementadas, mas compreender a quem cada responsabilidade pertence dentro do domínio.

Essas definições servirão de base para a modelagem das entidades, invariantes e agregados nas próximas fases.

| Questão | Resposta | Justificativa |
|----------|----------|---------------|
| Qual conceito do domínio controla as vagas disponíveis? | **Turma** | As vagas fazem parte do estado da Turma. É responsabilidade da própria Turma garantir que sua capacidade nunca seja excedida. |
| Qual conceito do domínio representa o vínculo entre um aluno e uma turma? | **Matrícula** | A Matrícula representa o relacionamento entre Aluno e Turma, bem como seu estado durante o processo acadêmico. |
| Qual conceito do domínio controla o estado de uma turma? | **Turma** | O estado da Turma (aberta ou fechada) faz parte de seu ciclo de vida e deve ser controlado pela própria entidade. |
| Qual conceito do domínio garante que um aluno não seja matriculado duas vezes na mesma turma? | **O domínio** | Essa é uma regra de negócio (invariante) que deve ser preservada independentemente da tecnologia ou da interface utilizada pelo sistema. |
| Qual conceito do domínio controla o estado de uma matrícula? | **Matrícula** | O ciclo de vida da matrícula (pendente, confirmada ou cancelada) pertence ao próprio conceito de Matrícula. |
| Qual conceito do domínio controla o consumo e a liberação de vagas? | **Turma** | Sempre que uma matrícula for confirmada ou cancelada, a Turma deverá refletir corretamente a quantidade de vagas disponíveis. |

---

# 7. Regras de Negócio Identificadas

As seguintes regras foram identificadas a partir da análise dos requisitos.

**RN01**

Uma matrícula somente poderá ser realizada em uma turma aberta.

---

**RN02**

Uma turma possui um número máximo de vagas.

---

**RN03**

Um aluno não pode possuir duas matrículas para a mesma turma.

---

**RN04**

Toda matrícula inicia com o status PENDENTE.

---

**RN05**

Ao confirmar uma matrícula, uma vaga deverá ser consumida.

---

**RN06**

Ao cancelar uma matrícula confirmada, uma vaga deverá ser devolvida.

---

**RN07**

O sistema deverá permitir consultar matrículas por aluno.

---

**RN08**

O sistema deverá permitir consultar matrículas por turma.

---

# 8. Critérios Arquiteturais

Toda decisão arquitetural deverá responder às seguintes perguntas:

1. Qual problema esta decisão resolve?

2. Existe respaldo em literatura, documentação oficial ou padrão de mercado?

3. Quais alternativas foram consideradas?

4. Por que esta solução foi escolhida?

5. Qual impacto esta decisão possui na manutenção, extensibilidade, testabilidade e legibilidade do sistema?

---

# 9. Artefatos Derivados

Este documento servirá de base para a elaboração dos seguintes artefatos:

01 - Princípios Arquiteturais

02 - Linguagem Ubíqua

03 - Modelo do Domínio

04 - Entidades

05 - Value Objects

06 - Aggregate Roots

07 - Invariantes

08 - Eventos de Domínio

09 - Casos de Uso

10 - Arquitetura da Solução

11 - Modelo de Dados

---

# 10. Considerações

Neste momento nenhuma decisão de implementação foi tomada.

Tecnologias como Spring Boot, Angular, PostgreSQL, Docker, Flyway, MapStruct ou JPA serão introduzidas somente após a conclusão da modelagem do domínio.

Essa abordagem segue o princípio do Domain-Driven Design de que o domínio deve orientar a arquitetura, e não ser moldado por ela.