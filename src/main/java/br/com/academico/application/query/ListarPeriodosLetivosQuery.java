package br.com.academico.application.query;

import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;

import java.time.LocalDate;

public record ListarPeriodosLetivosQuery(
        String codigo,
        SituacaoPeriodoLetivo situacao,
        LocalDate dataInicioDe,
        LocalDate dataInicioAte,
        LocalDate dataTerminoDe,
        LocalDate dataTerminoAte,
        LocalDate vigenteEm
) {

    public static ListarPeriodosLetivosQuery todos() {
        return new ListarPeriodosLetivosQuery(null, null, null, null, null, null, null);
    }
}
