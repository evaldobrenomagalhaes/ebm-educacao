package br.com.academico.infrastructure.web.response;

import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;

import java.time.LocalDate;
import java.util.UUID;

public record PeriodoLetivoResponse(
        UUID id,
        String codigo,
        LocalDate dataInicio,
        LocalDate dataTermino,
        SituacaoPeriodoLetivo situacao
) {

    public static PeriodoLetivoResponse from(PeriodoLetivoDto dto) {
        return new PeriodoLetivoResponse(
                dto.id(),
                dto.codigo(),
                dto.dataInicio(),
                dto.dataTermino(),
                dto.situacao()
        );
    }
}
