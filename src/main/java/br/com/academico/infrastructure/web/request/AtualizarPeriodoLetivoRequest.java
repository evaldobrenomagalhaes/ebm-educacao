package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.AtualizarPeriodoLetivoCommand;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AtualizarPeriodoLetivoRequest(
        @NotBlank @Size(max = 50) String codigo,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataTermino,
        @NotNull SituacaoPeriodoLetivo situacao
) {

    public AtualizarPeriodoLetivoCommand toCommand(UUID id) {
        return new AtualizarPeriodoLetivoCommand(id, codigo, dataInicio, dataTermino, situacao);
    }
}
