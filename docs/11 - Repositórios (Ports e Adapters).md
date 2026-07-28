# Documento 11 — Repositórios (Ports e Adapters)

> Versão: 1.0
>
> Fase: Arquitetura
>
> Status: Aprovado

---

# 1. Objetivo

Este documento define como será realizada a persistência dos objetos do domínio utilizando o padrão **Repository**, em conjunto com a Arquitetura Hexagonal (Ports and Adapters).

Seu objetivo é estabelecer uma separação clara entre o domínio e a infraestrutura, permitindo que as regras de negócio permaneçam independentes de tecnologias de persistência, frameworks e banco de dados.

---

# 2. Conceito de Repository

No Domain-Driven Design (DDD), um **Repository** representa uma abstração responsável por fornecer acesso aos Aggregates do domínio.

Seu propósito é permitir que os Casos de Uso recuperem e persistam objetos do domínio sem conhecer detalhes da tecnologia utilizada.

Um Repository deve oferecer uma interface semelhante à manipulação de uma coleção de objetos do domínio.

Exemplo conceitual:

```text
Buscar uma Turma

↓

Receber um objeto Turma

↓

Alterar seu estado

↓

Salvar novamente
```

O domínio permanece totalmente independente da forma como esses dados são armazenados.

---

# 3. Repository como Port

Na Arquitetura Hexagonal, o Repository representa uma **Port de saída (Outbound Port)**.

Essa Port é definida pelo domínio e descreve apenas **o que precisa ser feito**, sem especificar **como será realizado**.

Exemplo conceitual:

```java
public interface TurmaRepository {

    Optional<Turma> buscarPorId(TurmaId id);

    void salvar(Turma turma);

}
```

Essa interface pertence ao domínio.

Ela não conhece:

- JPA;
- Hibernate;
- SQL;
- Spring Data;
- Banco de Dados.

Conhece apenas os conceitos do domínio.

---

# 4. Implementação como Adapter

A implementação concreta do Repository pertence à camada de infraestrutura.

Ela é responsável por utilizar a tecnologia escolhida para persistir os dados.

Exemplo conceitual:

```java
interface SpringDataTurmaRepository extends JpaRepository<Turma, TurmaId> {
}

class JpaTurmaRepository implements TurmaRepository {

    private final SpringDataTurmaRepository springDataRepository;

    JpaTurmaRepository(SpringDataTurmaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Turma> buscarPorId(TurmaId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Turma turma) {
        springDataRepository.save(turma);
    }
}
```

O Adapter (`JpaTurmaRepository`) encapsula o Spring Data JPA internamente. Tipos do Spring Data não vazam para o domínio: a Port `TurmaRepository` expõe apenas conceitos do domínio.

A entidade de domínio `Turma` é manipulada diretamente pelo Spring Data (DA-011), sem DTO intermediário entre domínio e persistência.

Essa classe poderá utilizar:

- Spring Data JPA;
- Hibernate;
- SQL;
- Banco de Dados.

O domínio permanece desacoplado dessas tecnologias.

---

# 5. Repositórios Identificados

Com base na modelagem realizada, foram identificados os seguintes contratos de persistência.

| Repository | Aggregate |
|------------|-----------|
| AlunoRepository | Aluno |
| CursoRepository | Curso |
| DisciplinaRepository | Disciplina |
| PeriodoLetivoRepository | Período Letivo |
| TurmaRepository | Turma |
| MatriculaRepository | Matrícula |

Cada Repository será responsável apenas pelo Aggregate correspondente.

`PeriodoLetivoRepository` cobre o CRUD de períodos letivos do modelo acadêmico do projeto, inclusive consultas filtradas (texto, situação e data).

`MatriculaRepository` expõe, entre outros, `buscarPorId`, `salvar`, `existePorAlunoETurma`, `listarPorAluno`, `listarPorTurma` e `listar()` — este último para a listagem global (`ListarMatriculas`, Documento 09 §4.4); filtros opcionais são aplicados na camada de aplicação.

---

# 6. Responsabilidades dos Repositórios

Os Repositórios deverão:

- Recuperar Aggregates;
- Persistir alterações;
- Verificar existência de objetos quando necessário;
- Remover objetos, quando permitido pelo domínio.

Os Repositórios não deverão:

- Implementar regras de negócio;
- Alterar estados das entidades;
- Executar validações do domínio;
- Enviar notificações;
- Publicar eventos;
- Construir DTOs;
- Coordenar Casos de Uso.

Essas responsabilidades pertencem a outras camadas da aplicação.

---

# 7. Fluxo de Persistência

O fluxo esperado para recuperação e persistência de um Aggregate é o seguinte.

```text
Controller

↓

Caso de Uso

↓

TurmaRepository (Port)

↓

JpaTurmaRepository (Adapter)

↓

Spring Data JPA

↓

Banco de Dados
```

O Caso de Uso conhece apenas a interface do Repository.

A implementação concreta é fornecida pela infraestrutura.

---

# 8. Organização dos Pacotes

A separação entre contratos e implementações seguirá a organização abaixo.

