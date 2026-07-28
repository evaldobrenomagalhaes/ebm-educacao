package br.com.academico.application.query;

import br.com.academico.domain.valueobject.StatusMatricula;

import java.util.UUID;

public record ConsultarMatriculasPorAlunoQuery(
        UUID alunoId,
        StatusMatricula status,
        UUID periodoLetivoId,
        UUID disciplinaId
) {

    public static ConsultarMatriculasPorAlunoQuery doAluno(UUID alunoId) {
        return new ConsultarMatriculasPorAlunoQuery(alunoId, null, null, null);
    }
}
