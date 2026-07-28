package br.com.academico.application.usecase;

import br.com.academico.application.command.CadastrarTurmaCommand;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.exception.PeriodoLetivoEncerradoException;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.PeriodoLetivoId;

import java.util.Objects;

public final class CadastrarTurmaUseCase {

    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final PeriodoLetivoRepository periodoLetivoRepository;

    public CadastrarTurmaUseCase(
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public TurmaDto executar(CadastrarTurmaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        DisciplinaId disciplinaId = DisciplinaId.de(command.disciplinaId());
        PeriodoLetivoId periodoLetivoId = PeriodoLetivoId.de(command.periodoLetivoId());

        if (disciplinaRepository.buscarPorId(disciplinaId).isEmpty()) {
            throw EntityNotFoundException.of("Disciplina", command.disciplinaId());
        }
        PeriodoLetivo periodo = periodoLetivoRepository.buscarPorId(periodoLetivoId)
                .orElseThrow(() -> EntityNotFoundException.of("Período Letivo", command.periodoLetivoId()));
        if (periodo.estaEncerrado()) {
            throw PeriodoLetivoEncerradoException.doPeriodo();
        }

        Turma turma = Turma.cadastrar(
                command.codigo(),
                disciplinaId,
                periodoLetivoId,
                command.capacidadeMaxima(),
                command.status()
        );
        turmaRepository.salvar(turma);
        return TurmaDto.from(turma);
    }
}
