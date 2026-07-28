package br.com.academico.application.command;

import java.util.UUID;

public record CadastrarDisciplinaCommand(
        String nome,
        String codigo,
        UUID cursoId
) {
}
