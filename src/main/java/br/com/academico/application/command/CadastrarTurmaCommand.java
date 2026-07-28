package br.com.academico.application.command;

import br.com.academico.domain.valueobject.StatusTurma;

import java.util.UUID;

public record CadastrarTurmaCommand(
        String codigo,
        UUID disciplinaId,
        UUID periodoLetivoId,
        int capacidadeMaxima,
        StatusTurma status
) {
}
