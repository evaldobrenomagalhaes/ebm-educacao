package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.CadastrarDisciplinaCommand;
import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.valueobject.CursoId;

import java.util.Objects;

@Transactional
public class CadastrarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;

    public CadastrarDisciplinaUseCase(
            DisciplinaRepository disciplinaRepository,
            CursoRepository cursoRepository
    ) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public DisciplinaDto executar(CadastrarDisciplinaCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        CursoId cursoId = CursoId.de(command.cursoId());
        garantirCursoExistente(cursoId, command.cursoId());
        Disciplina disciplina = Disciplina.cadastrar(command.nome(), command.codigo(), cursoId);
        disciplinaRepository.salvar(disciplina);
        return DisciplinaDto.from(disciplina);
    }

    private void garantirCursoExistente(CursoId cursoId, Object idOriginal) {
        if (cursoRepository.buscarPorId(cursoId).isEmpty()) {
            throw EntityNotFoundException.of("Curso", idOriginal);
        }
    }
}
