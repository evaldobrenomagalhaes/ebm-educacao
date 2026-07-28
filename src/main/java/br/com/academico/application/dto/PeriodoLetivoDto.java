package br.com.academico.application.dto;

import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;

import java.time.LocalDate;
import java.util.UUID;

public record PeriodoLetivoDto(
        UUID id,
        String codigo,
        LocalDate dataInicio,
        LocalDate dataTermino,
        SituacaoPeriodoLetivo situacao
) {

    public static PeriodoLetivoDto from(PeriodoLetivo periodoLetivo) {
        return new PeriodoLetivoDto(
                periodoLetivo.getId().valor(),
                periodoLetivo.getCodigo(),
                periodoLetivo.getDataInicio(),
                periodoLetivo.getDataTermino(),
                periodoLetivo.getSituacao()
        );
    }
}
