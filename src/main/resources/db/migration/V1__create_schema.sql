CREATE TABLE LY_ALUNO (
    id                  UUID            NOT NULL,
    nome                VARCHAR(255)    NOT NULL,
    email               VARCHAR(255)    NOT NULL,
    situacao_academica  VARCHAR(30)     NOT NULL,
    CONSTRAINT pk_ly_aluno PRIMARY KEY (id)
);

CREATE TABLE LY_CURSO (
    id          UUID            NOT NULL,
    nome        VARCHAR(255)    NOT NULL,
    situacao    VARCHAR(30)     NOT NULL,
    CONSTRAINT pk_ly_curso PRIMARY KEY (id)
);

CREATE TABLE LY_DISCIPLINA (
    id          UUID            NOT NULL,
    nome        VARCHAR(255)    NOT NULL,
    codigo      VARCHAR(100)    NOT NULL,
    curso_id    UUID            NOT NULL,
    CONSTRAINT pk_ly_disciplina PRIMARY KEY (id),
    CONSTRAINT fk_disciplina_curso FOREIGN KEY (curso_id) REFERENCES LY_CURSO (id)
);

CREATE TABLE LY_PERIODO_LETIVO (
    id              UUID            NOT NULL,
    codigo          VARCHAR(100)    NOT NULL,
    data_inicio     DATE            NOT NULL,
    data_termino    DATE            NOT NULL,
    situacao        VARCHAR(30)     NOT NULL,
    CONSTRAINT pk_ly_periodo_letivo PRIMARY KEY (id)
);

CREATE TABLE LY_TURMA (
    id                  UUID            NOT NULL,
    codigo              VARCHAR(100)    NOT NULL,
    disciplina_id       UUID            NOT NULL,
    periodo_letivo_id   UUID            NOT NULL,
    capacidade_maxima   INTEGER         NOT NULL,
    vagas_disponiveis   INTEGER         NOT NULL,
    status              VARCHAR(30)     NOT NULL,
    CONSTRAINT pk_ly_turma PRIMARY KEY (id),
    CONSTRAINT fk_turma_disciplina FOREIGN KEY (disciplina_id) REFERENCES LY_DISCIPLINA (id),
    CONSTRAINT fk_turma_periodo_letivo FOREIGN KEY (periodo_letivo_id) REFERENCES LY_PERIODO_LETIVO (id),
    CONSTRAINT ck_turma_capacidade_positiva CHECK (capacidade_maxima > 0),
    CONSTRAINT ck_turma_vagas_nao_negativas CHECK (vagas_disponiveis >= 0),
    CONSTRAINT ck_turma_vagas_nao_excedem_capacidade CHECK (vagas_disponiveis <= capacidade_maxima)
);

CREATE TABLE LY_MATRICULA (
    id          UUID            NOT NULL,
    aluno_id    UUID            NOT NULL,
    turma_id    UUID            NOT NULL,
    status      VARCHAR(30)     NOT NULL,
    CONSTRAINT pk_ly_matricula PRIMARY KEY (id),
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES LY_ALUNO (id),
    CONSTRAINT fk_matricula_turma FOREIGN KEY (turma_id) REFERENCES LY_TURMA (id),
    CONSTRAINT uk_matricula_aluno_turma UNIQUE (aluno_id, turma_id)
);

CREATE INDEX ix_disciplina_curso_id ON LY_DISCIPLINA (curso_id);
CREATE INDEX ix_turma_disciplina_id ON LY_TURMA (disciplina_id);
CREATE INDEX ix_turma_periodo_letivo_id ON LY_TURMA (periodo_letivo_id);
CREATE INDEX ix_matricula_aluno_id ON LY_MATRICULA (aluno_id);
CREATE INDEX ix_matricula_turma_id ON LY_MATRICULA (turma_id);
