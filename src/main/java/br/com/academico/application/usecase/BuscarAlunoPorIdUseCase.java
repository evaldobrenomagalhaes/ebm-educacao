package br.com.academico.application.usecase;

import br.com.academico.application.dto.AlunoDto;
import br.com.academico.application.query.BuscarAlunoPorIdQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.AlunoId;

import java.util.Objects;

public final class BuscarAlunoPorIdUseCase {

    private final AlunoRepository alunoRepository;

    public BuscarAlunoPorIdUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
    }

    public AlunoDto executar(BuscarAlunoPorIdQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return alunoRepository.buscarPorId(AlunoId.de(query.id()))
                .map(AlunoDto::from)
                .orElseThrow(() -> EntityNotFoundException.of("Aluno", query.id()));
    }
}
