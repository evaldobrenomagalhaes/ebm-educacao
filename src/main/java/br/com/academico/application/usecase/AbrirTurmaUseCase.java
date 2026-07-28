package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.AbrirTurmaCommand;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.event.DomainEventPublisher;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Objects;

@Transactional
public class AbrirTurmaUseCase {

    private final TurmaRepository turmaRepository;
    private final DomainEventPublisher domainEventPublisher;

    public AbrirTurmaUseCase(TurmaRepository turmaRepository, DomainEventPublisher domainEventPublisher) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher);
    }

    public TurmaDto executar(AbrirTurmaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        TurmaId id = TurmaId.de(command.turmaId());
        Turma turma = turmaRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Turma", command.turmaId()));
        turma.abrir();
        turmaRepository.salvar(turma);
        domainEventPublisher.publishAll(turma.pullDomainEvents());
        return TurmaDto.from(turma);
    }
}
