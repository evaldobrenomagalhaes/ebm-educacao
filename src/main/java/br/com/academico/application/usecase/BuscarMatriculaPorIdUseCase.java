package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.BuscarMatriculaPorIdQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.valueobject.MatriculaId;

import java.util.Objects;

@Transactional(readOnly = true)
public class BuscarMatriculaPorIdUseCase {

    private final MatriculaRepository matriculaRepository;

    public BuscarMatriculaPorIdUseCase(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
    }

    public MatriculaDto executar(BuscarMatriculaPorIdQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return matriculaRepository.buscarPorId(MatriculaId.de(query.id()))
                .map(MatriculaDto::from)
                .orElseThrow(() -> EntityNotFoundException.of("Matrícula", query.id()));
    }
}
