# Documento 27 — Comunicação com a API

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define como o frontend Angular consome a API REST do Sistema Acadêmico: base URL, organização dos services, convenções de contrato e mapeamento tela → endpoint.

Complementa a arquitetura ([Documento 23](23%20-%20Arquitetura%20do%20Frontend.md) — HttpClient manual) e o tratamento de erros ([Documento 26](26%20-%20Estados%20da%20Interface%20e%20Erros.md) — `ProblemDetail`).

A fonte de consulta do contrato HTTP permanece o **OpenAPI** (`/v3/api-docs` / Swagger UI).

---

# 2. Decisões

| # | Decisão | Escolha |
|---|---------|---------|
| 1 | Base URL | **`environment` com URL absoluta** |
| 2 | Services | **Um service HTTP por feature** |
| 3 | Contrato no FE | **Espelhar a API** (sem anti-corruption layer) |

---

# 3. Base URL e ambientes

| Ambiente | `apiUrl` (referência) | Observação |
|----------|----------------------|------------|
| Dev (`ng serve`) | `http://localhost:8080` | CORS libera `http://localhost:4200` (ADR-002 / Documento 20) |
| Compose / build estático | `http://localhost:8080` | O **browser** chama o host da máquina; não usar `http://backend:8080` no JavaScript |

- Os paths da API começam com `/api/...`.
- URL final das chamadas: `${apiUrl}/api/...`.
- Proxy do `ng serve` fica fora do MVP (roadmap — Documento 29).
- Sem autenticação no MVP: sem header `Authorization`.

---

# 4. Services por feature

| Feature | Service (exemplo) | Base path |
|---------|-------------------|-----------|
| Aluno | `AlunoService` | `/api/alunos` |
| Curso | `CursoService` | `/api/cursos` |
| Disciplina | `DisciplinaService` | `/api/disciplinas` |
| Período Letivo | `PeriodoLetivoService` | `/api/periodos-letivos` |
| Turma | `TurmaService` | `/api/turmas` |
| Matrícula | `MatriculaService` | `/api/matriculas` |

- Cada service injeta `HttpClient` e lê `environment.apiUrl`.
- Tipos TypeScript alinhados aos `*Request` / `*Response` do backend.
- Interceptor de erro em `core/` (Documento 26); os services não implementam toast genérico.
- O dashboard ([Documento 24](24%20-%20Navegação,%20Telas%20e%20Fluxos.md)) compõe chamadas a esses services — **sem** endpoint `/api/dashboard`.

---

# 5. Convenções de contrato

- JSON em **camelCase** (padrão Jackson do backend).
- Listagens: **array** no body (sem envelope `{ data: [] }`).
- Create: tipicamente **201 Created** + body; delete: **204 No Content** sem body.
- Filtros: **query params** opcionais; enviar apenas parâmetros preenchidos.
- Enums como strings (`ATIVO`, `PENDENTE`, `ABERTA`, …).
- IDs: UUID em path/body conforme a API.
- Erros: `ProblemDetail` (Documentos 14 e 26).
- OpenAPI: consulta e validação do contrato; **sem** geração automática de cliente no MVP (Documento 23).

---

# 6. Mapa tela / caso de uso → HTTP

## 6.1 CRUD genérico (exemplo Aluno)

| UI / caso de uso | HTTP |
|------------------|------|
| ListarAlunos | `GET /api/alunos?…` |
| BuscarAlunoPorId | `GET /api/alunos/{id}` |
| CadastrarAluno | `POST /api/alunos` |
| AtualizarAluno | `PUT /api/alunos/{id}` |
| ExcluirAluno | `DELETE /api/alunos/{id}` |
| ConsultarMatriculasPorAluno | `GET /api/alunos/{id}/matriculas?…` |

O mesmo padrão aplica-se a `/api/cursos`, `/api/disciplinas`, `/api/periodos-letivos` e `/api/turmas`.

## 6.2 Turma — operações extras

| Caso de uso | HTTP |
|-------------|------|
| ConsultarTurmasDisponiveis | `GET /api/turmas/disponiveis?…` |
| AbrirTurma | `POST /api/turmas/{id}/abrir` |
| FecharTurma | `POST /api/turmas/{id}/fechar` |
| ConsultarMatriculasPorTurma | `GET /api/turmas/{id}/matriculas?…` |

## 6.3 Matrícula

| Caso de uso | HTTP |
|-------------|------|
| RealizarMatricula | `POST /api/matriculas` |
| ConfirmarMatricula | `POST /api/matriculas/{id}/confirmar` |
| CancelarMatricula | `POST /api/matriculas/{id}/cancelar` |

O detalhe dos query params de filtro está no [Documento 09](09%20-%20Casos%20de%20Uso.md) (§4.5) e nos controllers. O frontend utiliza os **mesmos nomes** de parâmetro.

---

# 7. Headers e conteúdo

| Item | Valor |
|------|--------|
| `Content-Type` | `application/json` (bodies) |
| `Accept` | `application/json` (e `application/problem+json` quando aplicável) |
| Auth | Ausente no MVP |

---

# 8. Fora de escopo

- Cliente gerado a partir do OpenAPI;
- Proxy de desenvolvimento / BFF;
- Cache HTTP agressivo ou service worker;
- Paginação (evolução — Documento 21);
- Árvore de pastas de `environment` e services → **[Documento 28](28%20-%20Componentização%20e%20Estrutura%20do%20Projeto.md)**.

---

# 9. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 09 — Casos de Uso | Casos de uso e filtros |
| 14 / 26 | Erros e `ProblemDetail` |
| 20 — Segurança | CORS |
| 22 — ADR | ADR-002 (porta/CORS), ADR-004 (frontend) |
| 23 — Arquitetura do Frontend | HttpClient, sem codegen |
| 24 — Navegação, Telas e Fluxos | Telas que disparam as chamadas |
| 28 — Componentização e Estrutura | Onde ficam `environment` e services |
| 29 — Testes e Roadmap | Proxy, codegen, E2E, etc. |

---

# 10. Considerações finais

Alterações de base URL, organização dos services ou do contrato consumido pelo frontend devem atualizar este documento. Mudanças nos paths ou payloads da API devem ser refletidas aqui, no OpenAPI e, quando estruturais, no Documento 22 (ADR).
