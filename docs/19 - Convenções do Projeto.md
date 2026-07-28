# Documento 19 — Convenções do Projeto

> Versão: 1.0
>
> Fase: Engenharia de Software
>
> Status: Aprovado

---

# 1. Objetivo

Este documento estabelece as convenções de desenvolvimento adotadas pelo Sistema Acadêmico.

Seu objetivo é garantir consistência na implementação, facilitar a manutenção do código e reduzir divergências entre diferentes desenvolvedores.

As convenções aqui definidas complementam as decisões arquiteturais e os padrões de qualidade apresentados nos documentos anteriores.

---

# 2. Princípios

Todas as implementações deverão seguir os seguintes princípios:

- Simplicidade;
- Legibilidade;
- Baixo acoplamento;
- Alta coesão;
- Clareza sobre esperteza;
- Consistência em todo o projeto.

Sempre que houver mais de uma solução possível, deverá ser escolhida aquela que apresentar menor complexidade e maior facilidade de manutenção.

---

# 3. Convenções de Nomeação

## Classes

Classes deverão utilizar PascalCase.

Exemplos:

```text
Aluno

Turma

Matricula

ConfirmarMatriculaUseCase

TurmaRepository
```

---

## Interfaces

Interfaces não utilizarão prefixo "I".

Exemplos:

```text
Repository

TurmaRepository

EmailService
```

---

## Métodos

Métodos utilizarão camelCase.

Exemplos:

```java
confirmarMatricula()

cancelar()

abrir()

fechar()
```

Os nomes deverão representar claramente a ação executada.

---

## Variáveis

Utilizar camelCase.

Exemplos:

```java
quantidadeVagas

periodoLetivo

statusMatricula
```

Evitar abreviações desnecessárias.

---

## Constantes

Utilizar letras maiúsculas separadas por underscore.

```java
MAXIMO_VAGAS

STATUS_ATIVO

TEMPO_EXPIRACAO
```

---

# 4. Organização dos Métodos

Os métodos deverão permanecer pequenos e possuir uma única responsabilidade.

Recomenda-se:

- até 30 linhas;
- poucos parâmetros;
- uma única finalidade.

Métodos excessivamente grandes deverão ser refatorados.

---

# 5. Organização das Classes

Cada classe deverá possuir uma responsabilidade claramente definida.

Exemplos:

✔ Turma controla vagas.

✔ Matricula controla seu estado.

✔ Aluno representa um aluno.

Evitar classes que concentrem responsabilidades distintas.

---

# 6. Comentários

Comentários deverão explicar:

- decisões arquiteturais;
- regras complexas;
- justificativas.

Não deverão explicar código evidente.

Ruim:

```java
// Soma um ao contador

contador++;
```

Bom:

```java
// A matrícula somente pode ser confirmada enquanto a turma estiver aberta.
```

---

# 7. Tratamento de Null

Sempre que possível:

- utilizar objetos válidos;
- evitar retornos nulos;
- validar argumentos na entrada.

Value Objects deverão ser imutáveis e sempre consistentes.

---

# 8. Imutabilidade

Sempre que possível:

- atributos `final`;
- objetos imutáveis;
- Value Objects imutáveis;
- coleções não modificáveis quando apropriado.

A imutabilidade reduz efeitos colaterais e facilita os testes.

---

# 9. Uso de Recursos Modernos do Java (Java 21)

O projeto adota Java 21 LTS. Sempre que aumentar a clareza e a segurança do código, preferir recursos modernos da plataforma:

- Preferir `record` para DTOs, Commands e Responses imutáveis;
- Utilizar `sealed class` / `sealed interface` quando a hierarquia de tipos for fechada e conhecida;
- Utilizar `switch` com pattern matching quando aumentar a legibilidade;
- Utilizar `var` apenas quando o tipo for evidente pelo contexto;
- Preferir `List.of()`, `Set.of()` e `Map.of()` para coleções imutáveis;
- Evitar APIs legadas quando existir alternativa moderna equivalente na biblioteca padrão.

Essas práticas complementam DA-039 (Optional) e DA-040 (imutabilidade).

---

# 10. Uso de Optional

O `Optional` será utilizado apenas como tipo de retorno.

