package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.ConfirmarMatriculaCommand;
import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.domain.event.DomainEventPublisher;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.MatriculaId;

import java.util.Objects;

@Transactional
public class ConfirmarMatriculaUseCase {

    private final MatriculaRepository matriculaRepository;
    private final TurmaRepository turmaRepository;
    private final DomainEventPublisher domainEventPublisher;

    public ConfirmarMatriculaUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher);
    }

    public MatriculaDto executar(ConfirmarMatriculaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        MatriculaId matriculaId = MatriculaId.de(command.matriculaId());

        Matricula matricula = matriculaRepository.buscarPorId(matriculaId)
                .orElseThrow(() -> EntityNotFoundException.of("Matrícula", command.matriculaId()));
        Turma turma = turmaRepository.buscarPorId(matricula.getTurmaId())
                .orElseThrow(() -> EntityNotFoundException.of("Turma", matricula.getTurmaId().valor()));

        matricula.confirmar();
        turma.consumirVaga();

        matriculaRepository.salvar(matricula);
        turmaRepository.salvar(turma);
        domainEventPublisher.publishAll(matricula.pullDomainEvents());
        return MatriculaDto.from(matricula);
    }
}
