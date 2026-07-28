# Documento 18 — Qualidade de Código

> Versão: 1.0
>
> Fase: Engenharia de Software
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estratégia de qualidade de código adotada pelo Sistema Acadêmico.

Seu objetivo é estabelecer padrões que garantam um código legível, consistente, seguro e de fácil manutenção ao longo da evolução do projeto.

A estratégia contempla padronização de código, análise estática, métricas e cobertura de testes, integrando-se ao Pipeline de Integração Contínua.

---

# 2. Motivação

A qualidade do código influencia diretamente:

- Facilidade de manutenção;
- Redução de defeitos;
- Legibilidade;
- Facilidade de evolução;
- Confiabilidade da aplicação.

A adoção de ferramentas automatizadas reduz a dependência de revisões manuais e aumenta a consistência do projeto.

---

# 3. Objetivos

A estratégia de qualidade busca garantir que:

- O código siga um padrão único de desenvolvimento;
- Erros comuns sejam identificados antes da execução;
- Más práticas sejam detectadas automaticamente;
- O domínio permaneça simples e coeso;
- O projeto evolua de forma sustentável.

---

# 4. Ferramentas Adotadas

| Ferramenta | Objetivo |
|------------|----------|
| Checkstyle | Padronização do código |
| SpotBugs | Detecção de possíveis defeitos |
| PMD | Identificação de más práticas |
| JaCoCo | Cobertura de testes |
| Maven Enforcer | Validação do ambiente de build |

---

# 5. Checkstyle

O Checkstyle será responsável por validar a padronização do código-fonte.

Entre os principais critérios estão:

- Indentação consistente;
- Organização dos imports;
- Nomeação de classes, métodos e atributos;
- Limite de tamanho para métodos;
- Limite de tamanho para classes;
- Organização do código.

O objetivo é tornar o código uniforme em todo o projeto.

---

# 6. SpotBugs

O SpotBugs realizará análise estática para identificar possíveis problemas.

Exemplos:

- Null Pointer Exception;
- Comparações incorretas;
- Objetos não inicializados;
- Recursos não fechados;
- Problemas de concorrência;
- Código potencialmente inseguro.

Essas verificações ocorrem sem executar a aplicação.

---

# 7. PMD

O PMD será utilizado para identificar problemas relacionados à qualidade do código.

Exemplos:

- Código duplicado;
- Métodos excessivamente longos;
- Classes com muitas responsabilidades;
- Complexidade ciclomática elevada;
- Variáveis não utilizadas;
- Código morto.

O objetivo é incentivar um design simples e de fácil manutenção.

---

# 8. Cobertura de Testes

A cobertura será monitorada através do JaCoCo.

Metas iniciais:

| Camada | Cobertura mínima |
|----------|----------------:|
| Domínio | 90% |
| Aplicação | 85% |
| Infraestrutura | Conforme necessidade |

A cobertura será utilizada como indicador de qualidade, não como objetivo absoluto.

---

# 9. Revisão de Código

Além das verificações automáticas, todas as alterações deverão passar por revisão de código.

Durante a revisão serão avaliados aspectos como:

- Clareza da implementação;
- Coesão;
- Acoplamento;
- Legibilidade;
- Nomeação;
- Simplicidade;
- Aderência à arquitetura definida.

---

# 10. Métricas

Serão acompanhadas as seguintes métricas:

| Métrica | Objetivo |
|----------|----------|
| Cobertura de testes | Avaliar proteção do código |
| Complexidade | Identificar pontos críticos |
| Duplicação | Evitar repetição de código |
| Violações do Checkstyle | Garantir padronização |
| Violações do PMD | Melhorar o design |
| Alertas do SpotBugs | Detectar defeitos potenciais |

---

# 11. Integração com o Pipeline

As ferramentas de qualidade serão executadas automaticamente durante o Pipeline de Integração Contínua.

Fluxo:

```text
Compilação

↓

Testes

↓

JaCoCo

↓

Checkstyle

↓

SpotBugs

↓

PMD

↓

Build
```

Caso qualquer etapa obrigatória falhe, o pipeline será interrompido.

---

# 12. Boas Práticas

Durante o desenvolvimento deverão ser observadas as seguintes diretrizes:

- Métodos pequenos e coesos;
- Classes com responsabilidade única;
- Evitar duplicação de código;
- Preferir composição à herança;
- Utilizar nomes claros e expressivos;
- Evitar números mágicos;
- Priorizar imutabilidade sempre que possível;
- Manter baixo acoplamento entre componentes.

Essas práticas complementam as validações automatizadas.

---

# 13. Decisões de Arquitetura

## DA-033 — Qualidade validada automaticamente

### Decisão

Toda alteração será analisada pelas ferramentas de qualidade durante o pipeline.

### Justificativa

Reduz falhas e aumenta a consistência do código.

---

## DA-034 — Cobertura como indicador

### Decisão

A cobertura de testes será utilizada como métrica de apoio, não como único indicador de qualidade.

### Justificativa

Um alto percentual de cobertura não garante testes de qualidade.

---

## DA-035 — Revisão de código obrigatória

### Decisão

Alterações deverão ser submetidas à revisão antes da integração.

### Justificativa

Complementa as verificações automatizadas e promove compartilhamento de conhecimento.

---

## DA-036 — Simplicidade como princípio

### Decisão

A solução mais simples que atenda aos requisitos deverá ser priorizada.

### Justificativa

Reduz complexidade desnecessária e facilita a evolução do projeto.

---

# 14. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 14 | Estratégia de Testes |
| Documento 15 | Estrutura do Projeto |
| Documento 16 | Pipeline CI/CD |

---

# 15. Referências

Este documento foi elaborado com base nas seguintes obras:

- Robert C. Martin — *Clean Code*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Refactoring*
- SonarSource — *Clean as You Code*
- Checkstyle Documentation
- SpotBugs Documentation
- PMD Documentation
- JaCoCo Documentation

---

# 16. Considerações Finais

A qualidade do código será tratada como parte integrante da arquitetura do projeto.

A combinação de padrões de desenvolvimento, análise estática, cobertura de testes e revisão de código contribui para um sistema mais confiável, de fácil manutenção e preparado para evoluções futuras.

---

# 17. Próximos Passos

Este documento servirá como base para:

- Documento 19 — Convenções do Projeto;
- Documento 20 — Segurança;
- Documento 21 — Plano de Evolução do Sistema.