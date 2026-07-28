# Documento 25 — Layout, Design System e UX

> Versão: 1.0
>
> Fase: UX
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define o layout, a identidade visual, os componentes base, a responsividade, a acessibilidade mínima e o movimento (transições e animações) do frontend Angular do Sistema Acadêmico.

O design system do MVP é **prático e coerente**, alinhado à arquitetura ([Documento 23](23%20-%20Arquitetura%20do%20Frontend.md)) e à navegação ([Documento 24](24%20-%20Navegação,%20Telas%20e%20Fluxos.md)). Não se trata de um kit completo de produto.

---

# 2. Decisões

| # | Decisão | Escolha |
|---|---------|---------|
| 1 | Biblioteca de UI | **Angular Material** |
| 2 | Shell | **Sidebar** (desktop) + **drawer** (mobile) |
| 3 | Tema | **Laranja institucional** elegante/moderno |
| 4 | Responsividade / a11y | **Mínimo sólido** |
| 5 | Movimento | Transições e microanimações **cuidadas** (sem ruído) |

---

# 3. Shell da aplicação

```text
┌──────────────┬─────────────────────────────────────┐
│ Brand        │  Título da página                   │
│ EBM Edu      │─────────────────────────────────────│
│              │                                     │
│ Navegação    │  Conteúdo (lista / detalhe / form / │
│ (Doc 24)     │   dashboard)                        │
│              │                                     │
└──────────────┴─────────────────────────────────────┘
```

- **Desktop (≥ ~960px):** sidebar fixa à esquerda.
- **Abaixo do breakpoint:** sidebar vira **drawer** (Angular Material); botão de menu na barra superior da área de conteúdo.
- A área de conteúdo deve ter largura máxima confortável, evitando formulários excessivamente esticados em monitores ultra-wide.

---

# 4. Identidade visual (tema)

## 4.1 Princípios

- Visual **elegante e moderno**: espaço útil bem distribuído, hierarquia tipográfica clara, pouco ornamento.
- Laranja como **cor de ação e ênfase**, não como fundo de página.
- Sem dark mode global no MVP (a sidebar escura faz parte do shell, não de um tema escuro da aplicação inteira).
- Cores semânticas (erro / sucesso / aviso) **não** reutilizam o laranja primary.

## 4.2 Tokens (referência)

| Token | Uso | Valor de referência |
|-------|-----|---------------------|
| Primary | CTA, item ativo do menu, ênfase de marca | Laranja queimado ≈ `#E65100` |
| On-primary | Texto sobre primary | `#FFFFFF` |
| Sidebar bg | Shell | ≈ `#1C1C1E` |
| Sidebar texto | Navegação | claro / muted |
| Sidebar ativo | Indicador + texto | primary |
| Surface | Fundo do conteúdo | ≈ `#F7F7F5` / `#FAFAFA` |
| On-surface | Texto principal | ≈ `#212121` |
| Secondary | Ações secundárias | cinza neutro |
| Error / Success / Warn | Feedback | palette Material padrão |

Os valores exatos podem ser ajustados na implementação do tema Angular Material, desde que se preservem o contraste adequado e o caráter **laranja queimado + shell escuro + superfície clara**.

## 4.3 Tipografia e elevação

- Tipografia do tema Angular Material (legível; sem fonte display exagerada).
- Cards do dashboard: elevação leve.
- Listas e tabelas: preferir superfície plana com divisão sutil — evitar “card em tudo”.

---

# 5. Componentes base (Material)

| Necessidade (Documento 24) | Componente típico |
|----------------------------|-------------------|
| Menu / drawer | `mat-sidenav` |
| Listagens | `mat-table` + filtros em form fields |
| Formulários | Reactive Forms + `mat-form-field` |
| Confirmação (excluir) | `mat-dialog` |
| Feedback rápido | `mat-snack-bar` |
| Ações | `mat-button` / `mat-flat-button` (primary) |
| Status (matrícula, turma) | `mat-chip` ou badge textual |

