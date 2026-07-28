package br.com.academico.application.usecase;

import br.com.academico.application.command.ExcluirDisciplinaCommand;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;

import java.util.Objects;

public final class ExcluirDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public ExcluirDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
    }

    public void executar(ExcluirDisciplinaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        DisciplinaId id = DisciplinaId.de(command.id());
        if (disciplinaRepository.buscarPorId(id).isEmpty()) {
            throw EntityNotFoundException.of("Disciplina", command.id());
        }
        disciplinaRepository.excluir(id);
    }
}
