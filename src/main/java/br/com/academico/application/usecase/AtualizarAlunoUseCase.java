package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarAlunoCommand;
import br.com.academico.application.dto.AlunoDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.Email;

import java.util.Objects;

public final class AtualizarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public AtualizarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
    }

    public AlunoDto executar(AtualizarAlunoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        AlunoId id = AlunoId.de(command.id());
        Aluno aluno = alunoRepository.buscarPorId(id)
                .orElseThrow(() -> EntityNotFoundException.of("Aluno", command.id()));
        aluno.atualizar(command.nome(), Email.de(command.email()), command.situacaoAcademica());
        alunoRepository.salvar(aluno);
        return AlunoDto.from(aluno);
    }
}
