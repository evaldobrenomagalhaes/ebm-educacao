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

API em `http://localhost:8080` · Swagger em `http://localhost:8080/swagger-ui.html` · Frontend (Angular/nginx) em `http://localhost:4200`.

No profile `dev`, o Flyway aplica também a carga de cenários de teste (`db/testdata`). Roteiro completo: [docs/30 - Roteiro de Testes Manuais.md](docs/30%20-%20Roteiro%20de%20Testes%20Manuais.md).

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
