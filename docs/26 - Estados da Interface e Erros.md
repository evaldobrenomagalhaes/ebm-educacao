# Documento 26 — Estados da Interface e Erros

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento padroniza os estados de interface e o tratamento de erros no frontend Angular do Sistema Acadêmico.

O comportamento descrito alinha-se ao contrato HTTP **RFC 7807 `ProblemDetail`** ([Documento 14](14%20-%20Tratamento%20de%20Exceções%20do%20Domínio.md)) e à UX ([Documento 25](25%20-%20Layout,%20Design%20System%20e%20UX.md)).

A UI **não reimplementa** invariantes de domínio: interpreta a resposta da API e orienta o usuário.

---

# 2. Decisões

| # | Decisão | Escolha |
|---|---------|---------|
| 1 | Erros da API | **Híbrido** (campo + snackbar + empty 404) |
| 2 | Estados de tela | **loading / empty / error / ready** + sucesso via snackbar |
| 3 | Validação no client | **UX mínima** (required/formato); regras de negócio na API |

---

# 3. Estados padrão da interface

Toda lista, detalhe e operação assíncrona deve distinguir:

| Estado | Quando | Comportamento |
|--------|--------|----------------|
| **Loading** | Carregando dados ou submetendo | Spinner/skeleton na região; ações primárias desabilitadas (“Salvando…”) |
| **Empty** | Lista sem resultados | Mensagem clara + CTA (ex.: “Novo aluno”) |
| **Error** | Falha ao carregar a tela | Mensagem + “Tentar novamente” |
| **Ready** | Dados disponíveis | Conteúdo normal |
| **Success** | Mutação bem-sucedida | Snackbar curto + navegação conforme o fluxo ([Documento 24](24%20-%20Navegação,%20Telas%20e%20Fluxos.md)) |

O estado vive na feature/tela (estado local — [Documento 23](23%20-%20Arquitetura%20do%20Frontend.md)). Transições loading → ready devem respeitar o movimento do Documento 25 e `prefers-reduced-motion`.

---

# 4. Contrato de erro (`ProblemDetail`)

O backend expõe, entre outros (Documento 14):

| Situação | HTTP | Exemplos |
|----------|------|----------|
| Validação de entrada | 400 | Bean Validation, payload inválido |
| Não encontrado | 404 | `EntityNotFoundException` |
| Conflito / regra | 409 | `DuplicateMatriculaException`, `SemVagasException` |
| Violação de regra | 422 | `BusinessRuleViolationException` |
| Erro inesperado | 5xx | Sem detalhes internos na UI |

Campos úteis na UI: `status`, `title`, `detail`; extensões de campo quando o backend enviar erros de validação por propriedade.

---

# 5. Tratamento híbrido na UI

## 5.1 Interceptor (`core/`)

- Intercepta respostas HTTP de erro.
- Normaliza `ProblemDetail` (e falha de rede / status 0).
- Encaminha para um serviço de feedback (snackbar) e/ou propaga erro tipado para a feature tratar em formulário/página.

## 5.2 Por tipo de falha

| Tipo | UI |
|------|-----|
| **400** com erros de campo | `mat-error` nos campos + snackbar opcional resumido |
| **409 / 422** | Snackbar (ou dialog leve) com `detail` legível |
| **404** em detalhe | Estado “não encontrado” na página + Voltar à lista |
| **404** em ação pontual | Snackbar + atualizar/voltar |
| **Rede / 5xx** | Snackbar neutro (“Falha de comunicação. Tente novamente.”); sem stack trace |
| **Submit em andamento** | Evitar duplo clique (botão desabilitado) |

Mensagens ao usuário preferem `detail` (ou `title`) do ProblemDetail, em português como já retornado pela API, sem expor payload técnico.

---

# 6. Validação no cliente

**Permitido (UX):**

- Campos obrigatórios;
- Formato básico (ex.: e-mail);
- Consistências triviais de formulário (ex.: data início ≤ data término, se ambos informados).

**Proibido no client (domínio):**

- “Turma deve estar aberta”;
- “Há vagas”;
- “Matrícula duplicada”;
- Demais invariantes dos Documentos 06 e 09.

Esses casos vêm da API e seguem a seção 5.

**Confirmações destrutivas** (Excluir, Cancelar matrícula): `mat-dialog` de confirmação **antes** do request.

---

# 7. Sucesso e feedback positivo

| Ação | Feedback típico |
|------|-----------------|
| Cadastrar / atualizar | Snackbar de sucesso → detalhe ou lista |
| Excluir | Snackbar → lista |
| Confirmar / cancelar matrícula | Snackbar → detalhe atualizado |
| Abrir / fechar turma | Snackbar → detalhe atualizado |

Evitar alertas nativos do browser (`alert` / `confirm`).

---

# 8. Empty e filtros

- Lista sem cadastros: empty com CTA de criação.
- Lista com filtros sem match: empty diferenciado (“Nenhum resultado para os filtros”) + ação para limpar filtros.
- Bloco de matrículas no detalhe do aluno/turma: empty local (“Nenhuma matrícula”) + atalho “Nova matrícula” (Documento 24).

---

# 9. Fora de escopo

- Retry automático agressivo ou fila offline;
- Tradução i18n de mensagens;
- Página global única de erro para todos os casos (exceto 404 de detalhe);
- Mapeamento fino de cada endpoint → **[Documento 27](27%20-%20Comunicação%20com%20a%20API.md)**.

---

# 10. Relação com outros documentos

| Documento | Relação |
|-----------|---------|
| 06 / 09 | Invariantes e casos de uso (não duplicar na UI) |
| 14 — Exceções | ProblemDetail e status HTTP |
| 23 — Arquitetura do Frontend | Interceptor em `core/`; estado local |
| 24 — Navegação, Telas e Fluxos | Fluxos após sucesso/erro |
| 25 — Layout, Design System e UX | Snackbar, dialog, movimento |
| 27 — Comunicação com a API | Cliente HTTP e convenções |

---

# 11. Considerações finais

Alterações no padrão de estados ou na estratégia híbrida de erros devem atualizar este documento. Mudanças no contrato `ProblemDetail` do backend devem ser refletidas aqui e no Documento 14.
