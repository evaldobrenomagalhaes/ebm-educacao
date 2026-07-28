package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.CadastrarDisciplinaCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CadastrarDisciplinaRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotBlank @Size(max = 50) String codigo,
        @NotNull UUID cursoId
) {

    public CadastrarDisciplinaCommand toCommand() {
        return new CadastrarDisciplinaCommand(nome, codigo, cursoId);
    }
}
