# Documento 08 — Eventos de Domínio

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento identifica os **Eventos de Domínio (Domain Events)** do Sistema Acadêmico.

Seu objetivo é registrar os acontecimentos relevantes do domínio que poderão ser utilizados para notificar outras partes da aplicação, mantendo baixo acoplamento entre os componentes e permitindo a evolução do sistema sem alterar as regras centrais do negócio.

Os Eventos de Domínio representam fatos que já ocorreram e não comandos ou intenções de execução.

---

# 2. Conceito de Evento de Domínio

Segundo Eric Evans, um **Evento de Domínio** representa algo importante que aconteceu dentro do domínio e que pode interessar a outros elementos da aplicação.

Eventos representam fatos consumados.

Exemplos:

- Matrícula confirmada;
- Matrícula cancelada;
- Turma fechada.

Os eventos não executam regras de negócio.

Eles apenas registram que determinado fato ocorreu, permitindo que outros componentes reajam a esse acontecimento.

---

# 3. Quando Utilizar Eventos de Domínio

Eventos de Domínio deverão ser utilizados quando:

- Um fato relevante do domínio precisar ser comunicado;
- Outros componentes precisarem reagir ao mesmo acontecimento;
- Deseja-se reduzir o acoplamento entre diferentes partes da aplicação;
- Novas funcionalidades puderem ser adicionadas sem alterar o domínio principal.

Eventos não substituem Casos de Uso nem métodos das entidades.

As regras de negócio continuam sendo executadas pelos Aggregates.

---

# 4. Eventos Identificados

Após a análise do domínio foram identificados os seguintes eventos.

| Evento | Descrição |
|----------|-----------|
| MatriculaRealizada | Uma nova matrícula foi registrada (caso de uso `RealizarMatricula`) |
| MatriculaConfirmada | Uma matrícula foi confirmada |
| MatriculaCancelada | Uma matrícula foi cancelada |
| TurmaAberta | Uma turma foi disponibilizada para matrícula |
| TurmaFechada | Uma turma deixou de aceitar novas matrículas |

> Nome canônico: **MatriculaRealizada**. O sinônimo `MatriculaCriada` não deve ser utilizado.

Todos os nomes representam fatos ocorridos e, por isso, são expressos no particípio.

---

# 5. Fluxo Geral dos Eventos

O fluxo esperado durante uma operação do domínio é o seguinte:

```text
Caso de Uso

↓

Carrega os Aggregates necessários

↓

Executa as regras de negócio

↓

Persiste as alterações

↓

Publica Evento(s) de Domínio

↓

Consumidores executam ações secundárias
```

Essa sequência garante que os eventos sejam publicados somente após a conclusão bem-sucedida das operações do domínio.

---

# 6. Eventos Identificados

## 6.1 MatriculaRealizada

### Descrição

Representa o registro de uma nova matrícula no sistema (fato correspondente ao caso de uso `RealizarMatricula`).

### Publicado por

Aggregate **Matrícula**.

### Possíveis consumidores

- Auditoria;
- Registro de logs.

---

## 6.2 MatriculaConfirmada

### Descrição

Representa a confirmação de uma matrícula.

### Publicado por

Aggregate **Matrícula**.

### Possíveis consumidores

- Serviço de Notificação;
- Auditoria;
- Integrações futuras.

### Observação

O consumo de vagas da turma **não ocorre por meio deste evento**.

Essa operação faz parte do próprio Caso de Uso e ocorre antes da publicação do evento.

---

## 6.3 MatriculaCancelada

### Descrição

Representa o cancelamento de uma matrícula.

### Publicado por

Aggregate **Matrícula**.

### Possíveis consumidores

- Serviço de Notificação;
- Auditoria.

### Observação

A liberação da vaga ocorre durante a execução do Caso de Uso e não por meio do evento.

---

## 6.4 TurmaAberta

### Descrição

Representa a abertura de uma turma para novas matrículas.