Padrões de estado da interface (loading, vazio, erro) → [Documento 26](26%20-%20Estados%20da%20Interface%20e%20Erros.md).

---

# 6. Movimento (transições e animações)

O movimento faz parte da identidade visual: deve parecer **preciso e discreto**, reforçando a sensação elegante/moderna, sem ruído.

## 6.1 Princípios

- Duração curta: tipicamente **150–300 ms** (diálogos e drawer até ~400 ms).
- Easing suave (ease-out / curvas padrão Material); sem bounce exagerado.
- Preferir **opacidade + deslocamento leve** (8–16 px) ou altura; evitar giros e efeitos de glow.
- Respeitar `prefers-reduced-motion`: reduzir ou desligar animações não essenciais.

## 6.2 Onde aplicar (MVP)

| Momento | Comportamento esperado |
|---------|------------------------|
| Abrir/fechar drawer | Transição Material do sidenav |
| Troca de rota | Fade/slide leve do conteúdo (opcional, porém consistente) |
| Cards do dashboard | Entrada escalonada sutil (stagger curto) |
| Snackbar | Entrada/saída padrão Material |
| Dialog | Abertura/fechamento padrão Material |
| Hover em botões/linhas | Transição rápida de cor ou elevação |
| Item ativo do menu | Indicação clara (barra/cor) com transição de cor suave |

## 6.3 O que evitar

- Animações longas que atrasem formulários ou tabelas;
- Parallax, partículas e loaders elaborados;
- Animar cada célula em listagens grandes.

---

# 7. Responsividade (mínimo sólido)

- Breakpoint principal ≈ **960px** (sidebar ↔ drawer).
- Tabelas: scroll horizontal em viewport estreito; não ocultar colunas críticas sem alternativa.
- Formulários: uma coluna no mobile; até duas no desktop quando fizer sentido.
- Alvos de toque com área clicável adequada (padrão dos botões Material).

---

# 8. Acessibilidade (mínimo sólido)

- Todo campo de formulário com **label** visível.
- Foco de teclado visível (tema Material / outline).
- Ícones apenas com ação: `aria-label` (ex.: editar, excluir, menu).
- Contraste: texto e botões primary com contraste adequado; ajustar o tom do laranja se o teste falhar.
- Não transmitir informação somente por cor (status com texto + chip).
- Honrar `prefers-reduced-motion` (§6.1).

Aprofundamento WCAG formal permanece no roadmap ([Documento 29](29%20-%20Testes%20e%20Roadmap%20do%20Frontend.md)).

---

# 9. Conteúdo e linguagem

- Labels na **linguagem ubíqua** ([Documento 02](02%20-%20Linguagem%20Ubíqua.md) e Documento 24).
- Botões de ação com verbo claro: Salvar, Confirmar, Cancelar, Abrir turma, etc.
- Marca **EBM Edu** visível na sidebar (não apenas no título da aba do navegador).

---

# 10. Fora de escopo

- Design system documentado em Storybook;
- Dark mode completo da aplicação;
- Superfícies de marketing / hero (o shell é administrativo);
- Detalhe de loading, empty e erro → **Documento 26**;
- Estrutura de pastas do tema e componentes shared → **[Documento 28](28%20-%20Componentização%20e%20Estrutura%20do%20Projeto.md)**.

---

# 11. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 02 — Linguagem Ubíqua | Textos na UI |
| 23 — Arquitetura do Frontend | Angular; UI kit na implementação |
| 24 — Navegação, Telas e Fluxos | Menu, telas, dashboard |
| 26 — Estados da Interface e Erros | Loading, vazio, ProblemDetail |
| 28 — Componentização e Estrutura | Onde vivem tema e shared |
| 29 — Testes e Roadmap | A11y avançada e evoluções visuais |

---

# 12. Considerações finais

Mudanças de biblioteca de UI, do shell (sidebar/topo) ou da identidade (primary fora do laranja institucional) devem atualizar este documento e, se forem decisões estruturais, o [Documento 22 — ADR](22%20-%20Registro%20de%20Decisões%20Arquiteturais%20(ADR).md).
