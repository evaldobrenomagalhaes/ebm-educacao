# Documento 21 — Plano de Evolução do Sistema

> Versão: 1.0
>
> Fase: Engenharia de Software
>
> Status: Aprovado

---

# 1. Objetivo

Este documento apresenta o plano de evolução arquitetural do Sistema Acadêmico.

Seu objetivo é registrar as principais funcionalidades e melhorias previstas para futuras versões da aplicação, demonstrando que a arquitetura foi projetada para permitir crescimento contínuo sem comprometer a qualidade do software.

As evoluções descritas neste documento não fazem parte do escopo inicial do projeto, mas orientam decisões arquiteturais tomadas desde a fase de análise.

---

# 2. Princípios de Evolução

A evolução do sistema seguirá os seguintes princípios:

- Evolução incremental;
- Baixo acoplamento;
- Compatibilidade com a arquitetura existente;
- Preservação das regras de negócio;
- Evitar reescritas desnecessárias.

Cada nova funcionalidade deverá respeitar os princípios definidos pelos documentos arquiteturais anteriores.

---

# 3. Escopo da Primeira Versão (MVP)

A primeira versão contempla:

- CRUD de alunos (incluindo `ListarAlunos` com filtros avançados);
- CRUD de cursos (incluindo `ListarCursos` com filtros avançados);
- CRUD de disciplinas (incluindo `ListarDisciplinas` com filtros avançados);
- CRUD de períodos letivos (incluindo `ListarPeriodosLetivos` com filtros avançados de texto, situação e data);
- CRUD de turmas (incluindo `ListarTurmas` com filtros avançados e `ConsultarTurmasDisponiveis`);
- Realizar, confirmar e cancelar matrícula;
- Consultar matrículas por aluno e por turma (com filtros avançados de status e vínculos indiretos);
- Controle de vagas;
- Aplicação das regras de negócio do domínio;
- API REST;
- Persistência com JPA;
- **Documentação da API com OpenAPI/Swagger**;
- **Docker Compose** para ambiente local (PostgreSQL + backend + frontend) via `docker compose up`;
- Testes automatizados;
- Pipeline de Integração Contínua.

Essas funcionalidades representam o núcleo do domínio acadêmico.

---

# 4. Evoluções Planejadas

As funcionalidades futuras estão organizadas por prioridade.

---

## 4.1 Curto Prazo

Melhorias previstas para as próximas versões.

- **Paginação** nas listagens (`Listar*` / `Consultar*`);
- Auditoria de alterações;
- Soft Delete.

Os **filtros avançados** por entidade (Aluno, Curso, Disciplina, Período Letivo, Turma — inclusive `ConsultarTurmasDisponiveis` — e Matrícula) já estão no MVP; inventário no Documento 09, §4.5. Não há atributo de data em Aluno, Curso, Disciplina, Turma ou Matrícula no Documento 04 (datas naturais só em Período Letivo); datas de auditoria ficam fora do domínio.

---

## 4.2 Médio Prazo

Funcionalidades que ampliam o domínio.

- Controle de notas;
- Controle de frequência;
- Histórico escolar;
- Professores;
- Lançamento de avaliações;
- Fechamento do período letivo;
- Emissão de documentos acadêmicos.

Ao introduzir novos módulos de domínio (ex.: Notas, Frequência), reavaliar a organização de pacotes conforme **ADR-001** (Documento 22): a estrutura atual por camada pode migrar para organização por módulo de negócio.

---

## 4.3 Longo Prazo

Funcionalidades voltadas para escalabilidade e integração.

- Mensageria (RabbitMQ ou Apache Kafka);
- Cache distribuído;
- Notificações por e-mail;
- Integração com sistemas externos;
- Dashboard gerencial;
- Versionamento da API;
- Multi-tenancy.

---

# 5. Evolução da Arquitetura

A arquitetura foi planejada para permitir futuras evoluções.

Exemplos:

- Novos Casos de Uso;
- Novos Aggregates;
- Novos Eventos de Domínio;
- Novos Adaptadores;
- Novos mecanismos de autenticação.

