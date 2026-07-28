package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.AtualizarDisciplinaCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AtualizarDisciplinaRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotBlank @Size(max = 50) String codigo,
        @NotNull UUID cursoId
) {

    public AtualizarDisciplinaCommand toCommand(UUID id) {
        return new AtualizarDisciplinaCommand(id, nome, codigo, cursoId);
    }
}