```text
domain

└── repository

    ├── AlunoRepository
    ├── CursoRepository
    ├── DisciplinaRepository
    ├── PeriodoLetivoRepository
    ├── TurmaRepository
    └── MatriculaRepository
```

```text
infrastructure

└── persistence

    └── repository

        ├── JpaAlunoRepository
        ├── JpaCursoRepository
        ├── JpaDisciplinaRepository
        ├── JpaPeriodoLetivoRepository
        ├── JpaTurmaRepository
        └── JpaMatriculaRepository
```

Essa organização preserva a independência entre domínio e infraestrutura.

---

# 9. Relação com os Casos de Uso

Os Casos de Uso dependem apenas das interfaces definidas no domínio.

Fluxo conceitual:

```text
ConfirmarMatriculaUseCase

↓

MatriculaRepository

↓

TurmaRepository

↓

Executa domínio

↓

Salva alterações
```

Os Casos de Uso desconhecem a tecnologia utilizada para persistência.

---

# 10. Relação com os Aggregate Roots

Cada Repository é responsável por recuperar e persistir um Aggregate completo.

O objetivo é garantir que as invariantes protegidas pelo Aggregate permaneçam consistentes.

Os Repositórios não manipulam entidades isoladamente.

Sempre trabalham com o Aggregate correspondente.

---

# 11. Exemplo Conceitual

Exemplo simplificado da interação entre domínio e infraestrutura.

```text
                 Domínio

         TurmaRepository (Port)

               ▲
               │ implementa
               │

     JpaTurmaRepository (Adapter)

               │ encapsula
               ▼

    SpringDataTurmaRepository

               │

               ▼

        Spring Data JPA

               │

               ▼

          Banco de Dados
```

Código correspondente (mesmo padrão da seção 4):

```java
interface SpringDataTurmaRepository extends JpaRepository<Turma, TurmaId> {
}

class JpaTurmaRepository implements TurmaRepository {

    private final SpringDataTurmaRepository springDataRepository;

    JpaTurmaRepository(SpringDataTurmaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Turma> buscarPorId(TurmaId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Turma turma) {
        springDataRepository.save(turma);
    }
}
```

`Turma` (entidade de domínio) é persistida diretamente via Spring Data (DA-011), sem mapper nem DTO intermediário. Essa estrutura segue o princípio da inversão de dependências.

---

# 12. Decisões de Arquitetura

## DA-006 — Repositórios serão definidos por interfaces

### Decisão

Todos os contratos de persistência serão representados por interfaces.

### Justificativa

Permite desacoplamento entre domínio e infraestrutura.

---

## DA-007 — Implementações pertencem à infraestrutura

### Decisão

As implementações concretas dos Repositórios ficarão exclusivamente na camada de infraestrutura.

### Justificativa

Evita dependência do domínio em relação a frameworks ou tecnologias específicas.

---

## DA-008 — Casos de Uso dependem apenas das Ports

### Decisão

Os Casos de Uso utilizarão exclusivamente interfaces de Repository.

### Justificativa

Essa abordagem segue os princípios da Arquitetura Hexagonal e da Inversão de Dependência.

---

## DA-009 — Repository não contém regras de negócio

### Decisão

Os Repositórios terão responsabilidade exclusivamente relacionada à persistência.

### Justificativa

As regras de negócio pertencem ao domínio e não devem ser implementadas na camada de infraestrutura.

---

## DA-010 — Repository recupera Aggregates completos

### Decisão

Os Repositórios deverão recuperar Aggregates completos sempre que necessário.

### Justificativa

Essa estratégia garante que as invariantes do Aggregate possam ser preservadas durante sua manipulação.

---

# 13. Relação com os Documentos Anteriores

Este documento complementa as decisões arquiteturais anteriormente definidas.

| Documento | Contribuição |
|-----------|--------------|
| Documento 07 | Define os Aggregate Roots que serão persistidos pelos Repositórios |
| Documento 09 | Define os Casos de Uso que utilizam os Repositórios |
| Documento 10 | Define a Arquitetura Hexagonal utilizada pela aplicação |

---

# 14. Referências

Este documento foi elaborado com base nas seguintes obras:

- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Martin Fowler — *Patterns of Enterprise Application Architecture*
- Robert C. Martin — *Clean Architecture*
- Alistair Cockburn — *Hexagonal Architecture*

---

# 15. Considerações Finais

Os Repositórios representam o mecanismo de persistência do domínio sem comprometer sua independência em relação às tecnologias utilizadas.

Ao definir contratos no domínio e implementações na infraestrutura, a aplicação mantém baixo acoplamento, favorece testes unitários e permite substituir mecanismos de persistência com impacto mínimo sobre as regras de negócio.

Essa abordagem está alinhada aos princípios do Domain-Driven Design, da Arquitetura Hexagonal e da Clean Architecture.

---

# 16. Próximos Passos

Este documento servirá como base para:

- Documento 13 — Persistência com JPA;
- Documento 14 — Tratamento de Exceções do Domínio;
- Documento 15 — Estratégia de Testes;
- Documento 16 — Estrutura do Projeto.