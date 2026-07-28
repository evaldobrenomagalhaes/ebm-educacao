# Frontend — Sistema Acadêmico (EBM Edu)

SPA Angular (standalone + Material) que consome a API REST em `http://localhost:8080`.

Documentação: Documentos 23–29 em `docs/`.

## Desenvolvimento

```bash
npm install
npm start
```

A aplicação sobe em [http://localhost:4200](http://localhost:4200) e chama a API em `http://localhost:8080` (CORS liberado no profile `dev`).

## Build de produção (Docker)

```bash
docker build -t ebm-edu-frontend .
```

No Compose do repositório (`docker compose up`), o serviço `frontend` publica a porta **4200** → nginx (**80**), com fallback SPA (`try_files`). A URL da API no browser permanece `http://localhost:8080`.
