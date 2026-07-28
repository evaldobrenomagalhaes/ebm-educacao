package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.CadastrarTurmaCommand;
import br.com.academico.domain.valueobject.StatusTurma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CadastrarTurmaRequest(
        @NotBlank @Size(max = 50) String codigo,
        @NotNull UUID disciplinaId,
        @NotNull UUID periodoLetivoId,
        @Positive int capacidadeMaxima,
        @NotNull StatusTurma status
) {

    public CadastrarTurmaCommand toCommand() {
        return new CadastrarTurmaCommand(codigo, disciplinaId, periodoLetivoId, capacidadeMaxima, status);
    }
}
