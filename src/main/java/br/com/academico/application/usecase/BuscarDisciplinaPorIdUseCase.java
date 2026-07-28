package br.com.academico.application.usecase;

import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.application.query.BuscarDisciplinaPorIdQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;

import java.util.Objects;

public final class BuscarDisciplinaPorIdUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public BuscarDisciplinaPorIdUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
    }

    public DisciplinaDto executar(BuscarDisciplinaPorIdQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return disciplinaRepository.buscarPorId(DisciplinaId.de(query.id()))
                .map(DisciplinaDto::from)
                .orElseThrow(() -> EntityNotFoundException.of("Disciplina", query.id()));
    }
}
