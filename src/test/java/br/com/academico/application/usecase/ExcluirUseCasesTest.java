package br.com.academico.application.usecase;

import br.com.academico.application.command.ExcluirAlunoCommand;
import br.com.academico.application.command.ExcluirCursoCommand;
import br.com.academico.application.command.ExcluirDisciplinaCommand;
import br.com.academico.application.command.ExcluirPeriodoLetivoCommand;
import br.com.academico.application.command.ExcluirTurmaCommand;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.CursoId;
import br.com.academico.domain.valueobject.Email;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcluirUseCasesTest {

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

    private ExcluirAlunoUseCase excluirAluno;
    private ExcluirCursoUseCase excluirCurso;
    private ExcluirDisciplinaUseCase excluirDisciplina;
    private ExcluirPeriodoLetivoUseCase excluirPeriodo;
    private ExcluirTurmaUseCase excluirTurma;

    @BeforeEach
    void setUp() {
        excluirAluno = new ExcluirAlunoUseCase(alunoRepository);
        excluirCurso = new ExcluirCursoUseCase(cursoRepository);
        excluirDisciplina = new ExcluirDisciplinaUseCase(disciplinaRepository);
        excluirPeriodo = new ExcluirPeriodoLetivoUseCase(periodoLetivoRepository);
        excluirTurma = new ExcluirTurmaUseCase(turmaRepository);
    }

    @Test
    void deveExcluirAluno() {
        Aluno aluno = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));

        excluirAluno.executar(new ExcluirAlunoCommand(aluno.getId().valor()));

        verify(alunoRepository).excluir(aluno.getId());
    }

    @Test
    void excluirAlunoDeveFalharQuandoNaoExiste() {
        when(alunoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> excluirAluno.executar(new ExcluirAlunoCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveExcluirCurso() {
        Curso curso = Curso.cadastrar("ADS", SituacaoCurso.ATIVO);
        when(cursoRepository.buscarPorId(curso.getId())).thenReturn(Optional.of(curso));

        excluirCurso.executar(new ExcluirCursoCommand(curso.getId().valor()));

        verify(cursoRepository).excluir(curso.getId());
    }

    @Test
    void excluirCursoDeveFalharQuandoNaoExiste() {
        when(cursoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> excluirCurso.executar(new ExcluirCursoCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveExcluirDisciplina() {
        Disciplina disciplina = Disciplina.cadastrar("POO", "POO1", CursoId.novo());
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));

        excluirDisciplina.executar(new ExcluirDisciplinaCommand(disciplina.getId().valor()));

        verify(disciplinaRepository).excluir(disciplina.getId());
    }

    @Test
    void excluirDisciplinaDeveFalharQuandoNaoExiste() {
        when(disciplinaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> excluirDisciplina.executar(new ExcluirDisciplinaCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveExcluirPeriodoLetivo() {
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2026.1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), SituacaoPeriodoLetivo.ABERTO);
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        excluirPeriodo.executar(new ExcluirPeriodoLetivoCommand(periodo.getId().valor()));

        verify(periodoLetivoRepository).excluir(periodo.getId());
    }

    @Test
    void excluirPeriodoDeveFalharQuandoNaoExiste() {
        when(periodoLetivoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> excluirPeriodo.executar(new ExcluirPeriodoLetivoCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveExcluirTurma() {
        Turma turma = Turma.cadastrar(
                "T1",
                br.com.academico.domain.valueobject.DisciplinaId.novo(),
                br.com.academico.domain.valueobject.PeriodoLetivoId.novo(),
                30,
                StatusTurma.ABERTA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        excluirTurma.executar(new ExcluirTurmaCommand(turma.getId().valor()));

        verify(turmaRepository).excluir(turma.getId());
    }

    @Test
    void excluirTurmaDeveFalharQuandoNaoExiste() {
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> excluirTurma.executar(new ExcluirTurmaCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
