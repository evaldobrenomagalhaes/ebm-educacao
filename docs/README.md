# Sistema Acadêmico

> Projeto desenvolvido para demonstrar a aplicação de Domain-Driven Design (DDD), Arquitetura Hexagonal e boas práticas de engenharia de software na construção de um sistema acadêmico.
>
> Entrega alinhada ao desafio técnico (Desenvolvedor Júnior Full Stack — Tribe Lyceum / Techne).

---

# Sobre o Projeto

O Sistema Acadêmico é uma API REST desenvolvida em **Java 21** e **Spring Boot 3**, cujo objetivo é gerenciar o processo de matrícula de alunos em turmas, respeitando regras de negócio típicas de instituições de ensino.

Mais do que implementar funcionalidades, este projeto busca demonstrar decisões arquiteturais fundamentadas em literatura e práticas consolidadas do mercado.

---

# Objetivos

- Aplicar Domain-Driven Design (DDD);
- Utilizar Arquitetura Hexagonal;
- Implementar um Modelo de Domínio Rico (Rich Domain Model);
- Demonstrar boas práticas de engenharia de software;
- Construir uma base preparada para evolução contínua;
- Atender ao escopo obrigatório do desafio técnico (CRUD, matrícula, vagas, consultas e README executável).

---

# Tecnologias

| Tecnologia | Versão / Observação |
|------------|---------------------|
| Java | 21 LTS |
| Spring Boot | 3.x |
| Spring Data JPA | 3.x |
| Hibernate | 6.x |
| Maven | 3.9+ |
| PostgreSQL | Banco relacional da versão 1.0 |
| Docker / Docker Compose | Ambiente local (banco + backend + frontend) |
| OpenAPI / Swagger | Documentação interativa da API (MVP) |
| JUnit 5 | Testes |
| Mockito | Testes |
| Testcontainers | Testes de integração |
| GitHub Actions | CI |
| JaCoCo | Cobertura |
| Checkstyle | Qualidade |
| SpotBugs | Análise estática |
| PMD | Qualidade |
| Frontend | A especificar (Angular, TypeScript/JavaScript ou equivalente) |

---

# Arquitetura

O projeto utiliza uma combinação de padrões arquiteturais amplamente adotados no mercado.

- Domain-Driven Design (DDD);
- Arquitetura Hexagonal (Ports and Adapters);
- Clean Architecture (princípios);
- SOLID;
- Repository Pattern;
- Domain Events;
- Aggregate Roots;
- Value Objects.

---

# Diagramas

Diagramas derivados da documentação de domínio e arquitetura (Documentos 04, 08, 09 e 10).

## Arquitetura Hexagonal

Dependências apontam para o domínio; a infraestrutura implementa as ports.

```mermaid
flowchart TB
  subgraph infra["Infrastructure"]
    CTRL[Controllers / Web]
    JPA[Adapters JPA / Eventos]
  end

  subgraph app["Application"]
    UC[Casos de Uso]
  end

  subgraph dom["Domain"]
    MODEL[model / Aggregates]
    PORT[Ports - Repositories]
    EVT[Domain Events]
  end

  CTRL --> UC
  UC --> MODEL
  UC --> PORT
  MODEL --> EVT
  JPA -.->|implementa| PORT
  UC --> JPA
```

## Diagrama de classes do domínio

Relacionamentos conceituais (Documento 04, §10).

```mermaid
classDiagram
  class Curso {
    +id
    +nome
  }
  class Disciplina {
    +id
    +nome
  }
  class PeriodoLetivo {
    +id
    +codigo
    +situacao
  }
  class Turma {
    +id
    +vagas
    +situacao
  }
  class Aluno {
    +id
    +nome
  }
  class Matricula {
    +id
    +status
  }

  Curso "1" --> "*" Disciplina : oferece
  Disciplina "1" --> "*" Turma : ofertada em
  PeriodoLetivo "1" --> "*" Turma : contém
  Aluno "1" --> "*" Matricula : realiza
  Turma "1" --> "*" Matricula : recebe
```

## Sequência — Realizar Matrícula

Fluxo conceitual: Controller → Use Case → Aggregate → Repository → Evento (Documentos 09 e 08).

```mermaid
sequenceDiagram
  actor Usuario
  participant Controller
  participant RealizarMatricula as RealizarMatriculaUseCase
  participant Matricula as Matricula Aggregate
  participant Repo as MatriculaRepository
  participant Evento as MatriculaRealizada

  Usuario->>Controller: Solicitar matrícula
  Controller->>RealizarMatricula: execute(command)
  RealizarMatricula->>Matricula: criar / validar regras
  Matricula-->>RealizarMatricula: matrícula PENDENTE
  RealizarMatricula->>Repo: salvar(matricula)
  RealizarMatricula->>Evento: publicar
  RealizarMatricula-->>Controller: resultado
  Controller-->>Usuario: resposta
```

