package br.com.academico.application.usecase;

import br.com.academico.application.dto.AlunoDto;
import br.com.academico.application.dto.CursoDto;
import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.application.query.BuscarAlunoPorIdQuery;
import br.com.academico.application.query.BuscarCursoPorIdQuery;
import br.com.academico.application.query.BuscarDisciplinaPorIdQuery;
import br.com.academico.application.query.BuscarPeriodoLetivoPorIdQuery;
import br.com.academico.application.query.BuscarTurmaPorIdQuery;
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
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarUseCasesTest {

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

    private BuscarAlunoPorIdUseCase buscarAluno;
    private BuscarCursoPorIdUseCase buscarCurso;
    private BuscarDisciplinaPorIdUseCase buscarDisciplina;
    private BuscarPeriodoLetivoPorIdUseCase buscarPeriodo;
    private BuscarTurmaPorIdUseCase buscarTurma;

    @BeforeEach
    void setUp() {
        buscarAluno = new BuscarAlunoPorIdUseCase(alunoRepository);
        buscarCurso = new BuscarCursoPorIdUseCase(cursoRepository);
        buscarDisciplina = new BuscarDisciplinaPorIdUseCase(disciplinaRepository);
        buscarPeriodo = new BuscarPeriodoLetivoPorIdUseCase(periodoLetivoRepository);
        buscarTurma = new BuscarTurmaPorIdUseCase(turmaRepository);
    }

    @Test
    void deveBuscarAlunoPorId() {
        Aluno aluno = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));

        AlunoDto dto = buscarAluno.executar(new BuscarAlunoPorIdQuery(aluno.getId().valor()));

        assertThat(dto.nome()).isEqualTo("Ana");
    }

    @Test
    void buscarAlunoDeveFalharQuandoNaoExiste() {
        when(alunoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarAluno.executar(new BuscarAlunoPorIdQuery(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveBuscarCursoPorId() {
        Curso curso = Curso.cadastrar("ADS", SituacaoCurso.ATIVO);
        when(cursoRepository.buscarPorId(curso.getId())).thenReturn(Optional.of(curso));

        CursoDto dto = buscarCurso.executar(new BuscarCursoPorIdQuery(curso.getId().valor()));

        assertThat(dto.nome()).isEqualTo("ADS");
    }

    @Test
    void buscarCursoDeveFalharQuandoNaoExiste() {
        when(cursoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarCurso.executar(new BuscarCursoPorIdQuery(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveBuscarDisciplinaPorId() {
        Disciplina disciplina = Disciplina.cadastrar("POO", "POO1", CursoId.novo());
        when(disciplinaRepository.buscarPorId(disciplina.getId())).thenReturn(Optional.of(disciplina));

        DisciplinaDto dto = buscarDisciplina.executar(new BuscarDisciplinaPorIdQuery(disciplina.getId().valor()));

        assertThat(dto.codigo()).isEqualTo("POO1");
    }

    @Test
    void buscarDisciplinaDeveFalharQuandoNaoExiste() {
        when(disciplinaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarDisciplina.executar(new BuscarDisciplinaPorIdQuery(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveBuscarPeriodoPorId() {
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2026.1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), SituacaoPeriodoLetivo.ABERTO);
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        PeriodoLetivoDto dto = buscarPeriodo.executar(new BuscarPeriodoLetivoPorIdQuery(periodo.getId().valor()));

        assertThat(dto.codigo()).isEqualTo("2026.1");
    }

    @Test
    void buscarPeriodoDeveFalharQuandoNaoExiste() {
        when(periodoLetivoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarPeriodo.executar(new BuscarPeriodoLetivoPorIdQuery(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveBuscarTurmaPorId() {
        Turma turma = Turma.cadastrar("T1", DisciplinaId.novo(), PeriodoLetivoId.novo(), 30, StatusTurma.ABERTA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        TurmaDto dto = buscarTurma.executar(new BuscarTurmaPorIdQuery(turma.getId().valor()));

        assertThat(dto.codigo()).isEqualTo("T1");
    }

    @Test
    void buscarTurmaDeveFalharQuandoNaoExiste() {
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarTurma.executar(new BuscarTurmaPorIdQuery(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
