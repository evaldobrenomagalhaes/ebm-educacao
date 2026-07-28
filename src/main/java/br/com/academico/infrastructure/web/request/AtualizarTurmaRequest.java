package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.AtualizarTurmaCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AtualizarTurmaRequest(
        @NotBlank @Size(max = 50) String codigo,
        @NotNull UUID disciplinaId,
        @NotNull UUID periodoLetivoId,
        @Positive int capacidadeMaxima
) {

    public AtualizarTurmaCommand toCommand(UUID id) {
        return new AtualizarTurmaCommand(id, codigo, disciplinaId, periodoLetivoId, capacidadeMaxima);
    }
}
