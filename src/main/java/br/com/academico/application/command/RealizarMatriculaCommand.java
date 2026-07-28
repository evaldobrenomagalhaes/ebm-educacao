package br.com.academico.application.command;

import java.util.UUID;

public record RealizarMatriculaCommand(
        UUID alunoId,
        UUID turmaId
) {
}
