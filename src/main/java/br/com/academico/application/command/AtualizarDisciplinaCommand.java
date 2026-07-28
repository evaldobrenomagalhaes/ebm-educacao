package br.com.academico.application.command;

import java.util.UUID;

public record AtualizarDisciplinaCommand(
        UUID id,
        String nome,
        String codigo,
        UUID cursoId
) {
}