## Fluxo de Domain Events

Após confirmação ou cancelamento, consumidores reagem de forma desacoplada (Documentos 08 e 10).

```mermaid
flowchart LR
  MC[MatriculaConfirmada] --> AUD[Auditoria]
  MC --> NOT[Notificação]
  MX[MatriculaCancelada] --> AUD
  MX --> NOT
```

---

# Estrutura do Projeto

```text
.
├── docker-compose.yml
├── Dockerfile                 # backend
├── frontend/                  # a especificar (+ Dockerfile)
├── docs/
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    └── test/
```

A organização detalhada está descrita no **Documento 16 — Estrutura do Projeto**.

---

# Funcionalidades da Versão 1.0

- CRUD de alunos (com listagem filtrada);
- CRUD de cursos (com listagem filtrada);
- CRUD de disciplinas (com listagem filtrada);
- CRUD de períodos letivos (com listagem filtrada por texto, situação e data);
- CRUD de turmas (com listagem filtrada e consulta de turmas disponíveis);
- Realizar, confirmar e cancelar matrícula;
- Consultar matrículas por aluno e por turma (com filtros avançados);
- Controle de vagas;
- Validação das regras de negócio;
- Documentação da API com OpenAPI/Swagger.

---

# Casos de Uso

- CRUD de Aluno (Cadastrar, Atualizar, Listar com filtros avançados, Buscar por Id, Excluir);
- CRUD de Curso (idem, com filtros avançados na listagem);
- CRUD de Disciplina (idem, com filtros avançados na listagem);
- CRUD de Período Letivo (Cadastrar, Atualizar, Listar com filtros avançados, Buscar por Id, Excluir);
- CRUD de Turma (idem, com filtros avançados; inclui ConsultarTurmasDisponiveis);
- Realizar Matrícula;
- Confirmar Matrícula;
- Cancelar Matrícula;
- Consultar Matrículas por Aluno (com filtros avançados);
- Consultar Matrículas por Turma (com filtros avançados).

Detalhamento: [Documento 09 — Casos de Uso](09%20-%20Casos%20de%20Uso.md).

---

# Como Rodar Localmente

A forma padrão de subir o ambiente é o **Docker Compose**: banco (PostgreSQL), backend e frontend com um único comando.

## Pré-requisitos

- Docker Desktop (ou Docker Engine) com **Docker Compose**
- Para desenvolvimento fora do Compose (opcional): Java 21 e Maven 3.9+

## Clonar o projeto

```bash
git clone <url-do-repositorio>
cd ebm-edu
```

## Executar com Docker Compose (padrão)

```bash
docker compose up
```

Úteis:

```bash
docker compose up -d    # em segundo plano
docker compose down     # encerra e remove os containers
```

Serviços previstos no `docker-compose.yml`:

| Serviço | Função | Porta (planejada) |
|---------|--------|-------------------|
| `db` | PostgreSQL | `5432` |
| `backend` | API Spring Boot | `8080` |
| `frontend` | UI (a especificar) | `4200` (ou a definida no Compose) |

Após o Compose subir:

- API: `http://localhost:8080`
- Swagger UI (OpenAPI): `http://localhost:8080/swagger-ui.html` (caminho final conforme springdoc/springfox na implementação)
- Frontend: URL do serviço `frontend` (quando o módulo estiver especificado)

Variáveis de conexão (`POSTGRES_*`, `SPRING_DATASOURCE_*`, URLs entre serviços) ficam no `docker-compose.yml` / `.env` do repositório. O backend usa o hostname do serviço `db` dentro da rede do Compose (não `localhost`).

> Enquanto o frontend não estiver especificado, o serviço pode existir como placeholder no Compose; os fluxos da API podem ser validados via HTTP client (curl, Postman, Insomnia).

## Alternativa: API com Maven (desenvolvimento)

Se preferir rodar só o backend na máquina host (com o banco já disponível, por exemplo via Compose só do serviço `db`):

```bash
docker compose up db -d
mvn spring-boot:run
```

Ajuste `application.properties` / `application.yml` para apontar ao PostgreSQL (exemplo):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ebm_edu
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

## Executar os testes

```bash
mvn test
```

## Gerar cobertura

```bash
mvn verify
```

---

# Endpoints e Telas Principais

## API REST (planejada)

| Recurso | Operações |
|---------|-----------|
| `/api/alunos` | CRUD |
| `/api/cursos` | CRUD |
| `/api/disciplinas` | CRUD |
| `/api/periodos-letivos` | Cadastro / consulta |
| `/api/turmas` | CRUD |
| `/api/matriculas` | Realizar, confirmar, cancelar |
| `/api/alunos/{id}/matriculas` | Consultar matrículas por aluno |
| `/api/turmas/{id}/matriculas` | Consultar matrículas por turma |

