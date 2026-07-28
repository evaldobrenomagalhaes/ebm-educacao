package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.AtualizarCursoCommand;
import br.com.academico.application.dto.CursoDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.valueobject.CursoId;

import java.util.Objects;

@Transactional
public class AtualizarCursoUseCase {

    private final CursoRepository cursoRepository;

    public AtualizarCursoUseCase(CursoRepository cursoRepository) {
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public CursoDto executar(AtualizarCursoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        CursoId id = CursoId.de(command.id());
        Curso curso = cursoRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Curso", command.id()));
        curso.atualizar(command.nome(), command.situacao());
        cursoRepository.salvar(curso);
        return CursoDto.from(curso);
    }
}
