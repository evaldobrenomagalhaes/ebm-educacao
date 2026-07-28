package br.com.academico.application.usecase;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.ConsultarMatriculasPorTurmaQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.List;
import java.util.Objects;

public final class ConsultarMatriculasPorTurmaUseCase {

    private final MatriculaRepository matriculaRepository;
    private final TurmaRepository turmaRepository;

    public ConsultarMatriculasPorTurmaUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository
    ) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public List<MatriculaDto> executar(ConsultarMatriculasPorTurmaQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        Objects.requireNonNull(query.turmaId(), "Turma é obrigatória");
        TurmaId turmaId = TurmaId.de(query.turmaId());

        Turma turma = turmaRepository.buscarPorId(turmaId)
                .orElseThrow(() -> EntityNotFoundException.of("Turma", query.turmaId()));

        if (!correspondeTurma(turma, query)) {
            return List.of();
        }

        return matriculaRepository.listarPorTurma(turmaId).stream()
                .filter(matricula -> correspondeStatus(matricula, query))
                .map(MatriculaDto::from)
                .toList();
    }

    private static boolean correspondeTurma(Turma turma, ConsultarMatriculasPorTurmaQuery query) {
        if (query.periodoLetivoId() != null
                && !query.periodoLetivoId().equals(turma.getPeriodoLetivoId().valor())) {
            return false;
        }
        return query.disciplinaId() == null
                || query.disciplinaId().equals(turma.getDisciplinaId().valor());
    }

    private static boolean correspondeStatus(Matricula matricula, ConsultarMatriculasPorTurmaQuery query) {
        return query.status() == null || query.status() == matricula.getStatus();
    }
}