Os caminhos finais poderão ser refinados na implementação; a lista acima cobre os fluxos obrigatórios do desafio.

## Frontend

Frontend simples para consumir os principais fluxos da API — **a especificar** em documento dedicado. Enquanto isso, os fluxos podem ser validados via **Swagger UI** (OpenAPI) ou HTTP client (curl, Postman, Insomnia).

---

# Como Testar Manualmente o Fluxo de Matrícula

1. Cadastre um **Curso** e uma **Disciplina**.
2. Cadastre um **Período Letivo** (ex.: `2026.1`).
3. Cadastre uma **Turma** vinculada à disciplina/período, com limite de vagas (ex.: `2`) e status **aberta**.
4. Cadastre um **Aluno**.
5. **Realize** a matrícula do aluno na turma → status inicial `PENDENTE`.
6. **Confirme** a matrícula → status `CONFIRMADA` e uma vaga da turma é consumida.
7. (Opcional) **Cancele** a matrícula confirmada → status `CANCELADA` e a vaga é liberada.
8. Consulte matrículas por aluno e por turma para validar o resultado.

---

# Como Validar a Regra de Limite de Vagas

1. Crie uma turma com `vagas = 1`.
2. Realize e confirme a primeira matrícula → deve consumir a única vaga.
3. Tente confirmar uma segunda matrícula na mesma turma → a operação deve falhar (turma sem vagas).
4. Cancele a matrícula confirmada → a vaga deve ser liberada.
5. Confirme novamente uma matrícula pendente (ou realize + confirme outra) → deve suceder após a liberação.

Também valide:

- matrícula apenas em turma **aberta**;
- impedimento de **duplicidade** (mesmo aluno na mesma turma).

---

# Decisões Simples de Implementação

- Regras de matrícula e de vagas ficam no **domínio** (entidades/aggregates), não nos controllers.
- Persistência relacional com **JPA/Hibernate** sobre **PostgreSQL**.
- Ambiente local via **Docker Compose** (`db` + `backend` + `frontend`) com `docker compose up`.
- Documentação da API com **OpenAPI/Swagger** no MVP (Swagger UI para explorar e testar endpoints).
- Separação em camadas alinhada à Arquitetura Hexagonal (ports/adapters).
- Status de matrícula: `PENDENTE`, `CONFIRMADA`, `CANCELADA`.
- Autenticação JWT / Spring Security ficam como **evolução** (não bloqueiam o MVP Junior).
- Frontend será especificado em documento dedicado; o serviço já está previsto no Compose.

---

# Limitações Conhecidas

- Código de backend/frontend ainda em construção; este README descreve o contrato de entrega e a documentação de domínio.
- Frontend ainda não especificado em documento dedicado (serviço previsto no Compose).
- Soft delete e auditoria estão no plano de evolução (diferenciais).
- Publicação de imagens em registry e deploy automatizado (CD) são evolução — o MVP usa Compose **local**.
- Segurança avançada (JWT, RBAC, OAuth2) não faz parte do escopo mínimo da versão 1.0 Junior.
- Período Letivo faz parte do modelo acadêmico do projeto, mas o **fechamento** do período é evolução (médio prazo).
- Badges de build/cobertura, screenshot do pipeline, GIF da aplicação e coleção Postman ficam para depois da implementação (dependem de artefato real em execução).

---

# Uso de IA

O uso de ferramentas de IA é permitido pelo desafio e deve ser informado na entrega.

| Item | Conteúdo |
|------|----------|
| Ferramentas | Cursor (agente de documentação/código) — complementar conforme a implementação |
| Partes assistidas | Modelagem documental (casos de uso, domínio, README); trechos de código a registrar na implementação |
| Revisado manualmente | Regras de matrícula, limite de vagas, escopo MVP vs evolução, alinhamento ao desafio técnico |
| Trechos críticos | Confirmação/cancelamento de matrícula e consumo/liberação de vagas; invariantes de duplicidade e turma aberta |

Atualize esta seção ao finalizar a implementação, listando arquivos/fluxos concretos revisados sem auxílio automático.

---

# Qualidade

O projeto utiliza:

- Testes Unitários;
- Testes de Integração;
- JaCoCo;
- Checkstyle;
- SpotBugs;
- PMD;
- GitHub Actions.

---

# Segurança (versão 1.0 vs evolução)

**Na versão 1.0 (MVP Junior):**

- Validação de entrada;
- Tratamento centralizado de exceções;
- Mensagens de erro sem exposição de detalhes internos.

**Evolução (não obrigatório para o desafio):**

- Spring Security;
- JWT;
- BCrypt;
- RBAC e OAuth2.

