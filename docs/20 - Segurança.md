# Documento 20 — Segurança

> Versão: 1.0
>
> Fase: Engenharia de Software
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define as diretrizes de segurança adotadas pelo Sistema Acadêmico.

Seu objetivo é estabelecer princípios para proteger a aplicação, seus dados e seus usuários, garantindo que os requisitos de segurança sejam considerados desde a fase de arquitetura.

As decisões apresentadas seguem o princípio de **Security by Design**, incorporando segurança como parte integrante do desenvolvimento da aplicação.

---

# 2. Princípios

A arquitetura seguirá os seguintes princípios:

- Menor privilégio possível (Principle of Least Privilege);
- Defesa em profundidade (Defense in Depth);
- Falha segura (Fail Secure);
- Validação de entradas;
- Proteção de dados sensíveis;
- Autenticação antes da autorização.

Esses princípios deverão orientar todas as decisões de implementação.

---

# 3. Autenticação

O desafio Junior **não exige** autenticação na versão 1.0. A API do MVP poderá operar sem login enquanto os fluxos obrigatórios forem validados.

Quando a autenticação for introduzida (evolução), o acesso exigirá autenticação dos usuários, preferencialmente com **JWT (JSON Web Token)** integrado ao **Spring Security**.

Fluxo simplificado (evolução):

```text
Usuário

↓

Login

↓

Validação das credenciais

↓

Geração do JWT

↓

Requisições autenticadas

↓

Validação do Token
```

Na versão 1.0, permanecem obrigatórios: validação de entrada, tratamento centralizado de exceções e mensagens sem exposição de detalhes internos.
---

# 4. Autorização

Após autenticado (quando a autenticação existir), o usuário poderá acessar apenas os recursos compatíveis com seu perfil.

Exemplos de perfis (evolução):

- Administrador;
- Secretaria;
- Professor;
- Aluno.

A autorização será baseada em papéis (Role-Based Access Control - RBAC).

Na versão 1.0 Junior, autorização por perfil **não é obrigatória**.

---

# 5. Proteção de Senhas

Quando existirem usuários autenticados (evolução), as senhas nunca serão armazenadas em texto puro.

Diretrizes:

- Hash utilizando BCrypt;
- Nunca registrar senhas em logs;
- Nunca retornar senhas em respostas da API;
- Nunca persistir senhas sem criptografia.

Na versão 1.0 Junior, sem autenticação obrigatória, este item permanece como diretriz para a evolução.

---

# 6. Validação de Entrada

Toda entrada de dados deverá ser validada.

As validações ocorrerão em diferentes camadas.

## Camada de Apresentação

Responsável por validar com **Jakarta Bean Validation** (`@Valid`, `@NotNull`, `@Size`, etc. nos request DTOs / records):

- campos obrigatórios;
- formato dos dados;
- tamanho máximo e mínimo;
- tipos de dados.

Violações são traduzidas para HTTP 400 com corpo **RFC 7807 `ProblemDetail`**.

---

## Camada de Domínio

Responsável por validar:

- regras de negócio;
- invariantes;
- consistência das entidades.

Essa separação evita duplicidade de responsabilidades.

---

# 7. Proteção contra Vulnerabilidades

A aplicação deverá adotar medidas para reduzir riscos conhecidos.

Entre elas:

- prevenção de SQL Injection por meio do JPA e consultas parametrizadas;
- prevenção de Cross-Site Scripting (XSS) quando aplicável;
- proteção contra Cross-Site Request Forgery (CSRF), conforme o tipo de autenticação adotado;
- validação de entradas;
- tratamento adequado de exceções;
- **CORS** no MVP liberando a origin do frontend (`http://localhost:4200`); em `prod`, restringir às origins reais.

---

# 8. Tratamento de Erros

As mensagens retornadas ao cliente não deverão expor informações internas da aplicação. O contrato de erro da API utiliza **RFC 7807 `ProblemDetail`** (Documento 14).

Exemplo:

✔ Correto

```text
Turma não encontrada.
```

✘ Incorreto

```text
org.hibernate.ObjectNotFoundException...
```

Informações técnicas detalhadas deverão permanecer apenas nos logs.

---

# 9. Proteção de Dados Sensíveis

Os seguintes dados deverão receber tratamento especial:

- senhas;
- tokens;
- dados pessoais;
- informações de autenticação.

Esses dados nunca deverão ser expostos em respostas da API ou registros de log.

---

# 10. Logs

Os registros de log utilizarão **SLF4J** (implementação padrão do Spring Boot: Logback) e deverão conter apenas informações necessárias para auditoria e diagnóstico.

Não deverão ser registrados:

- senhas;
- tokens JWT;
- informações sensíveis dos usuários.

Cada log deverá possuir nível adequado:

| Nível | Finalidade |
|--------|------------|
| ERROR | Falhas inesperadas |
| WARN | Situações anormais |
| INFO | Eventos relevantes |
| DEBUG | Diagnóstico durante desenvolvimento |

