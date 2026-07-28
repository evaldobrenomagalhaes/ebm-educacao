package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.FecharTurmaCommand;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Objects;

@Transactional
public class FecharTurmaUseCase {

    private final TurmaRepository turmaRepository;

    public FecharTurmaUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public TurmaDto executar(FecharTurmaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        TurmaId id = TurmaId.de(command.turmaId());
        Turma turma = turmaRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Turma", command.turmaId()));
        turma.fechar();
        turmaRepository.salvar(turma);
        return TurmaDto.from(turma);
    }
}