Não deverá ser utilizado:

- em atributos;
- como parâmetro de métodos;
- em entidades JPA.

Essa prática segue as recomendações da equipe responsável pelo Java.

---

# 11. Exceções

As exceções deverão representar situações excepcionais.

Não utilizar exceções para controle de fluxo.

Sempre preferir exceções específicas do domínio.

---

# 12. Logging

Logs deverão possuir nível adequado.

| Nível | Utilização |
|--------|------------|
| ERROR | Falhas inesperadas |
| WARN | Situações anormais |
| INFO | Eventos relevantes da aplicação |
| DEBUG | Informações para diagnóstico |

Informações sensíveis nunca deverão ser registradas em logs.

---

# 13. Commits

O projeto adotará o padrão Conventional Commits.

Exemplos:

```text
feat: implementar matrícula

fix: corrigir cálculo de vagas

docs: atualizar Documento 08

refactor: simplificar Aggregate Turma

test: adicionar testes do caso de uso

chore: atualizar dependências

build: ajustar configuração do Maven

ci: adicionar workflow do GitHub Actions
```

---

# 14. Estrutura das Branches

Fluxo simplificado baseado em Git Flow.

```text
main

↓

feature/*

↓

Pull Request

↓

main
```

Cada funcionalidade será desenvolvida em uma branch específica.

---

# 15. Pull Requests

Todo Pull Request deverá conter:

- objetivo da alteração;
- descrição da solução;
- impacto esperado;
- evidências de testes realizados;
- referência ao requisito ou Caso de Uso correspondente.

---

# 16. Revisão de Código

Durante a revisão deverão ser observados:

- aderência ao DDD;
- respeito às invariantes;
- responsabilidade das entidades;
- legibilidade;
- simplicidade;
- cobertura de testes;
- conformidade com os documentos arquiteturais.

---

# 17. Decisões de Arquitetura

## DA-037 — Convenções obrigatórias

### Decisão

Todas as implementações deverão seguir as convenções definidas neste documento.

### Justificativa

Garantir uniformidade e facilitar a manutenção do projeto.

---

## DA-038 — Conventional Commits

### Decisão

O histórico de commits seguirá o padrão Conventional Commits.

### Justificativa

Facilita rastreabilidade, geração de changelogs e organização do histórico.

---

## DA-039 — Optional apenas como retorno

### Decisão

O `Optional` será utilizado exclusivamente como tipo de retorno.

### Justificativa

Evita uso inadequado da API e mantém o código mais simples.

---

## DA-040 — Preferência por imutabilidade

### Decisão

Objetos imutáveis serão priorizados sempre que possível.

### Justificativa

Reduz efeitos colaterais e aumenta a previsibilidade do código.

---

## DA-050 — Recursos modernos do Java 21

### Decisão

A implementação adotará recursos modernos do Java 21 (`record`, `sealed`, pattern matching em `switch`, coleções imutáveis da biblioteca padrão, etc.) conforme a seção 9 deste documento.

### Justificativa

Complementa DA-039 e DA-040: reduz boilerplate, reforça imutabilidade e tipagem fechada, e mantém o código alinhado à plataforma LTS escolhida para o projeto.

---

# 18. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 15 | Estrutura do Projeto |
| Documento 16 | Pipeline CI/CD |
| Documento 17 | Qualidade de Código |

---

# 19. Referências

Este documento foi elaborado com base nas seguintes obras:

- Robert C. Martin — *Clean Code*
- Robert C. Martin — *Clean Architecture*
- Joshua Bloch — *Effective Java (3ª edição)*
- Martin Fowler — *Refactoring*
- Conventional Commits Specification
- Oracle Java Coding Conventions

---

# 20. Considerações Finais

As convenções definidas neste documento estabelecem um padrão único para o desenvolvimento do Sistema Acadêmico.

Sua adoção promove maior legibilidade, reduz divergências entre implementações e contribui para a evolução sustentável da aplicação.

---

# 21. Próximos Passos

Este documento servirá como base para:

- Documento 20 — Segurança;
- Documento 21 — Plano de Evolução do Sistema;
- Documento 22 — Registro de Decisões Arquiteturais (ADR);
- `README.md` — visão geral e entrega final da documentação do projeto.