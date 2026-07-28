# Documento 09 — Casos de Uso

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento identifica os **Casos de Uso** do Sistema Acadêmico e define suas responsabilidades dentro da arquitetura da aplicação.

Os Casos de Uso representam as operações disponibilizadas pelo sistema aos usuários ou outros sistemas, coordenando a execução das regras de negócio presentes no domínio.

Seu papel é orquestrar entidades, Aggregate Roots, repositórios e eventos de domínio, sem incorporar regras de negócio próprias.

---

# 2. Conceito de Caso de Uso

Um **Caso de Uso** representa uma ação executada pelo sistema para atender a um objetivo do negócio.

Segundo os princípios da Clean Architecture e da Arquitetura Hexagonal, os Casos de Uso pertencem à camada de aplicação.

Sua responsabilidade é:

- Receber uma solicitação;
- Recuperar os objetos necessários;
- Coordenar a execução das regras do domínio;
- Persistir as alterações;
- Publicar Eventos de Domínio quando necessário.

Os Casos de Uso **não implementam regras de negócio**.

As regras permanecem encapsuladas nas entidades e Aggregate Roots.

---

# 3. Responsabilidades dos Casos de Uso

Os Casos de Uso são responsáveis por:

- Orquestrar o fluxo da aplicação;
- Carregar os Aggregates necessários;
- Invocar comportamentos do domínio;
- Persistir alterações utilizando repositórios;
- Publicar Eventos de Domínio;
- Retornar o resultado da operação.

Eles não devem:

- Alterar atributos diretamente;
- Validar regras pertencentes ao domínio;
- Conhecer detalhes de persistência;
- Depender de frameworks específicos.

---

# 4. Casos de Uso Identificados

Após a análise do domínio foram identificados os seguintes Casos de Uso.

O escopo da versão 1.0 (MVP) inclui o **CRUD completo** de Aluno, Curso, Disciplina, **Período Letivo** e Turma, o ciclo de matrícula (**Realizar**, **Confirmar** e **Cancelar**), a **listagem e busca global de matrículas**, as consultas de matrículas por aluno e por turma, as **listagens com critérios avançados** (§4.5) e a consulta de **turmas disponíveis**.

Nas listagens (`Listar*` / `Consultar*`), os filtros são **opcionais** (consulta sem critério continua válida). Os critérios de texto, status, relacionamento e data do inventário §4.5 **fazem parte do MVP**. A **paginação** permanece no **curto prazo** (Documento 21, §4.1).

## 4.1 CRUD — Aluno, Curso, Disciplina e Turma

| Caso de Uso | Objetivo |
|--------------|----------|
| CadastrarAluno | Registrar um novo aluno |
| AtualizarAluno | Alterar dados de um aluno existente |
| ListarAlunos | Listar alunos cadastrados |
| BuscarAlunoPorId | Obter o detalhe de um aluno |
| ExcluirAluno | Remover um aluno |
| CadastrarCurso | Registrar um novo curso |
| AtualizarCurso | Alterar dados de um curso existente |
| ListarCursos | Listar cursos cadastrados |
| BuscarCursoPorId | Obter o detalhe de um curso |
| ExcluirCurso | Remover um curso |
| CadastrarDisciplina | Registrar uma nova disciplina |
| AtualizarDisciplina | Alterar dados de uma disciplina existente |
| ListarDisciplinas | Listar disciplinas cadastradas |
| BuscarDisciplinaPorId | Obter o detalhe de uma disciplina |
| ExcluirDisciplina | Remover uma disciplina |
| CadastrarTurma | Criar uma nova turma |
| AtualizarTurma | Alterar dados de uma turma existente |
| ListarTurmas | Listar turmas cadastradas |
| BuscarTurmaPorId | Obter o detalhe de uma turma |
| ExcluirTurma | Remover uma turma |

## 4.2 Período Letivo

No MVP, Período Letivo possui **CRUD completo**. Em `ListarPeriodosLetivos`, além de texto e situação, há os únicos **filtros de data naturais** do modelo atual.

