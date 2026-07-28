package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.RealizarMatriculaCommand;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RealizarMatriculaRequest(
        @NotNull UUID alunoId,
        @NotNull UUID turmaId
) {

    public RealizarMatriculaCommand toCommand() {
        return new RealizarMatriculaCommand(alunoId, turmaId);
    }
}
