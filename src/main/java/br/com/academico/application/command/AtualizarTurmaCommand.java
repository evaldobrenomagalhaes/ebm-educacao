package br.com.academico.application.command;

import java.util.UUID;

public record AtualizarTurmaCommand(
        UUID id,
        String codigo,
        UUID disciplinaId,
        UUID periodoLetivoId,
        int capacidadeMaxima
) {
}
