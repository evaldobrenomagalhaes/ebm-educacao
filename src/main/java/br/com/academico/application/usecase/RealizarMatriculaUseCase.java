package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.RealizarMatriculaCommand;
import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.domain.event.DomainEventPublisher;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Objects;

@Transactional
public class RealizarMatriculaUseCase {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final DomainEventPublisher domainEventPublisher;

    public RealizarMatriculaUseCase(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher);
    }

    public MatriculaDto executar(RealizarMatriculaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        AlunoId alunoId = AlunoId.de(command.alunoId());
        TurmaId turmaId = TurmaId.de(command.turmaId());

        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> EntityNotFoundException.of("Aluno", command.alunoId()));
        aluno.garantirAptaParaMatricula();

        Turma turma = turmaRepository.buscarPorId(turmaId)
                .orElseThrow(() -> EntityNotFoundException.of("Turma", command.turmaId()));
        turma.garantirAbertaParaMatricula();

        Matricula.garantirUnicaNaTurma(matriculaRepository.existePorAlunoETurma(alunoId, turmaId));

        Matricula matricula = Matricula.realizar(alunoId, turmaId);
        matriculaRepository.salvar(matricula);
        domainEventPublisher.publishAll(matricula.pullDomainEvents());
        return MatriculaDto.from(matricula);
    }
}
