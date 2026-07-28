# Documento 30 — Roteiro de Testes Manuais

> Versão: 1.0
>
> Fase: Engenharia / QA
>
> Status: Aprovado

---

# 1. Objetivo

Roteiro completo para validar o MVP do Sistema Acadêmico (backend + frontend) com a **carga Flyway de cenários** (`V1000__seed_cenarios_teste.sql`).

Cobre:

- smoke mínimo (fluxo feliz de matrícula);
- CRUD e filtros de todas as entidades do MVP;
- invariantes e erros de domínio;
- telas e fluxos da SPA (Documentos 24–26).

Rastreável aos Documentos 09 (casos de uso), 06 (invariantes), 14 (erros), 21 (MVP) e 24 (UI).

---

# 2. Pré-requisitos

| Item | Valor |
|------|--------|
| Ambiente | `docker compose up` **ou** API local com profile `dev` + PostgreSQL |
| Profile | `dev` (ativa schema + seed em `classpath:db/testdata`) |
| API | `http://localhost:8080` |
| Swagger | `http://localhost:8080/swagger-ui.html` |
| Frontend | `http://localhost:4200` |
| Seed | `src/main/resources/db/testdata/V1000__seed_cenarios_teste.sql` |

**Importante**

- O seed **não** roda nos profiles `test` / `prod` (só `db/migration`).
- UUIDs abaixo são **fixos**. Use-os no Swagger, curl e na UI.
- Após alterar o seed em um banco já migrado: `docker compose down -v` e subir de novo (ou dropar o schema e reiniciar).
- Marque cada item com `[ ]` → `[x]` conforme executar.

---

# 3. Catálogo da carga (IDs fixos)

## 3.1 Cursos

| ID | Nome | Situação | Uso no roteiro |
|----|------|----------|----------------|
| `a0000001-0000-4000-8000-000000000001` | Ciência da Computação | ATIVO | Principal |
| `a0000001-0000-4000-8000-000000000002` | Engenharia de Software | ATIVO | 2º curso / filtro |
| `a0000001-0000-4000-8000-000000000003` | Administração | INATIVO | Filtro situação |
| `a0000001-0000-4000-8000-000000000099` | Curso para Exclusão | ATIVO | DELETE seguro |

## 3.2 Disciplinas

| ID | Código | Nome | Curso | Uso |
|----|--------|------|-------|-----|
| `b0000001-0000-4000-8000-000000000001` | ALG001 | Algoritmos | CC | Turmas principais |
| `b0000001-0000-4000-8000-000000000002` | BD001 | Banco de Dados | CC | Turma lotada |
| `b0000001-0000-4000-8000-000000000003` | REQ001 | Engenharia de Requisitos | ES | Turma disponível |
| `b0000001-0000-4000-8000-000000000004` | CONT001 | Contabilidade Geral | Adm | Filtro `cursoId` |
| `b0000001-0000-4000-8000-000000000099` | EXC001 | Disciplina para Exclusão | Curso exclusão | DELETE |

## 3.3 Períodos letivos

| ID | Código | Intervalo | Situação | Uso |
|----|--------|-----------|----------|-----|
| `c0000001-0000-4000-8000-000000000001` | 2026.1 | 2026-02-01 → 2026-06-30 | ABERTO | Passado / filtros de data |
| `c0000001-0000-4000-8000-000000000002` | 2026.2 | 2026-08-01 → 2026-12-15 | ABERTO | Oferta principal |
| `c0000001-0000-4000-8000-000000000003` | 2025.2 | 2025-08-01 → 2025-12-15 | ENCERRADO | Filtro situação |
| `c0000001-0000-4000-8000-000000000004` | 2026.INT | 2026-07-01 → 2026-07-31 | ABERTO | `vigenteEm=2026-07-28` |
| `c0000001-0000-4000-8000-000000000099` | 2027.1 | 2027-02-01 → 2027-06-30 | ABERTO | DELETE (sem vínculo crítico) |

## 3.4 Alunos

