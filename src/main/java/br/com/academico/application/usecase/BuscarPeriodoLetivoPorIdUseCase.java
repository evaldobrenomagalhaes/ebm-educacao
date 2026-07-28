package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.application.query.BuscarPeriodoLetivoPorIdQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.valueobject.PeriodoLetivoId;

import java.util.Objects;

@Transactional(readOnly = true)
public class BuscarPeriodoLetivoPorIdUseCase {

    private final PeriodoLetivoRepository periodoLetivoRepository;

    public BuscarPeriodoLetivoPorIdUseCase(PeriodoLetivoRepository periodoLetivoRepository) {
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public PeriodoLetivoDto executar(BuscarPeriodoLetivoPorIdQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return periodoLetivoRepository.buscarPorId(PeriodoLetivoId.de(query.id()))
                .map(PeriodoLetivoDto::from)
                .orElseThrow(() -> EntityNotFoundException.of("Período Letivo", query.id()));
    }
}
