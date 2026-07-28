package br.com.academico.application.usecase;

import br.com.academico.application.command.ExcluirPeriodoLetivoCommand;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.valueobject.PeriodoLetivoId;

import java.util.Objects;

public final class ExcluirPeriodoLetivoUseCase {

    private final PeriodoLetivoRepository periodoLetivoRepository;

    public ExcluirPeriodoLetivoUseCase(PeriodoLetivoRepository periodoLetivoRepository) {
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public void executar(ExcluirPeriodoLetivoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        PeriodoLetivoId id = PeriodoLetivoId.de(command.id());
        if (periodoLetivoRepository.buscarPorId(id).isEmpty()) {
            throw EntityNotFoundException.of("Período Letivo", command.id());
        }
        periodoLetivoRepository.excluir(id);
    }
}
