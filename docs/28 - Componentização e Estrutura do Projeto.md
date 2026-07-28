# Documento 28 — Componentização e Estrutura do Projeto

> Versão: 1.0
>
> Fase: Engenharia
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estrutura de pastas, o padrão de componentização e as convenções de naming do módulo `frontend/` do Sistema Acadêmico.

A estrutura detalha as decisões dos [Documentos 23](23%20-%20Arquitetura%20do%20Frontend.md) a [27](27%20-%20Comunicação%20com%20a%20API.md) e alinha-se à pasta `frontend/` descrita no [Documento 16](16%20-%20Estrutura%20do%20Projeto.md).

---

# 2. Decisões

| # | Decisão | Escolha |
|---|---------|---------|
| 1 | Estilo Angular | **Standalone** + `app.routes.ts` (lazy por feature quando fizer sentido) |
| 2 | Granularidade | **Páginas por rota**; extrair para shared/feature só o repetido ou inchado |
| 3 | Árvore | **`core` / `shared` / `layout` / `features`** (§4) |

---

# 3. Princípios de componentização

- Uma **rota** corresponde a um componente de página (lista, detalhe, formulário, dashboard).
- Extrair para `shared/` quando o trecho for usado em **duas ou mais** features.
- Extrair **dentro da feature** quando a página ficar claramente grande (ex.: painel de matrículas no detalhe do aluno).
- Evitar componentizar “por tabela” ou “por botão” sem reuso real.
- Features **não** devem depender umas das outras sem necessidade; preferir navegação por rota e o service da própria feature ([Documento 27](27%20-%20Comunicação%20com%20a%20API.md)).

---

# 4. Árvore do projeto

```text
frontend/
├── Dockerfile
├── angular.json
├── package.json
├── tsconfig*.json
└── src/
    ├── environments/
    │   ├── environment.ts
    │   └── environment.prod.ts
    ├── styles/                         # tema Material + tokens (Documento 25)
    ├── index.html
    ├── main.ts
    └── app/
        ├── app.config.ts
        ├── app.routes.ts
        ├── app.ts                      # root (host do shell)
        ├── core/
        │   ├── api/                    # helpers de URL / HttpParams
        │   ├── interceptors/           # ProblemDetail (Documento 26)
        │   └── services/               # feedback global (snackbar)
        ├── shared/
        │   └── components/             # empty, status chip, confirm dialog, …
        ├── layout/                     # sidenav, drawer, toolbar
        └── features/
            ├── dashboard/
            ├── aluno/                  # list, detail, form, service, models
            ├── curso/
            ├── disciplina/
            ├── periodo-letivo/
            ├── turma/
            └── matricula/
```

Os nomes exatos dos arquivos seguem o padrão do Angular CLI (kebab-case).

---

# 5. Responsabilidades por pasta

| Pasta | Deve conter | Não deve conter |
|-------|-------------|-----------------|
| `environments/` | `apiUrl` e flags de ambiente (Documento 27) | Lógica de negócio |
| `styles/` | Tema laranja, tipografia, overrides Material (Documento 25) | Regras por feature |
| `core/` | Singletons: interceptor, feedback, utilitários HTTP | Telas CRUD |
| `shared/` | UI genérica reutilizável | Chamadas HTTP de um único domínio |
| `layout/` | Shell (sidebar/drawer) + `router-outlet` | Formulários de entidade |
| `features/*` | Páginas, service e models daquele agregado | Import circular entre features |

---

# 6. Conteúdo típico de uma feature CRUD

Exemplo `features/aluno/`:

| Artefato | Papel |
|----------|--------|
| `aluno-list` | Lista + filtros (Documentos 24 e 09 §4.5) |
| `aluno-detail` | Detalhe + excluir + bloco de matrículas |
| `aluno-form` | Criar / editar |
| `aluno.service.ts` | HttpClient (Documento 27) |
| `aluno.model.ts` (ou pasta `models/`) | Tipos alinhados à API |
| `aluno.routes.ts` | Rotas lazy da feature |

Turma e matrícula acrescentam ações extras nas páginas (abrir/fechar; confirmar/cancelar), sem pastas técnicas separadas obrigatórias.

---

# 7. Rotas

- Rotas raiz em `app.routes.ts`, alinhadas ao menu do [Documento 24](24%20-%20Navegação,%20Telas%20e%20Fluxos.md).
- Preferir **lazy loading** por feature (`loadChildren` / `loadComponent`).
- Paths: `/`, `/cursos`, `/disciplinas`, `/periodos-letivos`, `/alunos`, `/turmas`, `/matriculas`, com `novo`, `:id` e `:id/editar` conforme o padrão CRUD.

---

# 8. Naming

| Tipo | Convenção |
|------|-----------|
| Arquivos / pastas | kebab-case (`periodo-letivo`) |
| Componentes | contexto claro (`aluno-list`) |
| Services | `*.service.ts` |
| Classes / interfaces TypeScript | PascalCase |
| Seletores | prefixo do app (ex.: `app-aluno-list`), definido no CLI |

Identificadores de código acompanham a linguagem ubíqua já usada no backend (`aluno`, `matricula`, `periodo-letivo`).

---

# 9. Fora de escopo

- Monorepo / libraries Nx;
- Storybook;
- Geração automática de pastas além do scaffold Angular;
- Estratégia de testes e roadmap do frontend → **[Documento 29](29%20-%20Testes%20e%20Roadmap%20do%20Frontend.md)**.

---

# 10. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 16 — Estrutura do Projeto | Pasta `frontend/` no repositório |
| 23 — Arquitetura do Frontend | Features + core/shared/layout |
| 24 — Navegação, Telas e Fluxos | Páginas e rotas |
| 25 — Layout, Design System e UX | Tema em `styles/` e shell em `layout/` |
| 26 — Estados da Interface e Erros | Interceptor em `core/` |
| 27 — Comunicação com a API | Services e `environments` |
| 29 — Testes e Roadmap | Evolução da estrutura |

---

# 11. Considerações finais

Mudanças na organização de pastas (ex.: abandono de standalone ou fusão de `core`/`shared`) devem atualizar este documento e, se forem decisões estruturais, o [Documento 22 — ADR](22%20-%20Registro%20de%20Decisões%20Arquiteturais%20(ADR).md).
