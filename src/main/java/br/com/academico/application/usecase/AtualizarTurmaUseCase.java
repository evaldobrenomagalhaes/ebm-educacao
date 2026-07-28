package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.AtualizarTurmaCommand;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Objects;

@Transactional
public class AtualizarTurmaUseCase {

    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final PeriodoLetivoRepository periodoLetivoRepository;

    public AtualizarTurmaUseCase(
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
        this.periodoLetivoRepository = Objects.requireNonNull(periodoLetivoRepository);
    }

    public TurmaDto executar(AtualizarTurmaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        TurmaId id = TurmaId.de(command.id());
        Turma turma = turmaRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Turma", command.id()));

        DisciplinaId disciplinaId = DisciplinaId.de(command.disciplinaId());
        PeriodoLetivoId periodoLetivoId = PeriodoLetivoId.de(command.periodoLetivoId());

        if (disciplinaRepository.buscarPorId(disciplinaId).isEmpty()) {
            throw EntityNotFoundException.of("Disciplina", command.disciplinaId());
        }
        PeriodoLetivo periodo = periodoLetivoRepository.buscarPorId(periodoLetivoId)
                .orElseThrow(() -> EntityNotFoundException.of("Período Letivo", command.periodoLetivoId()));
        periodo.garantirAbertoParaOferta();

        turma.atualizar(
                command.codigo(),
                disciplinaId,
                periodoLetivoId,
                command.capacidadeMaxima()
        );
        turmaRepository.salvar(turma);
        return TurmaDto.from(turma);
    }
}
