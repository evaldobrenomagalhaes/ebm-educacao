package br.com.academico.application.usecase;

import br.com.academico.application.dto.CursoDto;
import br.com.academico.application.query.BuscarCursoPorIdQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.valueobject.CursoId;

import java.util.Objects;

public final class BuscarCursoPorIdUseCase {

    private final CursoRepository cursoRepository;

    public BuscarCursoPorIdUseCase(CursoRepository cursoRepository) {
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public CursoDto executar(BuscarCursoPorIdQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return cursoRepository.buscarPorId(CursoId.de(query.id()))
                .map(CursoDto::from)
                .orElseThrow(() -> EntityNotFoundException.of("Curso", query.id()));
    }
}