Detalhamento: [Documento 20 — Segurança](20%20-%20Segurança.md) e [Documento 21 — Plano de Evolução](21%20-%20Plano%20de%20Evolução%20do%20Sistema.md).

---

# Documentação

Toda a modelagem arquitetural encontra-se em `docs/`. Índice alinhado aos arquivos reais:

| Doc | Arquivo | Conteúdo |
|-----|---------|----------|
| 00 | [00 - Visão do Domínio.md](00%20-%20Visão%20do%20Domínio.md) | Visão do domínio e regras iniciais |
| 01 | [01 - Princípios de Arquitetura e Modelagem.md](01%20-%20Princípios%20de%20Arquitetura%20e%20Modelagem.md) | Princípios de arquitetura e modelagem |
| 02 | [02 - Linguagem Ubíqua.md](02%20-%20Linguagem%20Ubíqua.md) | Linguagem ubíqua |
| 03 | [03 - Modelo do Domínio.md](03%20-%20Modelo%20do%20Domínio.md) | Modelo do domínio |
| 04 | [04 - Entidades do Domínio.md](04%20-%20Entidades%20do%20Domínio.md) | Entidades |
| 05 | [05 - Value Objects.md](05%20-%20Value%20Objects.md) | Value Objects |
| 06 | [06 - Invariantes do Domínio.md](06%20-%20Invariantes%20do%20Domínio.md) | Invariantes |
| 07 | [07 - Aggregate Roots.md](07%20-%20Aggregate%20Roots.md) | Aggregate Roots |
| 08 | [08 - Eventos de Domínio.md](08%20-%20Eventos%20de%20Domínio.md) | Eventos de domínio |
| 09 | [09 - Casos de Uso.md](09%20-%20Casos%20de%20Uso.md) | Casos de uso |
| 10 | [10 - Arquitetura Hexagonal.md](10%20-%20Arquitetura%20Hexagonal.md) | Arquitetura hexagonal |
| 11 | [11 - Repositórios (Ports e Adapters).md](11%20-%20Repositórios%20(Ports%20e%20Adapters).md) | Repositórios (ports e adapters) |
| 13 | [13 - Persistência com JPA.md](13%20-%20Persistência%20com%20JPA.md) | Persistência com JPA |
| 14 | [14 - Tratamento de Exceções do Domínio.md](14%20-%20Tratamento%20de%20Exceções%20do%20Domínio.md) | Tratamento de exceções |
| 15 | [15 - Estratégia de Testes.md](15%20-%20Estratégia%20de%20Testes.md) | Estratégia de testes |
| 16 | [16 - Estrutura do Projeto.md](16%20-%20Estrutura%20do%20Projeto.md) | Estrutura do projeto |
| 17 | [17 - Pipeline CI_CD.md](17%20-%20Pipeline%20CI_CD.md) | Pipeline CI/CD |
| 18 | [18 - Qualidade de Código.md](18%20-%20Qualidade%20de%20Código.md) | Qualidade de código |
| 19 | [19 - Convenções do Projeto.md](19%20-%20Convenções%20do%20Projeto.md) | Convenções |
| 20 | [20 - Segurança.md](20%20-%20Segurança.md) | Segurança |
| 21 | [21 - Plano de Evolução do Sistema.md](21%20-%20Plano%20de%20Evolução%20do%20Sistema.md) | Plano de evolução |
| 22 | [22 - Registro de Decisões Arquiteturais (ADR).md](22%20-%20Registro%20de%20Decisões%20Arquiteturais%20(ADR).md) | ADR |

> Não há Documento 12 numerado no conjunto atual; o arquivo de repositórios é o Documento 11.

---

# Estrutura de Branches

```text
main
  ↓
feature/*
  ↓
Pull Request
  ↓
main
```

---

# Convenção de Commits

O projeto utiliza **Conventional Commits**.

Exemplos:

```text
feat:
fix:
docs:
refactor:
test:
ci:
build:
chore:
```

---

# Roadmap

## Versão 1.0

- MVP (CRUD, matrícula, consultas, vagas)
- Docker Compose (PostgreSQL + backend + frontend)
- OpenAPI / Swagger

## Versão 1.1

- Auditoria
- Soft Delete
- Paginação

## Versão 1.2

- Notas
- Frequência
- Histórico Escolar

## Versão 2.0

- Mensageria
- Dashboard
- Publicação de imagens / deploy automatizado
- Kubernetes
- OAuth2 / JWT

---

# Referências

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Patterns of Enterprise Application Architecture*
- Alistair Cockburn — *Hexagonal Architecture*
- Documento do desafio técnico — Tribe Lyceum / Techne

---

# Licença

Projeto desenvolvido exclusivamente para fins de estudo, prática e demonstração de arquitetura de software.