---

# 11. Dependências

As dependências do projeto deverão ser mantidas atualizadas.

Boas práticas:

- utilizar versões estáveis;
- acompanhar correções de segurança;
- remover bibliotecas não utilizadas;
- revisar vulnerabilidades periodicamente.

---

# 12. Segurança da API

A API deverá seguir as seguintes diretrizes:

- utilização exclusiva de HTTPS em ambientes de produção;
- autenticação obrigatória para recursos protegidos;
- respostas padronizadas;
- tratamento centralizado de exceções;
- validação de entrada em todos os endpoints.

---

# 13. Evoluções Planejadas

As seguintes funcionalidades de segurança **não fazem parte da versão 1.0 Junior** e poderão ser incorporadas futuramente:

- Spring Security + JWT (primeira camada de autenticação);
- Hash de senhas com BCrypt (quando houver usuários autenticados);
- Refresh Token;
- MFA (Autenticação Multifator);
- Auditoria de acessos;
- Rate Limiting;
- Bloqueio temporário após tentativas de login malsucedidas;
- Integração com provedores OAuth2/OpenID Connect;
- Controle granular de permissões (RBAC completo).

Isso alinha o Documento 20 ao Documento 16 (pacote `security` reservado para evolução) e à expectativa de simplicidade do desafio técnico.
---

# 14. Decisões de Arquitetura

## DA-041 — Spring Security como mecanismo de autenticação (evolução)

### Decisão

Quando a autenticação for introduzida, será implementada utilizando Spring Security.

### Justificativa

É a solução padrão do ecossistema Spring, amplamente adotada e integrada ao framework. Fica fora do MVP Junior para priorizar clareza e execução dos fluxos de matrícula.

---

## DA-042 — JWT para autenticação (evolução)

### Decisão

As requisições autenticadas utilizarão JSON Web Tokens (JWT), em versão posterior à 1.0.

### Justificativa

Permite uma arquitetura stateless, adequada para APIs REST, sem bloquear a entrega mínima do desafio.
---

## DA-043 — BCrypt para armazenamento de senhas (evolução)

### Decisão

Quando houver usuários com senha, as senhas serão armazenadas utilizando BCrypt.

### Justificativa

BCrypt é um algoritmo amplamente recomendado para armazenamento seguro de senhas.
---

## DA-044 — Segurança em camadas

### Decisão

As validações serão distribuídas entre apresentação, aplicação e domínio.

### Justificativa

Cada camada será responsável apenas pelas validações compatíveis com sua função.

---

## DA-045 — Mensagens seguras

### Decisão

A aplicação não exporá detalhes internos em mensagens retornadas aos clientes. Erros da API usam **RFC 7807 `ProblemDetail`**.

### Justificativa

Reduz o risco de vazamento de informações sensíveis e padroniza o contrato de erro.

---

## DA-052 — Jakarta Bean Validation na API

### Decisão

A validação de entrada nos endpoints usará **Jakarta Bean Validation** nos request DTOs / records.

### Justificativa

Integração nativa com Spring MVC (`@Valid`) e separação clara entre validação sintática (apresentação) e regras de negócio (domínio).

---

## DA-053 — CORS liberado para o frontend no MVP

### Decisão

No profile `dev` / MVP, a API libera CORS para `http://localhost:4200`. Em `prod`, as origins devem ser restritas.

### Justificativa

Permite que o frontend no Compose consuma a API localmente sem autenticação no MVP Junior.

---

# 15. Relação com os Documentos Anteriores

| Documento | Contribuição |
|-----------|--------------|
| Documento 07 | Invariantes do Domínio |
| Documento 10 | Arquitetura Hexagonal |
| Documento 13 | Persistência |
| Documento 14 | Tratamento de Exceções / ProblemDetail |
| Documento 18 | Qualidade de Código |
| Documento 19 | Convenções do Projeto |
| Documento 22 | ADR-002 (stack e decisões técnicas) |

---

# 16. Referências

Este documento foi elaborado com base nas seguintes obras e referências:

- OWASP Top 10
- OWASP Application Security Verification Standard (ASVS)
- Spring Security Reference Documentation
- RFC 7519 — JSON Web Token (JWT)
- RFC 7807 — Problem Details for HTTP APIs
- Jakarta Bean Validation Specification
- Robert C. Martin — *Clean Architecture*

---

# 17. Considerações Finais

A segurança será tratada como um requisito arquitetural e não apenas como uma preocupação da fase de implementação.

As decisões apresentadas estabelecem uma base sólida para proteger a aplicação, preservar a integridade das informações e permitir a evolução futura para mecanismos de segurança mais avançados.

---

# 18. Próximos Passos

Este documento servirá como base para:

- Documento 21 — Plano de Evolução do Sistema;
- Documento 22 — Registro de Decisões Arquiteturais (ADR);
- `README.md` — visão geral e entrega final da documentação do projeto.