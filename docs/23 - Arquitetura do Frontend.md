# Documento 23 — Arquitetura do Frontend

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a arquitetura do **módulo frontend** do Sistema Acadêmico.

O frontend é uma SPA que consome a API REST já modelada no backend, permitindo operar os casos de uso do MVP (CRUD, matrícula, consultas e controle de vagas) por meio de uma interface web.

A UI **não reimplementa regras de domínio**: validações de negócio permanecem no backend (Documentos 06, 09 e 14). O frontend orquestra telas, formulários, chamadas HTTP e feedback ao usuário.

---

# 2. Stack tecnológica

| Tecnologia | Observação |
|------------|------------|
| Angular | SPA em TypeScript (versão estável atual na implementação) |
| TypeScript | Tipagem estática |
| Angular Router | Navegação entre telas |
| Angular HttpClient | Comunicação com a API |
| Angular Reactive Forms | Formulários de cadastro/edição |
| RxJS | Fluxos assíncronos (via Angular) |
| Docker / nginx | Imagem de produção do frontend no Compose |
| Porta | **4200** (host → container `80`, conforme ADR-002) |

A API permanece em `http://localhost:8080`. O contrato HTTP é documentado via **springdoc-openapi**; o frontend não gera cliente automaticamente no MVP.

Detalhamento de URLs, CORS e erros: Documentos 26 e 27. Estrutura de pastas: Documento 28.

Decisão consolidada: [Documento 22 — ADR-004](22%20-%20Registro%20de%20Decisões%20Arquiteturais%20(ADR).md).

---

# 3. Motivação

O backend já entrega o núcleo do domínio via API. O frontend existe para:

- Tornar os fluxos do MVP operáveis sem depender só do Swagger;
- Completar o ambiente Docker Compose (`db` + `backend` + `frontend`);
- Demonstrar integração full stack alinhada à linguagem ubíqua e aos casos de uso (Documento 09).

A arquitetura prioriza **simplicidade**, **rastreabilidade** (tela → caso de uso → endpoint) e **baixo acoplamento** com detalhes de infraestrutura além do contrato HTTP.

---

# 4. Decisões arquiteturais

## 4.1 Framework — Angular

Adota-se **Angular** como framework da SPA.

**Motivos:** porta `4200` já fixada no Compose; ecossistema opinativo (router, HTTP, forms); encaixa bem em stacks Java/Spring; reduz decisões soltas no MVP.

**Alternativas:** React + Vite; Vue + Vite — descartadas para o MVP em favor de menor carga de escolha de libs.

## 4.2 Organização — por feature

O código organiza-se **por feature de domínio** (aluno, curso, disciplina, período letivo, turma, matrícula), com pastas transversais:

- `core/` — configuração, base HTTP, interceptors;
- `shared/` — componentes e utilitários reutilizáveis;
- `layout/` — shell da aplicação (menu, área de conteúdo).

**Alternativa:** pastas só por tipo técnico (`components/`, `services/`) — rejeitada por dispersar o domínio e dificultar manutenção.

## 4.3 Integração — HttpClient + services manuais

Cada feature expõe um **service** que encapsula chamadas `HttpClient` aos endpoints da API. DTOs/tipos no frontend espelham o contrato da API.

**OpenAPI** permanece a fonte de consulta do contrato (`/v3/api-docs` / Swagger UI). **Geração automática de cliente** fica fora do MVP (roadmap no Documento 29).

## 4.4 Estado — local por tela/feature

Listagens, formulários, loading e erros de operação ficam no **estado local** do componente (ou service da feature, quando ajudar a coesão).

Não se adota store global (NgRx ou similar) no MVP. Feedback transversal de erro HTTP (ex.: toast/banner) pode viver em `core/` sem store completo.

---

# 5. Responsabilidades do frontend

O frontend **deve**:

- Renderizar telas e fluxos do MVP;
- Coletar entrada do usuário e enviar commands/queries à API;
- Exibir loading, vazio, sucesso e erro de forma consistente;
- Respeitar CORS e a base URL configurada por ambiente;
- Usar a linguagem ubíqua nas labels e rotas (Documento 02).

O frontend **não deve**:

- Duplicar invariantes de domínio (ex.: “turma deve estar aberta”, “não há vagas”);
- Persistir regras de negócio só no browser;
- Introduzir autenticação/autorização no MVP (Documento 20);
- Inventar entidades ou casos de uso fora do Documento 21, §3.

---

# 6. Visão em camadas (SPA)

```text
┌─────────────────────────────────────┐
│  Layout / Rotas                     │
├─────────────────────────────────────┤
│  Features (páginas + forms)         │
├─────────────────────────────────────┤
│  Services da feature (HttpClient)   │
├─────────────────────────────────────┤
│  core/ (base URL, interceptor)      │
└─────────────────────────────────────┘
                 │
                 ▼
         API REST (Spring Boot)
```

Dependências apontam para dentro da feature e para `core/` / `shared/`. Features não devem depender umas das outras sem necessidade explícita.

---

# 7. Ambiente e entrega

| Item | Decisão |
|------|---------|
| Dev local | `ng serve` na porta 4200 (ou equivalente do scaffold) |
| Compose | Build estático servido por **nginx**; host `4200:80` |
| API no browser | Base URL configurável (ex.: `environment`); no Compose, apontar para o backend |
| Auth MVP | Ausente — todas as telas acessíveis |

---

# 8. Série de documentos do frontend

O frontend é especificado em sete documentos, a partir deste:

| Doc | Conteúdo |
|-----|----------|
| **23** | Arquitetura do Frontend (este documento) |
| **24** | [Navegação, Telas e Fluxos](24%20-%20Navegação,%20Telas%20e%20Fluxos.md) |
| **25** | [Layout, Design System e UX](25%20-%20Layout,%20Design%20System%20e%20UX.md) |
| **26** | [Estados da Interface e Erros](26%20-%20Estados%20da%20Interface%20e%20Erros.md) |
| **27** | [Comunicação com a API](27%20-%20Comunicação%20com%20a%20API.md) |
| **28** | [Componentização e Estrutura do Projeto](28%20-%20Componentização%20e%20Estrutura%20do%20Projeto.md) |
| **29** | [Testes e Roadmap do Frontend](29%20-%20Testes%20e%20Roadmap%20do%20Frontend.md) |

---

# 9. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 09 — Casos de Uso | Escopo funcional das telas e fluxos |
| 14 — Exceções | Contrato de erro no backend; UI trata no Documento 26 |
| 16 / README | Pasta `frontend/`, Compose |
| 20 — Segurança | CORS `localhost:4200`; sem Security no MVP |
| 21 — Evolução | Escopo MVP; o que não entra na UI agora |
| 22 — ADR | ADR-002 (porta); ADR-004 (stack e organização do frontend) |
| 24–29 | Navegação, UX, estados/erros, API, pastas, testes/roadmap |

---

# 10. Fora de escopo deste documento

- Mapa de rotas e inventário de telas → **Documento 24**
- Visual, responsividade e acessibilidade → **Documento 25**
- Estados de UI e mapeamento de `ProblemDetail` → **Documento 26**
- Detalhe de endpoints e convenções HTTP → **Documento 27**
- Árvore de pastas e naming → **Documento 28**
- Testes e roadmap → **[Documento 29](29%20-%20Testes%20e%20Roadmap%20do%20Frontend.md)**

---

# 11. Considerações finais

A arquitetura do frontend privilegia clareza e alinhamento ao domínio já documentado. Mudanças de framework, organização por pastas, estratégia HTTP ou modelo de estado devem ser registradas em novo ADR e refletidas neste documento.