| Caso de Uso | Objetivo |
|--------------|----------|
| CadastrarPeriodoLetivo | Registrar um novo período letivo |
| AtualizarPeriodoLetivo | Alterar dados de um período letivo existente |
| ListarPeriodosLetivos | Listar períodos letivos (com critérios opcionais de texto, situação e data) |
| BuscarPeriodoLetivoPorId | Obter o detalhe de um período letivo |
| ExcluirPeriodoLetivo | Remover um período letivo |

O **fechamento** do período letivo permanece fora deste inventário (médio prazo — Documento 21, §4.2).

## 4.3 Turma — disponibilidade

| Caso de Uso | Objetivo |
|--------------|----------|
| AbrirTurma | Disponibilizar uma turma para matrícula |
| FecharTurma | Encerrar novas matrículas |
| ConsultarTurmasDisponiveis | Listar turmas abertas com vagas (atalho sobre `ListarTurmas`) |

`ConsultarTurmasDisponiveis` (MVP) é atalho semântico sobre `ListarTurmas` com `status=ABERTA` e `comVagas=true`, podendo aceitar ainda `periodoLetivoId` e/ou `disciplinaId`.

## 4.4 Matrícula

| Caso de Uso | Objetivo |
|--------------|----------|
| RealizarMatricula | Criar uma matrícula para um aluno |
| ConfirmarMatricula | Confirmar uma matrícula existente |
| CancelarMatricula | Cancelar uma matrícula existente |
| ListarMatriculas | Listar matrículas (visão global, com critérios opcionais) |
| BuscarMatriculaPorId | Obter o detalhe de uma matrícula |
| ConsultarMatriculasPorAluno | Listar as matrículas de um aluno |
| ConsultarMatriculasPorTurma | Listar as matrículas de uma turma |

`ListarMatriculas` e `BuscarMatriculaPorId` atendem às telas `/matriculas` e `/matriculas/:id` (Documento 24). `ConsultarMatriculasPorAluno` / `ConsultarMatriculasPorTurma` **permanecem** como atalhos nos detalhes de aluno e turma; a listagem global não os substitui.

Todos os Casos de Uso representam ações do negócio e seguem a convenção **Verbo + Substantivo**.

---

## 4.5 Critérios de consulta

Os filtros abaixo usam apenas atributos já modelados nos Documentos 04 e 05. **Todos os critérios deste inventário fazem parte do MVP** (opcionais na chamada). A **paginação** é evolução de curto prazo (Documento 21, §4.1).

Datas de auditoria (`createdAt` e similares) ficam fora do domínio e fora deste inventário.

### ListarAlunos

Uso: tela administrativa de alunos e apoio à matrícula.

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `nome` | texto (contains / startsWith) | Nome | MVP |
| `email` | texto (equals / contains) | VO Email | MVP |
| `situacaoAcademica` | enum | Situação Acadêmica | MVP |

### ListarCursos

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `nome` | texto | Nome | MVP |
| `situacao` | enum | Situação | MVP |

### ListarDisciplinas

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `nome` | texto | Nome | MVP |
| `codigo` | texto (equals / contains) | Código | MVP |
| `cursoId` | relacionamento | Disciplina pertence a Curso | MVP |

### ListarPeriodosLetivos

Única entidade com **filtros de data naturais** no modelo atual (`Data de Início` / `Data de Término`).

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `codigo` | texto (ex.: `2026.1`) | Código | MVP |
| `situacao` | enum | Situação | MVP |
| `dataInicioDe` / `dataInicioAte` | intervalo de data | Data de Início | MVP |
| `dataTerminoDe` / `dataTerminoAte` | intervalo de data | Data de Término | MVP |
| `vigenteEm` | data pontual | períodos cujo intervalo contém a data | MVP |

### ListarTurmas

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `codigo` | texto | Código | MVP |
| `status` | enum `ABERTA` / `FECHADA` | Status da Turma | MVP |
| `disciplinaId` | relacionamento | Turma → Disciplina | MVP |
| `periodoLetivoId` | relacionamento | Turma → Período Letivo | MVP |
| `comVagas` | booleano | vagas disponíveis > 0 | MVP |

`ConsultarTurmasDisponiveis` (MVP) = `status=ABERTA` + `comVagas=true` (+ opcionalmente `periodoLetivoId` / `disciplinaId`).

### ListarMatriculas

