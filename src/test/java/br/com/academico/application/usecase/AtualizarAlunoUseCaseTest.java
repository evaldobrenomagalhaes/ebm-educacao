package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarAlunoCommand;
import br.com.academico.application.dto.AlunoDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarAlunoUseCaseTest {

    @Mock
    private AlunoRepository alunoRepository;

    private AtualizarAlunoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarAlunoUseCase(alunoRepository);
    }

    @Test
    void deveAtualizarAluno() {
        Aluno aluno = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));

        AlunoDto dto = useCase.executar(new AtualizarAlunoCommand(
                aluno.getId().valor(), "Ana Silva", "ana.silva@email.com", SituacaoAcademica.INATIVO));

        assertThat(dto.nome()).isEqualTo("Ana Silva");
        assertThat(dto.email()).isEqualTo("ana.silva@email.com");
        assertThat(dto.situacaoAcademica()).isEqualTo(SituacaoAcademica.INATIVO);
        verify(alunoRepository).salvar(aluno);
    }

    @Test
    void deveFalharQuandoAlunoNaoExiste() {
        when(alunoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarAlunoCommand(
                UUID.randomUUID(), "Ana", "ana@email.com", SituacaoAcademica.ATIVO)))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
