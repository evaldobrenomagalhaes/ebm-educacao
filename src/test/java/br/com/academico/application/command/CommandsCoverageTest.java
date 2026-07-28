package br.com.academico.application.command;

import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.SituacaoCurso;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import br.com.academico.domain.valueobject.StatusTurma;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CommandsCoverageTest {

    @Test
    void deveInstanciarTodosOsCommands() {
        UUID id = UUID.randomUUID();
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate termino = LocalDate.of(2026, 6, 30);

        AbrirTurmaCommand abrir = new AbrirTurmaCommand(id);
        assertThat(abrir.turmaId()).isEqualTo(id);

        FecharTurmaCommand fechar = new FecharTurmaCommand(id);
        assertThat(fechar.turmaId()).isEqualTo(id);

        CadastrarAlunoCommand cadastrarAluno = new CadastrarAlunoCommand(
                "Ana", "ana@email.com", SituacaoAcademica.ATIVO);
        assertThat(cadastrarAluno.nome()).isEqualTo("Ana");
        assertThat(cadastrarAluno.email()).isEqualTo("ana@email.com");
        assertThat(cadastrarAluno.situacaoAcademica()).isEqualTo(SituacaoAcademica.ATIVO);

        AtualizarAlunoCommand atualizarAluno = new AtualizarAlunoCommand(
                id, "Ana", "ana@email.com", SituacaoAcademica.INATIVO);
        assertThat(atualizarAluno.id()).isEqualTo(id);
        assertThat(atualizarAluno.situacaoAcademica()).isEqualTo(SituacaoAcademica.INATIVO);

        ExcluirAlunoCommand excluirAluno = new ExcluirAlunoCommand(id);
        assertThat(excluirAluno.id()).isEqualTo(id);

        CadastrarCursoCommand cadastrarCurso = new CadastrarCursoCommand("ADS", SituacaoCurso.ATIVO);
        assertThat(cadastrarCurso.nome()).isEqualTo("ADS");
        assertThat(cadastrarCurso.situacao()).isEqualTo(SituacaoCurso.ATIVO);

        AtualizarCursoCommand atualizarCurso = new AtualizarCursoCommand(id, "ADS", SituacaoCurso.INATIVO);
        assertThat(atualizarCurso.id()).isEqualTo(id);

        ExcluirCursoCommand excluirCurso = new ExcluirCursoCommand(id);
        assertThat(excluirCurso.id()).isEqualTo(id);

        CadastrarDisciplinaCommand cadastrarDisc = new CadastrarDisciplinaCommand("POO", "POO1", id);
        assertThat(cadastrarDisc.codigo()).isEqualTo("POO1");
        assertThat(cadastrarDisc.cursoId()).isEqualTo(id);

        AtualizarDisciplinaCommand atualizarDisc = new AtualizarDisciplinaCommand(id, "POO", "POO1", id);
        assertThat(atualizarDisc.nome()).isEqualTo("POO");

        ExcluirDisciplinaCommand excluirDisc = new ExcluirDisciplinaCommand(id);
        assertThat(excluirDisc.id()).isEqualTo(id);

        CadastrarPeriodoLetivoCommand cadastrarPeriodo = new CadastrarPeriodoLetivoCommand(
                "2026.1", inicio, termino, SituacaoPeriodoLetivo.ABERTO);
        assertThat(cadastrarPeriodo.codigo()).isEqualTo("2026.1");
        assertThat(cadastrarPeriodo.dataInicio()).isEqualTo(inicio);
        assertThat(cadastrarPeriodo.dataTermino()).isEqualTo(termino);
        assertThat(cadastrarPeriodo.situacao()).isEqualTo(SituacaoPeriodoLetivo.ABERTO);

        AtualizarPeriodoLetivoCommand atualizarPeriodo = new AtualizarPeriodoLetivoCommand(
                id, "2026.1", inicio, termino, SituacaoPeriodoLetivo.ENCERRADO);
        assertThat(atualizarPeriodo.id()).isEqualTo(id);

        ExcluirPeriodoLetivoCommand excluirPeriodo = new ExcluirPeriodoLetivoCommand(id);
        assertThat(excluirPeriodo.id()).isEqualTo(id);

        CadastrarTurmaCommand cadastrarTurma = new CadastrarTurmaCommand(
                "T1", id, id, 30, StatusTurma.ABERTA);
        assertThat(cadastrarTurma.capacidadeMaxima()).isEqualTo(30);
        assertThat(cadastrarTurma.status()).isEqualTo(StatusTurma.ABERTA);

        AtualizarTurmaCommand atualizarTurma = new AtualizarTurmaCommand(id, "T1", id, id, 40);
        assertThat(atualizarTurma.capacidadeMaxima()).isEqualTo(40);

        ExcluirTurmaCommand excluirTurma = new ExcluirTurmaCommand(id);
        assertThat(excluirTurma.id()).isEqualTo(id);

        RealizarMatriculaCommand realizar = new RealizarMatriculaCommand(id, id);
        assertThat(realizar.alunoId()).isEqualTo(id);
        assertThat(realizar.turmaId()).isEqualTo(id);

        ConfirmarMatriculaCommand confirmar = new ConfirmarMatriculaCommand(id);
        assertThat(confirmar.matriculaId()).isEqualTo(id);

        CancelarMatriculaCommand cancelar = new CancelarMatriculaCommand(id);
        assertThat(cancelar.matriculaId()).isEqualTo(id);
    }
}
