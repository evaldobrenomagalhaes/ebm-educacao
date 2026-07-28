package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoAcademica;

public record CadastrarAlunoCommand(
        String nome,
        String email,
        SituacaoAcademica situacaoAcademica
) {
}
