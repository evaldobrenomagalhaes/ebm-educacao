# Frontend — Sistema Acadêmico (EBM Edu)

SPA Angular (standalone) que consome a API REST em `http://localhost:8080`.

Documentação: Documentos 23–29 em `docs/`.

## Desenvolvimento

```bash
npm install
npm start
```

A aplicação sobe em [http://localhost:4200](http://localhost:4200).

## Build de produção (Docker)

```bash
docker build -t ebm-edu-frontend .
```

No Compose do repositório, o serviço `frontend` publica a porta **4200** → nginx (**80**).