| ID | Nome | E-mail | Situação | Uso |
|----|------|--------|----------|-----|
| `d0000001-0000-4000-8000-000000000001` | Ana Silva | ana.silva@ebm.edu.br | ATIVO | Pendente + histórica |
| `d0000001-0000-4000-8000-000000000002` | Bruno Costa | bruno.costa@ebm.edu.br | ATIVO | Confirmada + 2ª pendente |
| `d0000001-0000-4000-8000-000000000003` | Carla Dias | carla.dias@ebm.edu.br | ATIVO | Cancelada |
| `d0000001-0000-4000-8000-000000000004` | Diego Souza | diego.souza@ebm.edu.br | ATIVO | Livre (realizar / erros) |
| `d0000001-0000-4000-8000-000000000005` | Elena Rocha | elena.rocha@ebm.edu.br | INATIVO | Bloqueio de matrícula |
| `d0000001-0000-4000-8000-000000000006` | Felipe Alves | felipe.alves@ebm.edu.br | ATIVO | Ocupa turma lotada |
| `d0000001-0000-4000-8000-000000000007` | Gabriela Nunes | gabriela.nunes@ebm.edu.br | ATIVO | Pendente sem vaga |
| `d0000001-0000-4000-8000-000000000008` | Helena Prado | helena.prado@ebm.edu.br | ATIVO | 2ª confirmada |
| `d0000001-0000-4000-8000-000000000099` | Aluno para Exclusão | aluno.exclusao@ebm.edu.br | ATIVO | DELETE |

## 3.5 Turmas

| ID | Código | Status | Cap / Vagas | Cenário |
|----|--------|--------|-------------|---------|
| `e0000001-0000-4000-8000-000000000001` | ALG-2026.2-A | ABERTA | 30 / 28 | Feliz + filtros status matrícula |
| `e0000001-0000-4000-8000-000000000002` | BD-2026.2-LOT | ABERTA | 1 / 0 | Sem vagas |
| `e0000001-0000-4000-8000-000000000003` | ALG-2026.1-A | FECHADA | 20 / 20 | Turma fechada / Abrir |
| `e0000001-0000-4000-8000-000000000004` | REQ-2026.2-A | ABERTA | 25 / 25 | Disponível |
| `e0000001-0000-4000-8000-000000000005` | ALG-2026.INT-A | ABERTA | 10 / 10 | Disponível + período vigente |
| `e0000001-0000-4000-8000-000000000006` | BD-2025.2-A | FECHADA | 15 / 14 | Histórica |
| `e0000001-0000-4000-8000-000000000007` | ALG-2026.2-B | FECHADA | 5 / 5 | Abrir / Fechar |
| `e0000001-0000-4000-8000-000000000099` | EXC-2027.1-A | ABERTA | 10 / 10 | DELETE turma |

## 3.6 Matrículas

| ID | Aluno | Turma | Status | Cenário |
|----|-------|-------|--------|---------|
| `f0000001-0000-4000-8000-000000000001` | Ana | ALG-2026.2-A | PENDENTE | Confirmar |
| `f0000001-0000-4000-8000-000000000002` | Bruno | ALG-2026.2-A | CONFIRMADA | Cancelar / filtros |
| `f0000001-0000-4000-8000-000000000003` | Helena | ALG-2026.2-A | CONFIRMADA | Listagem |
| `f0000001-0000-4000-8000-000000000004` | Carla | ALG-2026.2-A | CANCELADA | Estado final |
| `f0000001-0000-4000-8000-000000000005` | Felipe | BD-2026.2-LOT | CONFIRMADA | Ocupa vaga |
| `f0000001-0000-4000-8000-000000000006` | Gabriela | BD-2026.2-LOT | PENDENTE | Confirmar → 409 SemVagas |
| `f0000001-0000-4000-8000-000000000007` | Bruno | REQ-2026.2-A | PENDENTE | Filtros indiretos |
| `f0000001-0000-4000-8000-000000000008` | Ana | BD-2025.2-A | CONFIRMADA | Período encerrado |

---

# 4. Smoke mínimo (obrigatório)

Valida o núcleo do desafio em minutos. Preferir **não alterar** os IDs “para exclusão” nesta seção.

| # | Passo | Onde | Esperado |
|---|-------|------|----------|
| S1 | Listar alunos | `GET /api/alunos` ou `/alunos` | ≥ 9 alunos; Ana e Elena presentes |
| S2 | Listar turmas disponíveis | `GET /api/turmas/disponiveis` | Inclui `REQ-2026.2-A` e `ALG-2026.INT-A`; **não** inclui lotada (`vagas=0`) nem fechadas |
| S3 | Confirmar matrícula da Ana | `POST /api/matriculas/{f…001}/confirmar` | 200; status `CONFIRMADA`; vagas de `ALG-2026.2-A` = **27** |
| S4 | Cancelar a mesma | `POST /api/matriculas/{f…001}/cancelar` | 200; `CANCELADA`; vagas = **28** |
| S5 | Realizar matrícula do Diego em `REQ-2026.2-A` | `POST /api/matriculas` | 201; status `PENDENTE` |
| S6 | Dashboard / menu | UI `/` | Contadores ou atalhos carregam sem erro |

---

# 5. Backend — CRUD e filtros

