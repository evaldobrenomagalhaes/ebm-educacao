package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarCursoCommand;
import br.com.academico.application.dto.CursoDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.valueobject.SituacaoCurso;
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
class AtualizarCursoUseCaseTest {

    @Mock
    private CursoRepository cursoRepository;

    private AtualizarCursoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarCursoUseCase(cursoRepository);
    }

    @Test
    void deveAtualizarCurso() {
        Curso curso = Curso.cadastrar("ADS", SituacaoCurso.ATIVO);
        when(cursoRepository.buscarPorId(curso.getId())).thenReturn(Optional.of(curso));

        CursoDto dto = useCase.executar(new AtualizarCursoCommand(
                curso.getId().valor(), "ADS Atualizado", SituacaoCurso.INATIVO));

        assertThat(dto.nome()).isEqualTo("ADS Atualizado");
        assertThat(dto.situacao()).isEqualTo(SituacaoCurso.INATIVO);
        verify(cursoRepository).salvar(curso);
    }

    @Test
    void deveFalharQuandoCursoNaoExiste() {
        when(cursoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarCursoCommand(
                UUID.randomUUID(), "ADS", SituacaoCurso.ATIVO)))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