Uso: tela administrativa `/matriculas` (Documento 24). Todos os filtros são **opcionais**.

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `status` | enum `PENDENTE` / `CONFIRMADA` / `CANCELADA` | Status da Matrícula | MVP |
| `alunoId` | relacionamento | vínculo | MVP |
| `turmaId` | relacionamento | vínculo | MVP |
| `periodoLetivoId` | relacionamento indireto | via Turma | MVP |
| `disciplinaId` | relacionamento indireto | via Turma | MVP |

Os filtros indiretos (`periodoLetivoId` / `disciplinaId`) reutilizam o mesmo inventário das consultas por aluno/turma, por consistência.

### ConsultarMatriculasPorAluno / ConsultarMatriculasPorTurma

A consulta exige a chave informada (`alunoId` ou `turmaId`) e admite filtros adicionais no MVP:

| Filtro | Tipo | Base documental | Escopo |
|--------|------|-----------------|--------|
| `alunoId` | relacionamento | vínculo | MVP (`ConsultarMatriculasPorAluno`) |
| `turmaId` | relacionamento | vínculo | MVP (`ConsultarMatriculasPorTurma`) |
| `status` | enum `PENDENTE` / `CONFIRMADA` / `CANCELADA` | Status da Matrícula | MVP |
| `periodoLetivoId` | relacionamento indireto | via Turma | MVP |
| `disciplinaId` | relacionamento indireto | via Turma | MVP |

Não há data de matrícula modelada nos Documentos 03/04; **não se inventa** `dataMatricula` neste inventário.

---

# 5. Fluxo Geral de Execução

Todos os Casos de Uso deverão seguir, de forma conceitual, o seguinte fluxo.

```text
Receber solicitação

↓

Validar entrada da aplicação

↓

Recuperar Aggregates

↓

Executar regras do domínio

↓

Persistir alterações

↓

Publicar Eventos de Domínio

↓

Retornar resultado
```

Esse fluxo garante a separação entre aplicação e domínio.

---

# 6. Fluxos Conceituais

## 6.1 CRUD genérico (Atualizar / Excluir)

Os casos `Atualizar*` e `Excluir*` de Aluno, Curso, Disciplina, Período Letivo e Turma seguem o mesmo padrão conceitual.

```text
Receber solicitação

↓

Validar entrada da aplicação

↓

Buscar entidade / Aggregate por identificador

↓

Executar atualização ou exclusão no domínio

↓

Persistir alterações

↓

Retornar resultado
```

Os casos `Listar*` e `Buscar*PorId` recuperam dados via repositório e retornam o resultado, sem alterar o estado do domínio. Critérios opcionais de filtro constam na seção 4.5 (MVP); paginação no curto prazo (Documento 21, §4.1).

---

## 6.2 Confirmar Matrícula

```text
Receber solicitação

↓

Buscar Matrícula

↓

Buscar Turma

↓

Matrícula.confirmar()

↓

Turma.consumirVaga()

↓

Salvar alterações

↓

Publicar MatriculaConfirmada

↓

Retornar sucesso
```

---

## 6.3 Cancelar Matrícula

```text
Receber solicitação

↓

Buscar Matrícula

↓

Buscar Turma

↓

Matrícula.cancelar()

↓

Turma.liberarVaga()

↓

Salvar alterações

↓

Publicar MatriculaCancelada

↓

Retornar sucesso
```

---

## 6.4 Consultar Matrículas por Aluno

```text
Receber solicitação (identificador do aluno)

↓

Validar entrada da aplicação

↓

Buscar Aluno

↓

Consultar matrículas vinculadas ao aluno

↓

Retornar lista de matrículas
```

---

## 6.5 Consultar Matrículas por Turma

```text
Receber solicitação (identificador da turma)

↓

Validar entrada da aplicação

↓

Buscar Turma

↓

Consultar matrículas vinculadas à turma

↓

Retornar lista de matrículas
```

---

## 6.6 Abrir Turma

```text
Receber solicitação

↓

Buscar Turma

↓

Turma.abrir()

↓

Salvar alterações

↓

Publicar TurmaAberta

↓

Retornar sucesso
```

---

## 6.7 Fechar Turma

