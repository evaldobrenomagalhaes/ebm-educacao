# Documento 29 — Testes e Roadmap do Frontend

> Versão: 1.0
>
> Fase: Engenharia
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estratégia de testes do frontend Angular no MVP e o roadmap de evolução da UI do Sistema Acadêmico.

Alinha-se aos [Documentos 15](15%20-%20Estratégia%20de%20Testes.md) (pirâmide de testes), [17](17%20-%20Pipeline%20CI_CD.md) (CI), [21](21%20-%20Plano%20de%20Evolução%20do%20Sistema.md) (evolução) e [23](23%20-%20Arquitetura%20do%20Frontend.md)–[28](28%20-%20Componentização%20e%20Estrutura%20do%20Projeto.md) (especificação do frontend).

A **implementação da SPA do MVP** (telas e estrutura dos Documentos 24–28) é trabalho imediato de engenharia, não item de roadmap.

---

# 2. Decisões

| # | Decisão | Escolha |
|---|---------|---------|
| 1 | Testes no MVP | **Mínimo útil** (build + unitários de services + 1–2 fluxos críticos) |
| 2 | Roadmap | **Curto → médio → longo** (§5) |

---

# 3. Princípios de teste (frontend)

- O **domínio e a API** já são validados no backend (Documento 15); o frontend não retesta invariantes de negócio.
- Priorizar: contrato HTTP tipado, interceptor de erro e fluxos de matrícula/CRUD na UI.
- Preferir testes **rápidos e estáveis** a suites E2E frágeis no MVP.
- O CI atual executa apenas Maven; incluir job do frontend quando o aplicativo Angular existir.

---

# 4. Estratégia de testes — MVP

| Camada | Escopo MVP | Ferramenta típica |
|--------|------------|-------------------|
| Build | `ng build` (produção) no CI | Angular CLI |
| Unitário — services | Services por feature + interceptor de erro | Jasmine/Jest + `HttpClientTestingModule` |
| Unitário — componentes | 1–2 casos críticos (ex.: formulário de matrícula ou lista com filtros) | TestBed |
| E2E | Fora do MVP | — |
| Gate rígido de cobertura (%) | Fora do MVP | Roadmap |

**Smoke manual** permanece válido: Docker Compose + fluxo de matrícula/vagas do README, além do Swagger UI.

Quando o frontend entrar no CI (Documento 17), o fluxo previsto é: checkout → Node LTS → `npm ci` → `ng test --watch=false` (mínimo) → `ng build`.

---

# 5. Roadmap do frontend

## 5.1 Curto prazo

- Job de CI: install, testes mínimos e build do Angular;
- UI de **paginação** quando a API expuser paginação (Documento 21, §4.1);
- Proxy opcional do `ng serve` (alternativa à URL absoluta — Documento 27).

## 5.2 Médio prazo

- E2E smoke (ex.: Playwright) do ciclo de matrícula;
- Acessibilidade além do mínimo sólido (Documento 25);
- Geração de cliente/tipos a partir do OpenAPI (opcional);
- Storybook ou catálogo leve de componentes `shared/` (opcional).

## 5.3 Longo prazo / evolução de produto

- Telas de autenticação / sessão quando o backend adotar Security/JWT (Documentos 20 e 21);
- Store global (NgRx) **somente** se o estado compartilhado justificar;
- PWA / offline — apenas com necessidade explícita.

---

# 6. Relação com o plano do sistema (Documento 21)

| Evolução no backend | Impacto no frontend |
|---------------------|---------------------|
| Paginação | Controles nas listagens (curto prazo) |
| Soft delete / auditoria | Colunas ou filtros se expostos na API |
| Notas, frequência, professores | Novas features no padrão dos Documentos 24 e 28 |
| OAuth2 / JWT | Guards, login e interceptor de token |

---

# 7. Fora de escopo

- Detalhe de implementação de cada caso de teste;
- Alteração da pirâmide de testes do backend (Documento 15);
- Redefinição do escopo funcional das telas do MVP (já coberto nos Documentos 24–28).

---

# 8. Série de documentos do frontend

Com este documento, a série fica completa:

| Doc | Conteúdo |
|-----|----------|
| [23](23%20-%20Arquitetura%20do%20Frontend.md) | Arquitetura (Angular, features, HttpClient, estado local) |
| [24](24%20-%20Navegação,%20Telas%20e%20Fluxos.md) | Navegação, telas e fluxos |
| [25](25%20-%20Layout,%20Design%20System%20e%20UX.md) | Layout, design system e UX |
| [26](26%20-%20Estados%20da%20Interface%20e%20Erros.md) | Estados da interface e erros |
| [27](27%20-%20Comunicação%20com%20a%20API.md) | Comunicação com a API |
| [28](28%20-%20Componentização%20e%20Estrutura%20do%20Projeto.md) | Componentização e estrutura |
| **29** | Testes e roadmap (este documento) |

---

# 9. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 15 — Estratégia de Testes | Pirâmide e princípios |
| 17 — Pipeline CI/CD | Extensão com job frontend |
| 20 — Segurança | Auth UI no longo prazo |
| 21 — Plano de Evolução | Evoluções que puxam UI |
| 23–28 | Arquitetura e escopo da SPA |
| 25 — Layout / UX | A11y mínima agora; avançada no médio prazo |

---

# 10. Considerações finais

Com os Documentos **23–29** aprovados, a especificação do frontend do MVP está completa para implementação. Mudanças na profundidade de testes ou na ordem do roadmap devem atualizar este documento.
