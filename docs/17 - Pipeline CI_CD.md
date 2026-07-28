# Documento 17 — Pipeline CI/CD

> Versão: 1.0
>
> Fase: Engenharia de Software
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define a estratégia de Integração Contínua (Continuous Integration - CI) adotada pelo Sistema Acadêmico.

Seu objetivo é automatizar as principais verificações do projeto, garantindo que toda alteração submetida ao repositório seja compilada, validada e testada antes de ser integrada à branch principal.

Nesta primeira versão do projeto será implementado apenas o pipeline de **Continuous Integration (CI)**.

A etapa de **Continuous Delivery (CD)** poderá ser incorporada em futuras evoluções.

---

# 2. Motivação

A automação do processo de integração proporciona diversos benefícios:

- Redução de erros humanos;
- Validação automática da aplicação;
- Execução contínua dos testes;
- Garantia de qualidade antes da integração;
- Feedback rápido durante o desenvolvimento.

---

# 3. Objetivos do Pipeline

O pipeline deverá validar automaticamente:

- Compilação do projeto;
- Execução dos testes automatizados;
- Cobertura de código;
- Qualidade do código;
- Geração dos artefatos.

Nenhuma alteração deverá ser integrada sem passar por essas verificações.

---

# 4. Ferramentas Utilizadas

| Ferramenta | Finalidade |
|------------|------------|
| GitHub Actions | Orquestração do pipeline |
| Maven | Build do projeto |
| JUnit 5 | Execução dos testes |
| Mockito | Testes unitários |
| JaCoCo | Cobertura de código |
| Checkstyle | Padronização do código |
| SpotBugs | Análise estática |
| PMD | Análise de qualidade |

---

# 5. Fluxo do Pipeline

O pipeline seguirá o fluxo abaixo.

```text
Push

↓

Checkout do Código

↓

Configuração do Java

↓

Cache do Maven

↓

Compilação

↓

Testes Unitários

↓

Testes de Integração

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

↓

Publicação dos Relatórios
```

---

# 6. Eventos que Disparam o Pipeline

O pipeline será executado automaticamente nos seguintes eventos.

- Push para a branch principal;
- Push para branches de desenvolvimento;
- Abertura de Pull Request;
- Atualização de Pull Request.

Essa estratégia garante que todas as alterações sejam validadas antes da integração.

---

# 7. Estrutura do Pipeline

A configuração será mantida no repositório.

```text
.github

└── workflows

    └── ci.yml
```

Essa organização segue o padrão adotado pelo GitHub Actions.

---

# 8. Etapas do Pipeline

## 8.1 Checkout

Obtém a versão mais recente do código.

---

## 8.2 Configuração do Ambiente

- Java 21 (ou a versão definida para o projeto);
- Maven;
- Cache de dependências.

---

## 8.3 Compilação

Executa a compilação completa da aplicação.

Objetivo:

Garantir que todo o projeto possa ser compilado.

---

## 8.4 Testes Unitários

Executa toda a suíte de testes unitários.

Caso qualquer teste falhe, o pipeline será interrompido.

---

## 8.5 Testes de Integração

Executa os testes que validam:

- Persistência;
- Repositórios;
- Integração com infraestrutura.

---

## 8.6 Cobertura de Código

O JaCoCo será responsável por gerar os relatórios de cobertura.

Esses relatórios permitirão acompanhar a evolução da qualidade dos testes.

---

## 8.7 Qualidade de Código

Serão executadas ferramentas de análise estática.

- Checkstyle;
- SpotBugs;
- PMD.

Essas verificações ajudam a identificar problemas antes da execução da aplicação.

---

## 8.8 Build

Após todas as verificações, será gerado o artefato da aplicação.

A geração do build indica que o projeto encontra-se apto para distribuição.

---

# 9. Critérios para Aprovação

Uma execução será considerada bem-sucedida quando:

- Compilação concluída;
- Todos os testes aprovados;
- Cobertura gerada;
- Checkstyle sem erros;
- SpotBugs sem problemas críticos;
- PMD sem violações bloqueantes.

Caso qualquer etapa falhe, a integração deverá ser interrompida.

---

# 10. Evolução Planejada

A arquitetura do pipeline foi planejada para permitir futuras evoluções.

Exemplos:

- Deploy automático;
- Publicação de imagens Docker;
- Publicação em ambientes de homologação;
- Publicação em produção;
- Integração com SonarQube;
- Publicação automática de releases.

Essas funcionalidades não fazem parte da primeira versão do projeto.

---

# 11. Decisões de Arquitetura

## DA-029 — Integração Contínua obrigatória

### Decisão

Toda alteração enviada ao repositório deverá passar pelo pipeline.

### Justificativa

Garante estabilidade da branch principal.

---

## DA-030 — Build somente após testes

### Decisão

A geração do artefato ocorrerá apenas após a aprovação de todos os testes.

### Justificativa

Evita distribuir versões inconsistentes.

---

## DA-031 — Qualidade validada automaticamente

### Decisão

As ferramentas de análise estática serão executadas automaticamente.

### Justificativa

Padroniza o código e reduz defeitos.

---

## DA-032 — Pipeline preparado para evolução

### Decisão

A estrutura do pipeline permitirá futura adoção de Continuous Delivery.

### Justificativa

Facilita a evolução do processo de entrega sem necessidade de reestruturação.

---

# 12. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 12 | Persistência |
| Documento 13 | Tratamento de Exceções |
| Documento 14 | Estratégia de Testes |
| Documento 15 | Estrutura do Projeto |

---

# 13. Referências

Este documento foi elaborado com base nas seguintes referências:

- Martin Fowler — *Continuous Integration*
- Jez Humble e David Farley — *Continuous Delivery*
- GitHub Actions Documentation
- Maven Documentation
- JaCoCo Documentation
- Checkstyle Documentation
- SpotBugs Documentation
- PMD Documentation

---

# 14. Considerações Finais

A adoção de um pipeline de Integração Contínua automatiza as principais verificações do projeto e reduz significativamente o risco de integração de código com defeitos.

Além de aumentar a confiabilidade da aplicação, o pipeline estabelece uma base sólida para futuras evoluções, como entrega contínua, automação de deploy e integração com ferramentas de monitoramento da qualidade.

---

# 15. Próximos Passos

Este documento servirá como base para:

- Documento 18 — Qualidade de Código;
- Documento 19 — Convenções do Projeto;
- Documento 20 — Segurança.