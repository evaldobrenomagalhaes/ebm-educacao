package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarTurmaCommand;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.exception.PeriodoLetivoEncerradoException;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.CursoId;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import br.com.academico.domain.valueobject.StatusTurma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarTurmaUseCaseTest {

    @Mock
    private TurmaRepository turmaRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private PeriodoLetivoRepository periodoLetivoRepository;

    private AtualizarTurmaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarTurmaUseCase(turmaRepository, disciplinaRepository, periodoLetivoRepository);
    }

    @Test
    void deveAtualizarTurma() {
        Disciplina disciplina = Disciplina.cadastrar("POO", "POO1", CursoId.novo());
        PeriodoLetivo periodo = periodoAberto();
        Turma turma = Turma.cadastrar(
                "T1", disciplina.getId(), periodo.getId(), 30, StatusTurma.ABERTA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        TurmaDto dto = useCase.executar(new AtualizarTurmaCommand(
                turma.getId().valor(),
                "T2",
                disciplina.getId().valor(),
                periodo.getId().valor(),
                40));

        assertThat(dto.codigo()).isEqualTo("T2");
        assertThat(dto.capacidadeMaxima()).isEqualTo(40);
        verify(turmaRepository).salvar(turma);
    }

    @Test
    void deveFalharQuandoTurmaNaoExiste() {
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarTurmaCommand(
                UUID.randomUUID(), "T1", UUID.randomUUID(), UUID.randomUUID(), 30)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Turma");
    }

    @Test
    void deveFalharQuandoDisciplinaNaoExiste() {
        PeriodoLetivo periodo = periodoAberto();
        Turma turma = Turma.cadastrar(
                "T1", disciplinaId(), periodo.getId(), 30, StatusTurma.ABERTA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(disciplinaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarTurmaCommand(
                turma.getId().valor(), "T1", UUID.randomUUID(), periodo.getId().valor(), 30)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Disciplina");
    }

    @Test
    void deveFalharQuandoPeriodoEncerrado() {
        Disciplina disciplina = Disciplina.cadastrar("POO", "POO1", CursoId.novo());
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2025.2", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 12, 20), SituacaoPeriodoLetivo.ENCERRADO);
        Turma turma = Turma.cadastrar(
                "T1", disciplina.getId(), periodo.getId(), 30, StatusTurma.ABERTA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        assertThatThrownBy(() -> useCase.executar(new AtualizarTurmaCommand(
                turma.getId().valor(),
                "T1",
                disciplina.getId().valor(),
                periodo.getId().valor(),
                30)))
                .isInstanceOf(PeriodoLetivoEncerradoException.class);
    }

    private static PeriodoLetivo periodoAberto() {
        return PeriodoLetivo.cadastrar(
                "2026.1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), SituacaoPeriodoLetivo.ABERTO);
    }

    private static br.com.academico.domain.valueobject.DisciplinaId disciplinaId() {
        return br.com.academico.domain.valueobject.DisciplinaId.novo();
    }
}
