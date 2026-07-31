package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.ConsultarMatriculasPorAlunoQuery;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        Map<TurmaId, Turma> turmasPorId = MatriculaFiltro.indiceTurmas(turmaRepository);
        return matriculaRepository.listarPorAluno(alunoId).stream()
                .filter(matricula -> MatriculaFiltro.correspondeStatusPeriodoDisciplina(
                        matricula,
                        query.status(),
                        query.periodoLetivoId(),
                        query.disciplinaId(),
                        turmasPorId
                ))
                .map(MatriculaDto::from)
                .toList();
    }
}
