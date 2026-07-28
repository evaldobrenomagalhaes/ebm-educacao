package br.com.academico.application.query;

import br.com.academico.domain.valueobject.StatusTurma;

import java.util.UUID;

public record ListarTurmasQuery(
        String codigo,
        StatusTurma status,
        UUID disciplinaId,
        UUID periodoLetivoId,
        Boolean comVagas
) {

    public static ListarTurmasQuery todos() {
        return new ListarTurmasQuery(null, null, null, null, null);
    }
}
