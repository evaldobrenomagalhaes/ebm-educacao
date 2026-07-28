package br.com.academico.application.query;

import br.com.academico.domain.valueobject.SituacaoCurso;

public record ListarCursosQuery(
        String nome,
        SituacaoCurso situacao
) {

    public static ListarCursosQuery todos() {
        return new ListarCursosQuery(null, null);
    }
}
