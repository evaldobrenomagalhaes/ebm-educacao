package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.ConsultarMatriculasPorAlunoQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class ConsultarMatriculasPorAlunoUseCase {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public ConsultarMatriculasPorAlunoUseCase(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository
    ) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public List<MatriculaDto> executar(ConsultarMatriculasPorAlunoQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        Objects.requireNonNull(query.alunoId(), "Aluno é obrigatório");
        AlunoId alunoId = AlunoId.de(query.alunoId());

        if (alunoRepository.buscarPorId(alunoId).isEmpty()) {
            throw EntityNotFoundException.of("Aluno", query.alunoId());
        }

        Map<TurmaId, Turma> turmasPorId = indiceTurmas();
        return matriculaRepository.listarPorAluno(alunoId).stream()
                .filter(matricula -> corresponde(matricula, query, turmasPorId))
                .map(MatriculaDto::from)
                .toList();
    }

    private Map<TurmaId, Turma> indiceTurmas() {
        return turmaRepository.listar().stream()
                .collect(Collectors.toMap(Turma::getId, Function.identity()));
    }

    private static boolean corresponde(
            Matricula matricula,
            ConsultarMatriculasPorAlunoQuery query,
            Map<TurmaId, Turma> turmasPorId
    ) {
        if (query.status() != null && query.status() != matricula.getStatus()) {
            return false;
        }
        if (query.periodoLetivoId() == null && query.disciplinaId() == null) {
            return true;
        }
        Turma turma = turmasPorId.get(matricula.getTurmaId());
        if (turma == null) {
            return false;
        }
        if (query.periodoLetivoId() != null
                && !query.periodoLetivoId().equals(turma.getPeriodoLetivoId().valor())) {
            return false;
        }
        return query.disciplinaId() == null
                || query.disciplinaId().equals(turma.getDisciplinaId().valor());
    }
}
