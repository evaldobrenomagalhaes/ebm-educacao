package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;

import java.time.LocalDate;

public record CadastrarPeriodoLetivoCommand(
        String codigo,
        LocalDate dataInicio,
        LocalDate dataTermino,
        SituacaoPeriodoLetivo situacao
) {
}
