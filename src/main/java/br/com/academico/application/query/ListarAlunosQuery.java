package br.com.academico.application.query;

import br.com.academico.domain.valueobject.SituacaoAcademica;

public record ListarAlunosQuery(
        String nome,
        String email,
        SituacaoAcademica situacaoAcademica
) {

    public static ListarAlunosQuery todos() {
        return new ListarAlunosQuery(null, null, null);
    }
}
