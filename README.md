# Sistema Acadêmico

API REST para gestão acadêmica (matrículas, turmas, cursos e alunos), com **DDD** e **Arquitetura Hexagonal**.

## Stack (ADR-002)

| Tecnologia | Versão |
|------------|--------|
| Java | 21 LTS |
| Spring Boot | 3.5.x |
| PostgreSQL | 18 |
| Maven | 3.9+ |
| Flyway | migrações de schema |
| Docker Compose | ambiente local |

## Como rodar

```bash
docker compose up
```

API em `http://localhost:8080` · Swagger em `http://localhost:8080/swagger-ui.html`.

Backend com Maven (banco via Compose):

```bash
docker compose up db -d
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Testes / qualidade:

```bash
mvn verify
```

## Documentação

A documentação completa do projeto (domínio, casos de uso, arquitetura, ADRs e guia detalhado) está em **[docs/README.md](docs/README.md)**.
