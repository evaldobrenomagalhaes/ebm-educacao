package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;

import java.time.LocalDate;
import java.util.UUID;

public record AtualizarPeriodoLetivoCommand(
        UUID id,
        String codigo,
        LocalDate dataInicio,
        LocalDate dataTermino,
        SituacaoPeriodoLetivo situacao
) {
}