Base: `http://localhost:8080`. Corpo JSON; enums em maiúsculas (`ATIVO`, `ABERTA`, …).

## 5.1 Alunos — `/api/alunos`

| # | Caso | Chamada | Esperado |
|---|------|---------|----------|
| A1 | Listar sem filtro | `GET /api/alunos` | Lista completa |
| A2 | Filtro nome | `?nome=Ana` | Só Ana (ou contains) |
| A3 | Filtro e-mail | `?email=bruno.costa@ebm.edu.br` | Bruno |
| A4 | Filtro situação | `?situacaoAcademica=INATIVO` | Elena |
| A5 | Buscar por id | `GET /api/alunos/{d…001}` | Ana |
| A6 | 404 | `GET /api/alunos/{uuid-inexistente}` | 404 ProblemDetail |
| A7 | Cadastrar | `POST` nome/e-mail/`ATIVO` | 201 + Location |
| A8 | Validação | `POST` sem e-mail | 400 |
| A9 | Atualizar | `PUT /api/alunos/{d…004}` | 200 |
| A10 | Excluir sem vínculo | `DELETE /api/alunos/{d…099}` | 204 |
| A11 | Matrículas do aluno | `GET /api/alunos/{d…001}/matriculas` | Pendente/cancelada Ana + histórica |
| A12 | Filtro status na consulta | `…/matriculas?status=CONFIRMADA` | Só históricas confirmadas do aluno |

## 5.2 Cursos — `/api/cursos`

| # | Caso | Chamada | Esperado |
|---|------|---------|----------|
| C1 | Listar | `GET /api/cursos` | 4 cursos (se exclusão ainda não rodou) |
| C2 | Filtro nome | `?nome=Software` | Engenharia de Software |
| C3 | Filtro situação | `?situacao=INATIVO` | Administração |
| C4 | Buscar / atualizar | `GET`/`PUT` `{a…001}` | 200 |
| C5 | Cadastrar | `POST` | 201 |
| C6 | Excluir órfão | `DELETE /api/cursos/{a…099}` | 204 (após excluir disciplina/turma filhas, se FK bloquear — ver ordem §8) |

## 5.3 Disciplinas — `/api/disciplinas`

| # | Caso | Chamada | Esperado |
|---|------|---------|----------|
| D1 | Listar | `GET /api/disciplinas` | Lista |
| D2 | Filtro código | `?codigo=ALG001` | Algoritmos |
| D3 | Filtro nome | `?nome=Banco` | BD001 |
| D4 | Filtro curso | `?cursoId={a…002}` | REQ001 |
| D5 | CRUD básico | POST / GET / PUT | 201/200 |
| D6 | Excluir órfã | `DELETE …/{b…099}` | 204 (se sem turmas restantes) |

## 5.4 Períodos letivos — `/api/periodos-letivos`

| # | Caso | Chamada | Esperado |
|---|------|---------|----------|
| P1 | Listar | `GET /api/periodos-letivos` | 5 períodos |
| P2 | Código | `?codigo=2026.2` | Um registro |
| P3 | Situação | `?situacao=ENCERRADO` | 2025.2 |
| P4 | `dataInicioDe` | `?dataInicioDe=2026-07-01` | 2026.INT e 2027.1 (e 2026.2) |
| P5 | `dataTerminoAte` | `?dataTerminoAte=2026-06-30` | 2026.1 (e anteriores) |
| P6 | `vigenteEm` | `?vigenteEm=2026-07-28` | **2026.INT** |
| P7 | CRUD + excluir `{c…099}` | POST/PUT/DELETE | 201/200/204 |

## 5.5 Turmas — `/api/turmas`

| # | Caso | Chamada | Esperado |
|---|------|---------|----------|
| T1 | Listar | `GET /api/turmas` | Todas |
| T2 | Código | `?codigo=LOT` | BD-2026.2-LOT |
| T3 | Status | `?status=FECHADA` | ALG-2026.1-A, BD-2025.2-A, ALG-2026.2-B |
| T4 | Disciplina | `?disciplinaId={b…001}` | Turmas de Algoritmos |
| T5 | Período | `?periodoLetivoId={c…002}` | Turmas 2026.2 |
| T6 | `comVagas=true` | `?comVagas=true` | Exclui BD-2026.2-LOT |
| T7 | Disponíveis | `GET /api/turmas/disponiveis` | ABERTA + vagas > 0 |
| T8 | Disponíveis + disciplina | `…/disponiveis?disciplinaId={b…001}` | ALG-2026.2-A, ALG-2026.INT-A |
| T9 | Abrir | `POST /api/turmas/{e…007}/abrir` | Status ABERTA |
| T10 | Fechar | `POST /api/turmas/{e…007}/fechar` | Status FECHADA |
| T11 | Abrir já aberta | `POST …/{e…001}/abrir` | 422 |
| T12 | Matrículas da turma | `GET /api/turmas/{e…001}/matriculas` | 4 matrículas seed |
| T13 | Filtro status | `…/matriculas?status=PENDENTE` | Ana (se ainda pendente) |
| T14 | Cadastrar / atualizar | POST / PUT | 201/200 |
| T15 | Excluir `{e…099}` | DELETE | 204 |