Essas evoluções deverão ocorrer sem alterações significativas no domínio.

---

# 6. Evolução Tecnológica

A arquitetura também prevê evolução tecnológica.

Possíveis melhorias:

- Atualização de versões do Java;
- Atualização do Spring Boot;
- Evolução do banco de dados;
- Integração com ferramentas de observabilidade;
- Deploy em ambientes de nuvem.

A atualização de tecnologias deverá preservar a estabilidade da aplicação.

---

# 7. Evolução da Qualidade

A estratégia de qualidade poderá incorporar:

- SonarQube;
- Mutation Testing;
- Testes End-to-End;
- Testes de Performance;
- Testes de Segurança Automatizados;
- Monitoramento contínuo da qualidade.

---

# 8. Evolução da Segurança

Recursos planejados (alinhados ao Documento 20 — segurança avançada fora do MVP Junior):

- Spring Security + JWT;
- Hash de senhas com BCrypt;
- Refresh Token;
- MFA (Autenticação Multifator);
- OAuth2/OpenID Connect;
- Controle granular de permissões;
- Rate Limiting;
- Auditoria de acessos.

---

# 9. Evolução da Infraestrutura

O **Docker Compose local** (banco + backend + frontend) faz parte do MVP e não é evolução.

Possíveis evoluções de infraestrutura:

- Publicação de imagens em registry (Docker Hub, GHCR, etc.);
- Deploy automatizado (CD);
- Kubernetes;
- Observabilidade;
- Centralização de logs;
- Monitoramento com Prometheus e Grafana.

Esses recursos serão incorporados conforme a necessidade do projeto.

---

# 10. Roadmap

```text
Versão 1.0

↓

MVP

↓

Versão 1.1

• Paginação
• Auditoria
• Soft Delete

↓

Versão 1.2

• Notas
• Frequência
• Histórico

↓

Versão 2.0

• Mensageria
• Cache
• Dashboard
• Integrações
```

---

# 11. Decisões de Arquitetura

## DA-046 — Evolução incremental

### Decisão

A aplicação será evoluída por incrementos pequenos e independentes.

### Justificativa

Reduz riscos e facilita validações contínuas.

---

## DA-047 — Domínio estável

### Decisão

As novas funcionalidades deverão preservar as regras do domínio existente.

### Justificativa

Evita regressões e reduz impacto arquitetural.

---

## DA-048 — Evolução por extensão

### Decisão

Sempre que possível, novas funcionalidades serão adicionadas por extensão e não por modificação de componentes consolidados.

### Justificativa

Segue o Princípio Aberto/Fechado (Open/Closed Principle).

---

## DA-049 — Arquitetura preparada para crescimento

### Decisão

A arquitetura deverá suportar a inclusão de novos módulos sem necessidade de reorganização estrutural.

### Justificativa

Facilita a evolução contínua do sistema.

---

# 12. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 10 | Arquitetura Hexagonal |
| Documento 15 | Estrutura do Projeto |
| Documento 16 | Pipeline CI/CD |
| Documento 17 | Qualidade de Código |
| Documento 18 | Convenções do Projeto |
| Documento 19 | Segurança |

---

# 13. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Refactoring*
- Sam Newman — *Building Microservices*

---

# 14. Considerações Finais

O Sistema Acadêmico foi concebido para evoluir de forma incremental, preservando a simplicidade da primeira versão e permitindo a incorporação de novas funcionalidades conforme a necessidade.

As decisões arquiteturais registradas ao longo desta documentação fornecem uma base sólida para crescimento sustentável, reduzindo riscos de acoplamento excessivo e facilitando a manutenção da aplicação.

---

# 15. Próximos Passos

Com este documento, conclui-se a série numerada da documentação de domínio e engenharia (Documentos 00–21).

O **Documento 22 — Registro de Decisões Arquiteturais (ADR)** complementa a arquitetura com ADRs formais (ex.: ADR-001 sobre organização de pacotes).

A entrega final da documentação do projeto está no `README.md` (visão geral, execução e índice dos documentos).