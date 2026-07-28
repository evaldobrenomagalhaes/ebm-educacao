package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoCurso;

import java.util.UUID;

public record AtualizarCursoCommand(
        UUID id,
        String nome,
        SituacaoCurso situacao
) {
}
