package br.com.academico.application.usecase;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.ConsultarMatriculasPorAlunoQuery;
import br.com.academico.application.query.ConsultarMatriculasPorTurmaQuery;
import br.com.academico.application.query.ConsultarTurmasDisponiveisQuery;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.StatusTurma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarUseCasesTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private TurmaRepository turmaRepository;
    @Mock
    private ListarTurmasUseCase listarTurmasUseCase;

    private ConsultarMatriculasPorAlunoUseCase consultarPorAluno;
    private ConsultarMatriculasPorTurmaUseCase consultarPorTurma;
    private ConsultarTurmasDisponiveisUseCase consultarDisponiveis;

    private DisciplinaId disciplinaId;
    private PeriodoLetivoId periodoId;
    private Aluno aluno;
    private Turma turma;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        consultarPorAluno = new ConsultarMatriculasPorAlunoUseCase(
                matriculaRepository, alunoRepository, turmaRepository);
        consultarPorTurma = new ConsultarMatriculasPorTurmaUseCase(matriculaRepository, turmaRepository);
        consultarDisponiveis = new ConsultarTurmasDisponiveisUseCase(listarTurmasUseCase);

        disciplinaId = DisciplinaId.novo();
        periodoId = PeriodoLetivoId.novo();
        aluno = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        turma = Turma.cadastrar("T1", disciplinaId, periodoId, 30, StatusTurma.ABERTA);
        matricula = Matricula.realizar(aluno.getId(), turma.getId());
    }

    @Test
    void deveConsultarMatriculasPorAluno() {
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));
        when(turmaRepository.listar()).thenReturn(List.of(turma));
        when(matriculaRepository.listarPorAluno(aluno.getId())).thenReturn(List.of(matricula));

        List<MatriculaDto> resultado = consultarPorAluno.executar(
                ConsultarMatriculasPorAlunoQuery.doAluno(aluno.getId().valor()));

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().alunoId()).isEqualTo(aluno.getId().valor());
    }

    @Test
    void consultarPorAlunoDeveFalharQuandoAlunoNaoExiste() {
        when(alunoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultarPorAluno.executar(
                ConsultarMatriculasPorAlunoQuery.doAluno(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void consultarPorAlunoDeveFiltrarPorStatusETurma() {
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));
        when(turmaRepository.listar()).thenReturn(List.of(turma));
        when(matriculaRepository.listarPorAluno(aluno.getId())).thenReturn(List.of(matricula));

        assertThat(consultarPorAluno.executar(new ConsultarMatriculasPorAlunoQuery(
                aluno.getId().valor(), StatusMatricula.CONFIRMADA, null, null))).isEmpty();

        assertThat(consultarPorAluno.executar(new ConsultarMatriculasPorAlunoQuery(
                aluno.getId().valor(), StatusMatricula.PENDENTE, periodoId.valor(), disciplinaId.valor())))
                .hasSize(1);

        assertThat(consultarPorAluno.executar(new ConsultarMatriculasPorAlunoQuery(
                aluno.getId().valor(), null, UUID.randomUUID(), null))).isEmpty();
    }

    @Test
    void consultarPorAlunoIgnoraMatriculaSemTurmaNoIndice() {
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));
        when(turmaRepository.listar()).thenReturn(List.of());
        when(matriculaRepository.listarPorAluno(aluno.getId())).thenReturn(List.of(matricula));

        assertThat(consultarPorAluno.executar(new ConsultarMatriculasPorAlunoQuery(
                aluno.getId().valor(), null, periodoId.valor(), null))).isEmpty();
    }

    @Test
    void deveConsultarMatriculasPorTurma() {
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(matriculaRepository.listarPorTurma(turma.getId())).thenReturn(List.of(matricula));

        List<MatriculaDto> resultado = consultarPorTurma.executar(
                ConsultarMatriculasPorTurmaQuery.daTurma(turma.getId().valor()));

        assertThat(resultado).hasSize(1);
    }

    @Test
    void consultarPorTurmaDeveFalharQuandoTurmaNaoExiste() {
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultarPorTurma.executar(
                ConsultarMatriculasPorTurmaQuery.daTurma(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void consultarPorTurmaRetornaVazioQuandoFiltroDeTurmaNaoBate() {
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        assertThat(consultarPorTurma.executar(new ConsultarMatriculasPorTurmaQuery(
                turma.getId().valor(), null, UUID.randomUUID(), null))).isEmpty();

        assertThat(consultarPorTurma.executar(new ConsultarMatriculasPorTurmaQuery(
                turma.getId().valor(), null, null, UUID.randomUUID()))).isEmpty();
    }

    @Test
    void consultarPorTurmaFiltraPorStatus() {
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(matriculaRepository.listarPorTurma(turma.getId())).thenReturn(List.of(matricula));

        assertThat(consultarPorTurma.executar(new ConsultarMatriculasPorTurmaQuery(
                turma.getId().valor(), StatusMatricula.CONFIRMADA, null, null))).isEmpty();

        assertThat(consultarPorTurma.executar(new ConsultarMatriculasPorTurmaQuery(
                turma.getId().valor(), StatusMatricula.PENDENTE, periodoId.valor(), disciplinaId.valor())))
                .hasSize(1);
    }

    @Test
    void deveConsultarTurmasDisponiveis() {
        TurmaDto dto = TurmaDto.from(turma);
        when(listarTurmasUseCase.executar(any())).thenReturn(List.of(dto));

        List<TurmaDto> resultado = consultarDisponiveis.executar(
                new ConsultarTurmasDisponiveisQuery(disciplinaId.valor(), periodoId.valor()));

        assertThat(resultado).containsExactly(dto);
    }

    @Test
    void consultarDisponiveisComTodas() {
        when(listarTurmasUseCase.executar(any())).thenReturn(List.of());

        assertThat(consultarDisponiveis.executar(ConsultarTurmasDisponiveisQuery.todas())).isEmpty();
    }
}
