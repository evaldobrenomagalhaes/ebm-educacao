package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoCurso;

public record CadastrarCursoCommand(
        String nome,
        SituacaoCurso situacao
) {
}
