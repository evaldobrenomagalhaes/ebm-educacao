package br.com.academico.application.usecase;

import br.com.academico.application.command.CadastrarPeriodoLetivoCommand;
import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.PeriodoLetivoRepository;

import java.util.Objects;

public final class CadastrarPeriodoLetivoUseCase {

    private final PeriodoLetivoRepository periodoLetivoRepository;

    public CadastrarPeriodoLetivoUseCase(PeriodoLetivoRepository periodoLetivoRepository) {
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public PeriodoLetivoDto executar(CadastrarPeriodoLetivoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                command.codigo(),
                command.dataInicio(),
                command.dataTermino(),
                command.situacao()
        );
        periodoLetivoRepository.salvar(periodo);
        return PeriodoLetivoDto.from(periodo);
    }
}
