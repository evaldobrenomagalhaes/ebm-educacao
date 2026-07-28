package br.com.academico.application.query;

import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.SituacaoCurso;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.StatusTurma;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QueriesCoverageTest {

    @Test
    void deveInstanciarTodasAsQueriesEFactories() {
        UUID id = UUID.randomUUID();
        LocalDate data = LocalDate.of(2026, 3, 15);

        BuscarAlunoPorIdQuery buscarAluno = new BuscarAlunoPorIdQuery(id);
        assertThat(buscarAluno.id()).isEqualTo(id);

        BuscarCursoPorIdQuery buscarCurso = new BuscarCursoPorIdQuery(id);
        assertThat(buscarCurso.id()).isEqualTo(id);

        BuscarDisciplinaPorIdQuery buscarDisc = new BuscarDisciplinaPorIdQuery(id);
        assertThat(buscarDisc.id()).isEqualTo(id);

        BuscarPeriodoLetivoPorIdQuery buscarPeriodo = new BuscarPeriodoLetivoPorIdQuery(id);
        assertThat(buscarPeriodo.id()).isEqualTo(id);

        BuscarTurmaPorIdQuery buscarTurma = new BuscarTurmaPorIdQuery(id);
        assertThat(buscarTurma.id()).isEqualTo(id);

        ListarAlunosQuery listarAlunos = new ListarAlunosQuery("Ana", "ana@", SituacaoAcademica.ATIVO);
        assertThat(listarAlunos.nome()).isEqualTo("Ana");
        assertThat(ListarAlunosQuery.todos().nome()).isNull();

        ListarCursosQuery listarCursos = new ListarCursosQuery("ADS", SituacaoCurso.ATIVO);
        assertThat(listarCursos.situacao()).isEqualTo(SituacaoCurso.ATIVO);
        assertThat(ListarCursosQuery.todos().nome()).isNull();

        ListarDisciplinasQuery listarDisc = new ListarDisciplinasQuery("POO", "POO1", id);
        assertThat(listarDisc.cursoId()).isEqualTo(id);
        assertThat(ListarDisciplinasQuery.todos().codigo()).isNull();

        ListarPeriodosLetivosQuery listarPeriodos = new ListarPeriodosLetivosQuery(
                "2026.1", SituacaoPeriodoLetivo.ABERTO, data, data, data, data, data);
        assertThat(listarPeriodos.vigenteEm()).isEqualTo(data);
        assertThat(ListarPeriodosLetivosQuery.todos().codigo()).isNull();

        ListarTurmasQuery listarTurmas = new ListarTurmasQuery("T1", StatusTurma.ABERTA, id, id, true);
        assertThat(listarTurmas.comVagas()).isTrue();
        assertThat(ListarTurmasQuery.todos().status()).isNull();

        ConsultarMatriculasPorAlunoQuery porAluno = new ConsultarMatriculasPorAlunoQuery(
                id, StatusMatricula.PENDENTE, id, id);
        assertThat(porAluno.status()).isEqualTo(StatusMatricula.PENDENTE);
        assertThat(ConsultarMatriculasPorAlunoQuery.doAluno(id).alunoId()).isEqualTo(id);

        ConsultarMatriculasPorTurmaQuery porTurma = new ConsultarMatriculasPorTurmaQuery(
                id, StatusMatricula.CONFIRMADA, id, id);
        assertThat(porTurma.turmaId()).isEqualTo(id);
        assertThat(ConsultarMatriculasPorTurmaQuery.daTurma(id).status()).isNull();

        ConsultarTurmasDisponiveisQuery disponiveis = new ConsultarTurmasDisponiveisQuery(id, id);
        assertThat(disponiveis.disciplinaId()).isEqualTo(id);
        assertThat(ConsultarTurmasDisponiveisQuery.todas().periodoLetivoId()).isNull();
    }
}
