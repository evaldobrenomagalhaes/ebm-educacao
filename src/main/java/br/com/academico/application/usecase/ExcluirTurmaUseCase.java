package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.ExcluirTurmaCommand;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Objects;

@Transactional
public class ExcluirTurmaUseCase {

    private final TurmaRepository turmaRepository;

    public ExcluirTurmaUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public void executar(ExcluirTurmaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        TurmaId id = TurmaId.de(command.id());
        if (turmaRepository.buscarPorId(id).isEmpty()) {
            throw EntityNotFoundException.of("Turma", command.id());
        }
        turmaRepository.excluir(id);
    }
}
