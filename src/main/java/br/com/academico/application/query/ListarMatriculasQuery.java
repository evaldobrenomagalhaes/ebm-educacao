package br.com.academico.application.query;

import br.com.academico.domain.valueobject.StatusMatricula;

import java.util.UUID;

public record ListarMatriculasQuery(
        StatusMatricula status,
        UUID alunoId,
        UUID turmaId,
        UUID periodoLetivoId,
        UUID disciplinaId
) {

    public static ListarMatriculasQuery todas() {
        return new ListarMatriculasQuery(null, null, null, null, null);
    }
}
