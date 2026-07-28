package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarDisciplinaCommand;
import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
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
class AtualizarDisciplinaUseCaseTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private CursoRepository cursoRepository;

    private AtualizarDisciplinaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarDisciplinaUseCase(disciplinaRepository, cursoRepository);
    }

    @Test
    void deveAtualizarDisciplina() {
        Curso curso = Curso.cadastrar("ADS", SituacaoCurso.ATIVO);
        Disciplina disciplina = Disciplina.cadastrar("POO", "POO1", curso.getId());
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));
        when(cursoRepository.buscarPorId(curso.getId())).thenReturn(Optional.of(curso));

        DisciplinaDto dto = useCase.executar(new AtualizarDisciplinaCommand(
                disciplina.getId().valor(), "POO II", "POO2", curso.getId().valor()));

        assertThat(dto.nome()).isEqualTo("POO II");
        assertThat(dto.codigo()).isEqualTo("POO2");
        verify(disciplinaRepository).salvar(disciplina);
    }

    @Test
    void deveFalharQuandoDisciplinaNaoExiste() {
        when(disciplinaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarDisciplinaCommand(
                UUID.randomUUID(), "POO", "POO1", UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Disciplina");
    }

    @Test
    void deveFalharQuandoCursoNaoExiste() {
        Curso curso = Curso.cadastrar("ADS", SituacaoCurso.ATIVO);
        Disciplina disciplina = Disciplina.cadastrar("POO", "POO1", curso.getId());
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));
        when(cursoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarDisciplinaCommand(
                disciplina.getId().valor(), "POO", "POO1", UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Curso");
    }
}
