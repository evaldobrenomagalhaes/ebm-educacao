package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.ListarMatriculasQuery;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional(readOnly = true)
public class ListarMatriculasUseCase {

    private final MatriculaRepository matriculaRepository;
    private final TurmaRepository turmaRepository;

    public ListarMatriculasUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository
    ) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public List<MatriculaDto> executar(ListarMatriculasQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        Map<TurmaId, Turma> turmasPorId = MatriculaFiltro.indiceTurmas(turmaRepository);
        return matriculaRepository.listar().stream()
                .filter(matricula -> corresponde(matricula, query, turmasPorId))
                .map(MatriculaDto::from)
                .toList();
    }

    private static boolean corresponde(
            Matricula matricula,
            ListarMatriculasQuery query,
            Map<TurmaId, Turma> turmasPorId
    ) {
        if (query.alunoId() != null && !query.alunoId().equals(matricula.getAlunoId().valor())) {
            return false;
        }
        if (query.turmaId() != null && !query.turmaId().equals(matricula.getTurmaId().valor())) {
            return false;
        }
        return MatriculaFiltro.correspondeStatusPeriodoDisciplina(
                matricula,
                query.status(),
                query.periodoLetivoId(),
                query.disciplinaId(),
                turmasPorId
        );
    }
}
