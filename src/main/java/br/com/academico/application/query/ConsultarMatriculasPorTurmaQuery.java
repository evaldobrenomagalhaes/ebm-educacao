package br.com.academico.application.query;

import br.com.academico.domain.valueobject.StatusMatricula;

import java.util.UUID;

public record ConsultarMatriculasPorTurmaQuery(
        UUID turmaId,
        StatusMatricula status,
        UUID periodoLetivoId,
        UUID disciplinaId
) {

    public static ConsultarMatriculasPorTurmaQuery daTurma(UUID turmaId) {
        return new ConsultarMatriculasPorTurmaQuery(turmaId, null, null, null);
    }
}
