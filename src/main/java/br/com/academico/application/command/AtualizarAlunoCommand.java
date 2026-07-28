package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoAcademica;

import java.util.UUID;

public record AtualizarAlunoCommand(
        UUID id,
        String nome,
        String email,
        SituacaoAcademica situacaoAcademica
) {
}
