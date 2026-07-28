package br.com.academico.application.usecase;

import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.application.query.ListarPeriodosLetivosQuery;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.PeriodoLetivoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class ListarPeriodosLetivosUseCase {

    private final PeriodoLetivoRepository periodoLetivoRepository;

    public ListarPeriodosLetivosUseCase(PeriodoLetivoRepository periodoLetivoRepository) {
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public List<PeriodoLetivoDto> executar(ListarPeriodosLetivosQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return periodoLetivoRepository.listar().stream()
                .filter(periodo -> corresponde(periodo, query))
                .map(PeriodoLetivoDto::from)
                .toList();
    }

    private static boolean corresponde(PeriodoLetivo periodo, ListarPeriodosLetivosQuery query) {
        if (!TextoFiltro.contem(periodo.getCodigo(), query.codigo())) {
            return false;
        }
        if (query.situacao() != null && query.situacao() != periodo.getSituacao()) {
            return false;
        }
        if (!dentroDoIntervalo(periodo.getDataInicio(), query.dataInicioDe(), query.dataInicioAte())) {
            return false;
        }
        if (!dentroDoIntervalo(periodo.getDataTermino(), query.dataTerminoDe(), query.dataTerminoAte())) {
            return false;
        }
        return vigenteEm(periodo, query.vigenteEm());
    }

    private static boolean dentroDoIntervalo(LocalDate valor, LocalDate de, LocalDate ate) {
        if (de != null && valor.isBefore(de)) {
            return false;
        }
        return ate == null || !valor.isAfter(ate);
    }

    private static boolean vigenteEm(PeriodoLetivo periodo, LocalDate vigenteEm) {
        if (vigenteEm == null) {
            return true;
        }
        return !periodo.getDataInicio().isAfter(vigenteEm)
                && !periodo.getDataTermino().isBefore(vigenteEm);
    }
}
