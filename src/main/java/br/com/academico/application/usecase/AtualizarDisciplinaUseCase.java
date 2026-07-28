package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.AtualizarDisciplinaCommand;
import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.valueobject.CursoId;
import br.com.academico.domain.valueobject.DisciplinaId;

import java.util.Objects;

@Transactional
public class AtualizarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;

    public AtualizarDisciplinaUseCase(
            DisciplinaRepository disciplinaRepository,
            CursoRepository cursoRepository
    ) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public DisciplinaDto executar(AtualizarDisciplinaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        DisciplinaId id = DisciplinaId.de(command.id());
        Disciplina disciplina = disciplinaRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Disciplina", command.id()));
        CursoId cursoId = CursoId.de(command.cursoId());
        if (cursoRepository.buscarPorId(cursoId).isEmpty()) {
            throw EntityNotFoundException.of("Curso", command.cursoId());
        }
        disciplina.atualizar(command.nome(), command.codigo(), cursoId);
        disciplinaRepository.salvar(disciplina);
        return DisciplinaDto.from(disciplina);
    }
}