## 5.6 Matrículas — `/api/matriculas`

| # | Caso | Chamada | Esperado |
|---|------|---------|----------|
| M1 | Listar | `GET /api/matriculas` | Todas |
| M2 | Status | `?status=CANCELADA` | Carla |
| M3 | Aluno | `?alunoId={d…002}` | Bruno (2) |
| M4 | Turma | `?turmaId={e…002}` | Felipe + Gabriela |
| M5 | Período (indireto) | `?periodoLetivoId={c…003}` | Ana histórica |
| M6 | Disciplina (indireto) | `?disciplinaId={b…003}` | Bruno em REQ |
| M7 | Buscar por id | `GET /api/matriculas/{f…002}` | Confirmada Bruno |
| M8 | 404 | id inexistente | 404 |

---

# 6. Backend — regras de negócio e erros

| # | Cenário | Ação | HTTP | Observação |
|---|---------|------|------|------------|
| R1 | Confirmar pendente (feliz) | Confirmar `f…001` (se ainda PENDENTE) | 200 | Consome 1 vaga |
| R2 | Confirmar não pendente | Confirmar `f…002` (já CONFIRMADA) | 422 | Só PENDENTE → CONFIRMADA |
| R3 | Cancelar confirmada | Cancelar `f…002` | 200 | Libera 1 vaga |
| R4 | Cancelar não confirmada | Cancelar `f…004` (CANCELADA) | 422 | Estado final |
| R5 | Sem vagas | Confirmar `f…006` (Gabriela na lotada) | **409** | `SemVagasException` |
| R6 | Liberar vaga e confirmar | Cancelar Felipe `f…005` → confirmar Gabriela `f…006` | 200 | Após liberar, confirmação ok |
| R7 | Turma fechada | Realizar: Diego + `e…003` | 409/422 | `TurmaEncerradaException` |
| R8 | Duplicidade | Realizar: Ana + `e…001` (já existe vínculo) | **409** | `DuplicateMatriculaException` |
| R9 | Aluno inativo | Realizar: Elena + `e…004` | 422 | “Aluno inativo não pode…” |
| R10 | Realizar ok | Diego + `e…005` (ALG-2026.INT-A) | 201 | PENDENTE; vagas **não** mudam até confirmar |
| R11 | Confirmar consome vaga | Confirmar a de R10 | 200 | vagas 10 → 9 |
| R12 | Payload inválido | POST matrícula sem `alunoId` | 400 | Bean Validation |

> Se um passo mutável já foi executado, use o aluno “Aluno para Exclusão” / turma disponível restante, ou recrie o banco.

---

# 7. Frontend — telas e fluxos

Base: `http://localhost:4200`. Estados: loading / empty / error / ready (Documento 26).

## 7.1 Navegação e dashboard

| # | Caso | Esperado |
|---|------|----------|
| F1 | Menu: Início, Cursos, Disciplinas, Períodos, Alunos, Turmas, Matrículas | Todas as rotas abrem |
| F2 | Dashboard `/` | Cards/atalhos; sem spinner eterno |
| F3 | Deep link inválido `/alunos/{uuid-falso}` | Empty 404 + voltar |

## 7.2 CRUD por recurso (lista → novo → detalhe → editar → excluir)

Repetir o padrão para **Cursos, Disciplinas, Períodos Letivos, Alunos, Turmas**:

| # | Passo | Esperado |
|---|-------|----------|
| F4 | Lista sem filtro | Dados do seed |
| F5 | Aplicar cada filtro da tela (§4.5 doc 09) | Resultado coerente; empty se zero |
| F6 | Limpar filtros | Lista completa de novo |
| F7 | Novo → salvar válido | Snackbar + detalhe/lista |
| F8 | Novo → obrigatórios vazios | `mat-error` / não chama API indevida |
| F9 | Editar registro do seed | Persiste após F5 |
| F10 | Excluir item `* para Exclusão` (com confirmação) | Some da lista |
| F11 | Loading em submit | Botão desabilitado (“Salvando…”) |

## 7.3 Turma — abrir / fechar

