package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.TurmaDto;
import br.com.academico.application.query.BuscarTurmaPorIdQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Objects;

@Transactional(readOnly = true)
public class BuscarTurmaPorIdUseCase {

    private final TurmaRepository turmaRepository;

    public BuscarTurmaPorIdUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public TurmaDto executar(BuscarTurmaPorIdQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return turmaRepository.buscarPorId(TurmaId.de(query.id()))
                .map(TurmaDto::from)
                .orElseThrow(() -> EntityNotFoundException.of("Turma", query.id()));
    }
}
