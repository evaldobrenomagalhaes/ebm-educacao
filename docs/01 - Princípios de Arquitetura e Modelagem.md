# Documento 01 - Princípios de Arquitetura e Modelagem

> Versão: 1.0
>
> Fase: Análise
>
> Status: Aprovado

---

# 1. Objetivo

Este documento estabelece os princípios que orientarão todas as decisões de modelagem, arquitetura e implementação deste projeto.

Seu propósito é garantir consistência ao longo do desenvolvimento, assegurando que as decisões técnicas sejam consequência das necessidades do domínio e não da tecnologia utilizada.

Os princípios definidos neste documento servirão como referência para revisões de código, evolução da arquitetura e resolução de dúvidas durante o desenvolvimento.

---

# 2. Referências

Os princípios adotados neste projeto são fundamentados nas seguintes referências:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Robert C. Martin — *Clean Architecture*
- Martin Fowler — *Patterns of Enterprise Application Architecture*
- Joshua Bloch — *Effective Java*

---

# 3. Princípios de Modelagem do Domínio

## PA-01 — O domínio é o centro da aplicação

### Descrição

As regras de negócio pertencem ao domínio.

Nenhuma regra de negócio deverá ser implementada em controllers, componentes de infraestrutura ou mecanismos de persistência.

### Justificativa

Segundo Eric Evans, o software deve refletir o domínio do problema. A arquitetura existe para proteger o domínio, e não para defini-lo.

### Benefícios

- Alta coesão
- Independência tecnológica
- Facilidade de manutenção
- Reutilização das regras de negócio

---

## PA-02 — As responsabilidades devem estar próximas dos dados

### Descrição

As entidades devem encapsular os comportamentos relacionados ao seu próprio estado.

Sempre que possível, regras de negócio deverão ser implementadas pela entidade responsável pelos dados envolvidos.

### Justificativa

Eric Evans e Martin Fowler desencorajam o uso de modelos anêmicos (*Anemic Domain Model*), nos quais as entidades possuem apenas atributos e toda a lógica fica concentrada em serviços.

### Benefícios

- Alta coesão
- Código expressivo
- Encapsulamento
- Redução de inconsistências

---

## PA-03 — O domínio protege suas invariantes

### Descrição

Uma entidade nunca poderá assumir um estado inválido.

Toda alteração de estado deverá respeitar as regras de negócio definidas para o domínio.

### Justificativa

Segundo Vaughn Vernon, uma das principais responsabilidades de um Aggregate é proteger as invariantes do domínio.

### Benefícios

- Integridade do modelo
- Consistência
- Redução de erros

---

## PA-04 — Objetos protegem seu próprio estado

### Descrição

O estado interno das entidades somente poderá ser alterado por meio de comportamentos explícitos.

Sempre que representar uma regra de negócio, alterações de estado deverão ocorrer por métodos do domínio, evitando setters públicos.

### Justificativa

Esse princípio deriva do encapsulamento da Orientação a Objetos e é reforçado por Joshua Bloch em *Effective Java*.

### Benefícios

- Maior segurança
- Melhor legibilidade
- Integridade do modelo

---

## PA-05 — A linguagem do código deve refletir o negócio

### Descrição

Classes, métodos, atributos e eventos deverão utilizar a mesma linguagem empregada pelo domínio do negócio.

### Justificativa

A Linguagem Ubíqua (*Ubiquitous Language*) é um dos pilares do Domain-Driven Design e reduz ambiguidades entre especialistas do domínio e desenvolvedores.

### Benefícios

- Comunicação clara
- Código autoexplicativo
- Facilidade de manutenção

---

# 4. Princípios Arquiteturais

## PA-06 — O domínio não conhece frameworks

### Descrição

O modelo de domínio não deverá depender de frameworks, bibliotecas ou tecnologias específicas.

Frameworks são detalhes de implementação e devem permanecer nas camadas externas da aplicação.

### Justificativa

Este princípio segue a *Dependency Rule* proposta por Robert C. Martin em *Clean Architecture*.

### Benefícios

- Independência tecnológica
- Testabilidade
- Facilidade de evolução

---

## PA-07 — Casos de uso orquestram o sistema

### Descrição

Casos de uso coordenam o fluxo da aplicação.

Eles são responsáveis por orquestrar a execução das regras de negócio, sem substituir o comportamento das entidades do domínio.

### Justificativa

A camada de Application da Clean Architecture possui responsabilidade de coordenação, enquanto as regras permanecem no domínio.

### Benefícios

- Separação de responsabilidades
- Fluxos claros
- Facilidade de manutenção

---

## PA-08 — Repositórios possuem apenas responsabilidade de persistência

### Descrição

Repositórios são responsáveis apenas por recuperar e persistir objetos do domínio.

Nenhuma regra de negócio deverá ser implementada nessa camada.

### Justificativa

Segundo Martin Fowler, o padrão Repository abstrai os mecanismos de persistência, mantendo o domínio desacoplado da infraestrutura.

### Benefícios

- Baixo acoplamento
- Responsabilidade única
- Facilidade para testes

---

# 5. Princípios de Evolução

## PA-09 — Eventos representam fatos do domínio

### Descrição

Eventos representam acontecimentos relevantes ocorridos no domínio.

Eventos descrevem fatos consumados e nunca intenções ou comandos.

### Exemplo

✔ MatrículaConfirmada

✔ MatrículaCancelada

✘ ConfirmarMatricula

### Justificativa

Domain Events representam fatos relevantes para o negócio e favorecem baixo acoplamento entre componentes.

### Benefícios

- Extensibilidade
- Baixo acoplamento
- Evolução incremental

---

## PA-10 — Toda decisão arquitetural deve possuir justificativa

### Descrição

Nenhuma tecnologia, padrão de projeto ou ferramenta será adotada apenas por tendência de mercado.

Toda decisão deverá responder às seguintes perguntas:

- Qual problema esta decisão resolve?
- Existe respaldo em literatura ou documentação oficial?
- Quais alternativas foram consideradas?
- Por que esta solução foi escolhida?
- Qual impacto ela possui na manutenção, testabilidade e evolução do sistema?

### Justificativa

Arquitetura é composta por decisões conscientes. Registrar suas motivações reduz inconsistências e facilita futuras evoluções.

### Benefícios

- Consistência arquitetural
- Clareza nas decisões
- Facilidade de manutenção
- Evolução sustentável

---

# 6. Aplicação dos Princípios

Durante o desenvolvimento, qualquer dúvida relacionada à modelagem ou arquitetura deverá ser analisada à luz destes princípios.

Exemplos:

| Dúvida | Princípio Aplicável |
|---------|---------------------|
| Onde uma regra de negócio deve ser implementada? | PA-01 |
| Posso utilizar setters públicos para alterar estados importantes? | PA-04 |
| Um Repository pode conter regras de negócio? | PA-08 |
| O domínio pode depender diretamente do Spring Framework? | PA-06 |
| Como devo nomear classes e métodos? | PA-05 |

Esses princípios servirão como referência durante revisões de código e decisões arquiteturais.

---

# 7. Considerações Finais

Os princípios definidos neste documento constituem a base arquitetural do projeto.

Todos os documentos produzidos nas próximas fases, bem como toda implementação realizada, deverão respeitar estes princípios.

Sempre que uma nova decisão arquitetural relevante surgir, ela deverá ser registrada e justificada de acordo com o princípio **PA-10**.