| # | Caso | Esperado |
|---|------|----------|
| F12 | Detalhe `ALG-2026.2-B` → Abrir | Status ABERTA + snackbar |
| F13 | Mesma → Fechar | Status FECHADA |
| F14 | Bloco de matrículas no detalhe de `ALG-2026.2-A` | Lista as 4 do seed |

## 7.4 Ciclo de matrícula (UI)

| # | Caso | Esperado |
|---|------|----------|
| F15 | `/matriculas` + filtros status/aluno/turma | Coerente com API |
| F16 | `/matriculas/nova` — Diego + turma disponível | Cria PENDENTE |
| F17 | Apoio turmas disponíveis no formulário | Não lista lotada/fechada |
| F18 | Detalhe → Confirmar | Status CONFIRMADA; vagas na turma −1 |
| F19 | Detalhe → Cancelar (com dialog) | CANCELADA; vaga +1 |
| F20 | Atalho no detalhe do aluno → nova matrícula | Aluno pré-selecionado |
| F21 | Atalho no detalhe da turma → nova matrícula | Turma pré-selecionada |

## 7.5 Erros na UI (ProblemDetail)

| # | Caso | Esperado |
|---|------|----------|
| F22 | Tentar matricular Elena (inativa) | Snackbar 422 com `detail` |
| F23 | Duplicar Ana em ALG-2026.2-A | Snackbar 409 |
| F24 | Confirmar Gabriela na lotada (se ainda pendente e Felipe confirmado) | Snackbar 409 Sem vagas |
| F25 | Matricular em turma fechada | Snackbar de regra |
| F26 | API parada / rede | Mensagem neutra (sem stack) |

---

# 8. Ordem sugerida para exclusões

Para não esbarrar em FK ao limpar os registros `* para Exclusão`:

```text
1. Matrículas (se houver) da turma EXC-2027.1-A
2. DELETE turma e…099
3. DELETE disciplina b…099
4. DELETE período c…099   (se não houver outras turmas)
5. DELETE curso a…099
6. DELETE aluno d…099
```

---

# 9. Checklist de cobertura MVP

### Casos de uso (Documento 09)

- [ ] CRUD Aluno / Curso / Disciplina / Período Letivo / Turma
- [ ] AbrirTurma / FecharTurma
- [ ] ConsultarTurmasDisponiveis
- [ ] Realizar / Confirmar / Cancelar matrícula
- [ ] ListarMatriculas / BuscarMatriculaPorId
- [ ] ConsultarMatriculasPorAluno / PorTurma
- [ ] Filtros §4.5 em todas as listagens (incl. datas / `vigenteEm`)

### Invariantes (Documento 06)

- [ ] INV-01/02 — vagas coerentes após confirmar/cancelar
- [ ] INV-03 — turma fechada rejeita matrícula
- [ ] INV-04 — duplicidade rejeitada
- [ ] INV-05 — cancelamento devolve vaga
- [ ] INV-06 — confirmação consome vaga
- [ ] Aluno inativo não matricula

### Frontend (Documentos 24–26)

- [ ] Rotas do inventário
- [ ] Fluxo matrícula + atalhos aluno/turma
- [ ] Estados loading / empty / error / ready
- [ ] Erros 400 / 404 / 409 / 422 / rede

---

# 10. Exemplos curl (smoke)

```bash
# Turmas disponíveis
curl -s "http://localhost:8080/api/turmas/disponiveis" | jq

# Confirmar matrícula pendente da Ana
curl -s -X POST "http://localhost:8080/api/matriculas/f0000001-0000-4000-8000-000000000001/confirmar" | jq

# Período vigente em 2026-07-28
curl -s "http://localhost:8080/api/periodos-letivos?vigenteEm=2026-07-28" | jq

# Tentar confirmar Gabriela na lotada (esperar 409)
curl -s -o /tmp/out.json -w "%{http_code}" \
  -X POST "http://localhost:8080/api/matriculas/f0000001-0000-4000-8000-000000000006/confirmar"
```

---

# 11. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 09 — Casos de Uso | Inventário de operações e filtros |
| 06 / 14 | Invariantes e mapeamento HTTP |
| 21 | Limites do MVP |
| 24–26 | Telas, fluxos e erros de UI |
| 15 / 29 | Estratégia automatizada (este roteiro é **manual**) |
| Seed Flyway | `db/testdata/V1000__seed_cenarios_teste.sql` (profile `dev`) |

---

# 12. Considerações finais

Este roteiro é a referência operacional para homologar o MVP com dados previsíveis. Testes automatizados (JUnit / Testcontainers) **não** dependem deste seed — continuam com banco limpo via `db/migration` apenas.
