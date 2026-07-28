package br.com.academico.application.usecase;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.application.query.ListarMatriculasQuery;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarMatriculasUseCaseTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private TurmaRepository turmaRepository;

    private ListarMatriculasUseCase useCase;

    private DisciplinaId disciplinaId;
    private PeriodoLetivoId periodoId;
    private Aluno aluno1;
    private Aluno aluno2;
    private Turma turma1;
    private Turma turma2;
    private Matricula matricula1;
    private Matricula matricula2;

    @BeforeEach
    void setUp() {
        useCase = new ListarMatriculasUseCase(matriculaRepository, turmaRepository);

        disciplinaId = DisciplinaId.novo();
        periodoId = PeriodoLetivoId.novo();
        aluno1 = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        aluno2 = Aluno.cadastrar("Bruno", Email.de("bruno@email.com"), SituacaoAcademica.ATIVO);
        turma1 = Turma.cadastrar("T1", disciplinaId, periodoId, 30, StatusTurma.ABERTA);
        turma2 = Turma.cadastrar(
                "T2", DisciplinaId.novo(), PeriodoLetivoId.novo(), 20, StatusTurma.ABERTA);
        matricula1 = Matricula.realizar(aluno1.getId(), turma1.getId());
        matricula2 = Matricula.realizar(aluno2.getId(), turma2.getId());

        when(matriculaRepository.listar()).thenReturn(List.of(matricula1, matricula2));
        when(turmaRepository.listar()).thenReturn(List.of(turma1, turma2));
    }

    @Test
    void deveListarTodasComFiltroNulo() {
        List<MatriculaDto> resultado = useCase.executar(ListarMatriculasQuery.todas());

        assertThat(resultado).hasSize(2);
    }

    @Test
    void deveFiltrarPorStatus() {
        assertThat(useCase.executar(new ListarMatriculasQuery(
                StatusMatricula.PENDENTE, null, null, null, null))).hasSize(2);

        assertThat(useCase.executar(new ListarMatriculasQuery(
                StatusMatricula.CONFIRMADA, null, null, null, null))).isEmpty();
    }

    @Test
    void deveFiltrarPorAlunoId() {
        List<MatriculaDto> resultado = useCase.executar(new ListarMatriculasQuery(
                null, aluno1.getId().valor(), null, null, null));

        assertThat(resultado).extracting(MatriculaDto::alunoId)
                .containsExactly(aluno1.getId().valor());
    }

    @Test
    void deveFiltrarPorTurmaId() {
        List<MatriculaDto> resultado = useCase.executar(new ListarMatriculasQuery(
                null, null, turma2.getId().valor(), null, null));

        assertThat(resultado).extracting(MatriculaDto::turmaId)
                .containsExactly(turma2.getId().valor());
    }

    @Test
    void deveFiltrarPorPeriodoLetivoEDisciplina() {
        assertThat(useCase.executar(new ListarMatriculasQuery(
                null, null, null, periodoId.valor(), disciplinaId.valor())))
                .extracting(MatriculaDto::id)
                .containsExactly(matricula1.getId().valor());

        assertThat(useCase.executar(new ListarMatriculasQuery(
                null, null, null, UUID.randomUUID(), null))).isEmpty();

        assertThat(useCase.executar(new ListarMatriculasQuery(
                null, null, null, null, UUID.randomUUID()))).isEmpty();
    }

    @Test
    void deveExcluirMatriculaSemTurmaNoIndiceQuandoFiltroIndireto() {
        when(turmaRepository.listar()).thenReturn(List.of());

        assertThat(useCase.executar(new ListarMatriculasQuery(
                null, null, null, periodoId.valor(), null))).isEmpty();
    }
}
