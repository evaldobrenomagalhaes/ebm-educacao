package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.ExcluirCursoCommand;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.valueobject.CursoId;

import java.util.Objects;

@Transactional
public class ExcluirCursoUseCase {

    private final CursoRepository cursoRepository;

    public ExcluirCursoUseCase(CursoRepository cursoRepository) {
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public void executar(ExcluirCursoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        CursoId id = CursoId.de(command.id());
        if (cursoRepository.buscarPorId(id).isEmpty()) {
            throw EntityNotFoundException.of("Curso", command.id());
        }
        cursoRepository.excluir(id);
    }
}
