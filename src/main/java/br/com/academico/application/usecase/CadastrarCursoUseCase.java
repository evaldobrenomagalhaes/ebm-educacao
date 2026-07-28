package br.com.academico.application.usecase;

import br.com.academico.application.command.CadastrarCursoCommand;
import br.com.academico.application.dto.CursoDto;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.repository.CursoRepository;

import java.util.Objects;

public final class CadastrarCursoUseCase {

    private final CursoRepository cursoRepository;

    public CadastrarCursoUseCase(CursoRepository cursoRepository) {
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public CursoDto executar(CadastrarCursoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        Curso curso = Curso.cadastrar(command.nome(), command.situacao());
        cursoRepository.salvar(curso);
        return CursoDto.from(curso);
    }
}
