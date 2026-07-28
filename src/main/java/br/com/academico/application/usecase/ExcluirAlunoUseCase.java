package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.command.ExcluirAlunoCommand;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.AlunoId;

import java.util.Objects;

@Transactional
public class ExcluirAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public ExcluirAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
    }

    public void executar(ExcluirAlunoCommand command) {
        Objects.requireNonNull(command, "Command é obrigatório");
        AlunoId id = AlunoId.de(command.id());
        if (alunoRepository.buscarPorId(id).isEmpty()) {
            throw EntityNotFoundException.of("Aluno", command.id());
        }
        alunoRepository.excluir(id);
    }
}