```text
Receber solicitação

↓

Buscar Turma

↓

Turma.fechar()

↓

Salvar alterações

↓

Publicar TurmaFechada

↓

Retornar sucesso
```

---

# 7. Dependências dos Casos de Uso

Os Casos de Uso poderão depender apenas de abstrações da aplicação e do domínio.

Exemplos:

- Repositórios do domínio;
- Publicador de Eventos de Domínio;
- Objetos de entrada (Commands);
- Objetos de saída (Responses).

Não deverão depender diretamente de:

- Spring Framework;
- JPA;
- Controllers;
- Banco de Dados.

Essa separação favorece o desacoplamento entre domínio e infraestrutura.

---

# 8. Relação com o Domínio

Os Casos de Uso coordenam a execução do domínio, mas não substituem suas responsabilidades.

```text
Controller

↓

Caso de Uso

↓

Aggregate Root

↓

Entidades

↓

Value Objects
```

As regras de negócio permanecem concentradas no domínio.

O Caso de Uso apenas coordena sua execução.

---

# 9. Relação com os Eventos de Domínio

Após uma operação concluída com sucesso, o Caso de Uso poderá publicar Eventos de Domínio.

Exemplo:

```text
Confirmar Matrícula

↓

Persistir alterações

↓

Publicar

MatriculaConfirmada

↓

Auditoria

↓

Notificação

↓

Integrações futuras
```

Os consumidores dos eventos permanecem desacoplados do Caso de Uso.

---

# 10. Decisões de Modelagem

## MD-013 — Casos de Uso independentes de Frameworks

### Decisão

Os Casos de Uso não dependerão diretamente do Spring Framework, **exceto** pelo uso de `@Transactional` na camada de aplicação no MVP, conforme [ADR-003](22%20-%20Registro%20de%20Decisões%20Arquiteturais%20(ADR).md) (Documento 22). O domínio permanece livre de Spring.

### Justificativa

A camada de aplicação deve permanecer independente da tecnologia utilizada, facilitando testes e reduzindo o acoplamento. No MVP Spring Boot, delimitar a transação com `@Transactional` nos use cases é a exceção pragmática registrada no ADR-003; a reavaliação ocorre se surgirem adapters não-Spring.

---

## MD-014 — Regras permanecem no domínio

### Decisão

Os Casos de Uso não conterão regras de negócio.

### Justificativa

As entidades e Aggregate Roots são responsáveis por proteger o estado do domínio e garantir suas invariantes.

---

## MD-015 — Persistência por abstrações

### Decisão

Os Casos de Uso utilizarão apenas interfaces de repositório definidas no domínio.

### Justificativa

Essa abordagem permite substituir tecnologias de persistência sem alterar a lógica da aplicação.

---

## MD-016 — Eventos publicados após persistência

### Decisão

Eventos de Domínio serão publicados somente após a conclusão bem-sucedida da operação.

### Justificativa

Evita a divulgação de eventos referentes a alterações que não foram efetivamente persistidas.

---

# 11. Exemplo Conceitual

O comportamento esperado de um Caso de Uso pode ser representado da seguinte forma.

```text
Usuário

↓

Controller

↓

Caso de Uso

↓

Repositórios

↓

Aggregate Root

↓

Entidades

↓

Persistência

↓

Eventos de Domínio

↓

Consumidores
```

Cada camada possui uma responsabilidade específica, reduzindo o acoplamento e aumentando a coesão da aplicação.

---

# 12. Referências

Este documento foi elaborado com base nas seguintes referências:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Alistair Cockburn — *Hexagonal Architecture*

---

# 13. Considerações Finais

Os Casos de Uso representam a camada de aplicação responsável por coordenar a execução do domínio.

Sua responsabilidade limita-se à orquestração das operações, preservando a separação entre regras de negócio, infraestrutura e interfaces externas.

Essa abordagem favorece um modelo desacoplado, testável e alinhado aos princípios do Domain-Driven Design, da Clean Architecture e da Arquitetura Hexagonal.

---

# 14. Próximos Passos

Este documento servirá como base para:

- Documento 10 — Arquitetura Hexagonal;
- Documento 11 — Repositórios (Ports e Adapters);
- Documento 13 — Persistência com JPA;
- Documento 15 — Estratégia de Testes.