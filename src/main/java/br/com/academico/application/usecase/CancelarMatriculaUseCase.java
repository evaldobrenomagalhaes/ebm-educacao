package br.com.academico.application.usecase;

import br.com.academico.application.command.CancelarMatriculaCommand;
import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.MatriculaId;

import java.util.Objects;

public final class CancelarMatriculaUseCase {

    private final MatriculaRepository matriculaRepository;
    private final TurmaRepository turmaRepository;

    public CancelarMatriculaUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository
    ) {
        this.matriculaRepository = Objects.requireNonNull(matriculaRepository);
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public MatriculaDto executar(CancelarMatriculaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        MatriculaId matriculaId = MatriculaId.de(command.matriculaId());

        Matricula matricula = matriculaRepository.buscarPorId(matriculaId)
                .orElseThrow(() -> EntityNotFoundException.of("Matrícula", command.matriculaId()));
        Turma turma = turmaRepository.buscarPorId(matricula.getTurmaId())
                .orElseThrow(() -> EntityNotFoundException.of("Turma", matricula.getTurmaId().valor()));

        matricula.cancelar();
        turma.liberarVaga();

        matriculaRepository.salvar(matricula);
        turmaRepository.salvar(turma);
        return MatriculaDto.from(matricula);
    }
}
