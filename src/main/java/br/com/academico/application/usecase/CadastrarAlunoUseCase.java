package br.com.academico.application.usecase;

import br.com.academico.application.command.CadastrarAlunoCommand;
import br.com.academico.application.dto.AlunoDto;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.Email;

import java.util.Objects;

public final class CadastrarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public CadastrarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
    }

    public AlunoDto executar(CadastrarAlunoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        Aluno aluno = Aluno.cadastrar(
                command.nome(),
                Email.de(command.email()),
                command.situacaoAcademica()
        );
        alunoRepository.salvar(aluno);
        return AlunoDto.from(aluno);
    }
}