### Publicado por

Aggregate **Turma**.

### Possíveis consumidores

- Auditoria.

---

## 6.5 TurmaFechada

### Descrição

Representa o encerramento das matrículas de uma turma.

### Publicado por

Aggregate **Turma**.

### Possíveis consumidores

- Auditoria;
- Serviço de Notificação.

---

# 7. Publicadores e Consumidores

| Evento | Publicador | Consumidores |
|---------|------------|--------------|
| MatriculaRealizada | Matrícula | Auditoria |
| MatriculaConfirmada | Matrícula | Auditoria, Notificação |
| MatriculaCancelada | Matrícula | Auditoria, Notificação |
| TurmaAberta | Turma | Auditoria |
| TurmaFechada | Turma | Auditoria, Notificação |

Os consumidores não conhecem quem publicou o evento.

Essa separação reduz o acoplamento entre os componentes do domínio.

---

# 8. Benefícios da Utilização de Eventos

A utilização de Eventos de Domínio proporciona diversos benefícios.

- Baixo acoplamento;
- Maior extensibilidade;
- Facilidade para adicionar novas funcionalidades;
- Melhor separação de responsabilidades;
- Maior aderência aos princípios do Domain-Driven Design.

Esses benefícios tornam o domínio mais preparado para futuras evoluções.

---

# 9. Decisões de Modelagem

## MD-009 — Eventos representam fatos do domínio

### Decisão

Todos os Eventos de Domínio representarão acontecimentos já concluídos.

### Justificativa

Essa convenção está alinhada à literatura de Domain-Driven Design e facilita a compreensão do modelo.

---

## MD-010 — Eventos não executam regras de negócio

### Decisão

As regras de negócio permanecerão nos Aggregates e nos Casos de Uso.

### Justificativa

Eventos têm finalidade exclusivamente notificadora.

Essa separação evita dependências implícitas entre componentes.

---

## MD-011 — Eventos serão publicados após operações bem-sucedidas

### Decisão

Os Eventos de Domínio somente serão publicados após a conclusão da transação responsável pela alteração do domínio.

### Justificativa

Evita a publicação de eventos referentes a operações que não foram efetivamente persistidas.

---

## MD-012 — Novos consumidores poderão ser adicionados sem alterar o domínio

### Decisão

Novas funcionalidades deverão ser implementadas por meio de novos consumidores de eventos.

### Justificativa

Essa estratégia reduz alterações no núcleo do domínio e favorece o princípio **Open/Closed**.

---

# 10. Exemplo de Fluxo

A confirmação de uma matrícula deverá seguir o fluxo conceitual abaixo.

```text
Aluno

↓

Solicita matrícula

↓

Caso de Uso

↓

Carrega Matrícula

↓

Carrega Turma

↓

Matrícula.confirmar()

↓

Turma.consumirVaga()

↓

Persistência

↓

MatriculaConfirmada

──────────────┬───────────────┬──────────────

              │               │

              ▼               ▼

         Auditoria      Notificação
```

Observe que a alteração do domínio ocorre **antes** da publicação do evento.

Os consumidores executam apenas ações secundárias.

---

# 11. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Martin Fowler — *Domain Event*

---

# 12. Considerações Finais

Os Eventos de Domínio representam acontecimentos relevantes que ocorreram dentro do Sistema Acadêmico.

Sua utilização permite desacoplar funcionalidades secundárias das regras centrais do negócio, favorecendo uma arquitetura mais flexível e preparada para evolução.

Os eventos identificados neste documento servirão de base para futuras implementações de auditoria, notificações e integrações externas, preservando a consistência do domínio e respeitando os limites definidos pelos Aggregates.

---

# 13. Próximos Passos

Este documento servirá como base para:

- Documento 09 — Casos de Uso;
- Documento 10 — Arquitetura Hexagonal;
- Documento 11 — Repositórios (Ports e Adapters).