package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarPeriodoLetivoCommand;
import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.valueobject.PeriodoLetivoId;

import java.util.Objects;

public final class AtualizarPeriodoLetivoUseCase {

    private final PeriodoLetivoRepository periodoLetivoRepository;

    public AtualizarPeriodoLetivoUseCase(PeriodoLetivoRepository periodoLetivoRepository) {
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public PeriodoLetivoDto executar(AtualizarPeriodoLetivoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        PeriodoLetivoId id = PeriodoLetivoId.de(command.id());
        PeriodoLetivo periodo = periodoLetivoRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Período Letivo", command.id()));
        periodo.atualizar(
                command.codigo(),
                command.dataInicio(),
                command.dataTermino(),
                command.situacao()
        );
        periodoLetivoRepository.salvar(periodo);
        return PeriodoLetivoDto.from(periodo);
    }
}
