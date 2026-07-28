package br.com.academico.application.usecase;

import br.com.academico.application.command.CadastrarAlunoCommand;
import br.com.academico.application.command.CadastrarCursoCommand;
import br.com.academico.application.command.CadastrarDisciplinaCommand;
import br.com.academico.application.command.CadastrarPeriodoLetivoCommand;
import br.com.academico.application.command.CadastrarTurmaCommand;
import br.com.academico.application.dto.AlunoDto;
import br.com.academico.application.dto.CursoDto;
import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.exception.PeriodoLetivoEncerradoException;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.SituacaoCurso;
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
class CadastrarUseCasesTest {

    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private CursoRepository cursoRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private PeriodoLetivoRepository periodoLetivoRepository;
    @Mock
    private TurmaRepository turmaRepository;

    private CadastrarAlunoUseCase cadastrarAluno;
    private CadastrarCursoUseCase cadastrarCurso;
    private CadastrarDisciplinaUseCase cadastrarDisciplina;
    private CadastrarPeriodoLetivoUseCase cadastrarPeriodo;
    private CadastrarTurmaUseCase cadastrarTurma;

    @BeforeEach
    void setUp() {
        cadastrarAluno = new CadastrarAlunoUseCase(alunoRepository);
        cadastrarCurso = new CadastrarCursoUseCase(cursoRepository);
        cadastrarDisciplina = new CadastrarDisciplinaUseCase(disciplinaRepository, cursoRepository);
        cadastrarPeriodo = new CadastrarPeriodoLetivoUseCase(periodoLetivoRepository);
        cadastrarTurma = new CadastrarTurmaUseCase(turmaRepository, disciplinaRepository, periodoLetivoRepository);
    }

    @Test
    void deveCadastrarAluno() {
        AlunoDto dto = cadastrarAluno.executar(
                new CadastrarAlunoCommand("Ana", "ana@email.com", SituacaoAcademica.ATIVO));

        assertThat(dto.nome()).isEqualTo("Ana");
        verify(alunoRepository).salvar(any());
    }

    @Test
    void deveCadastrarCurso() {
        CursoDto dto = cadastrarCurso.executar(new CadastrarCursoCommand("ADS", SituacaoCurso.ATIVO));

        assertThat(dto.nome()).isEqualTo("ADS");
        verify(cursoRepository).salvar(any());
    }

    @Test
    void deveCadastrarDisciplina() {
        Curso curso = Curso.cadastrar("ADS", SituacaoCurso.ATIVO);
        when(cursoRepository.buscarPorId(curso.getId())).thenReturn(Optional.of(curso));

        DisciplinaDto dto = cadastrarDisciplina.executar(
                new CadastrarDisciplinaCommand("POO", "POO1", curso.getId().valor()));

        assertThat(dto.codigo()).isEqualTo("POO1");
        verify(disciplinaRepository).salvar(any());
    }

    @Test
    void cadastrarDisciplinaDeveFalharQuandoCursoNaoExiste() {
        when(cursoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cadastrarDisciplina.executar(
                new CadastrarDisciplinaCommand("POO", "POO1", UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveCadastrarPeriodoLetivo() {
        PeriodoLetivoDto dto = cadastrarPeriodo.executar(new CadastrarPeriodoLetivoCommand(
                "2026.1",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                SituacaoPeriodoLetivo.ABERTO));

        assertThat(dto.codigo()).isEqualTo("2026.1");
        verify(periodoLetivoRepository).salvar(any());
    }

    @Test
    void deveCadastrarTurma() {
        Disciplina disciplina = Disciplina.cadastrar(
                "POO", "POO1", br.com.academico.domain.valueobject.CursoId.novo());
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2026.1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), SituacaoPeriodoLetivo.ABERTO);
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        TurmaDto dto = cadastrarTurma.executar(new CadastrarTurmaCommand(
                "T1",
                disciplina.getId().valor(),
                periodo.getId().valor(),
                30,
                StatusTurma.ABERTA));

        assertThat(dto.codigo()).isEqualTo("T1");
        verify(turmaRepository).salvar(any());
    }

    @Test
    void cadastrarTurmaDeveFalharQuandoDisciplinaNaoExiste() {
        when(disciplinaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cadastrarTurma.executar(new CadastrarTurmaCommand(
                "T1", UUID.randomUUID(), UUID.randomUUID(), 30, StatusTurma.ABERTA)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Disciplina");
    }

    @Test
    void cadastrarTurmaDeveFalharQuandoPeriodoEncerrado() {
        Disciplina disciplina = Disciplina.cadastrar(
                "POO", "POO1", br.com.academico.domain.valueobject.CursoId.novo());
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2025.2", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 12, 20), SituacaoPeriodoLetivo.ENCERRADO);
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        assertThatThrownBy(() -> cadastrarTurma.executar(new CadastrarTurmaCommand(
                "T1",
                disciplina.getId().valor(),
                periodo.getId().valor(),
                30,
                StatusTurma.ABERTA)))
                .isInstanceOf(PeriodoLetivoEncerradoException.class);
    }
}
