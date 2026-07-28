package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.CadastrarPeriodoLetivoCommand;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CadastrarPeriodoLetivoRequest(
        @NotBlank @Size(max = 50) String codigo,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataTermino,
        @NotNull SituacaoPeriodoLetivo situacao
) {

    public CadastrarPeriodoLetivoCommand toCommand() {
        return new CadastrarPeriodoLetivoCommand(codigo, dataInicio, dataTermino, situacao);
    }
}
