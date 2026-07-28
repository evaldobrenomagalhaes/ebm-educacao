package br.com.academico.domain.model;

import br.com.academico.domain.event.MatriculaCancelada;
import br.com.academico.domain.event.MatriculaConfirmada;
import br.com.academico.domain.event.MatriculaRealizada;
import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.TurmaId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatriculaTest {

    private AlunoId alunoId;
    private TurmaId turmaId;

    @BeforeEach
    void setUp() {
        alunoId = AlunoId.novo();
        turmaId = TurmaId.novo();
    }

    @Test
    void deveRealizarMatriculaEmStatusPendente() {
        Matricula matricula = Matricula.realizar(alunoId, turmaId);

        assertThat(matricula.getId()).isNotNull();
        assertThat(matricula.getAlunoId()).isEqualTo(alunoId);
        assertThat(matricula.getTurmaId()).isEqualTo(turmaId);
        assertThat(matricula.getStatus()).isEqualTo(StatusMatricula.PENDENTE);
        assertThat(matricula.estaPendente()).isTrue();
        assertThat(matricula.pullDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(MatriculaRealizada.class)
                .satisfies(evento -> {
                    MatriculaRealizada realizada = (MatriculaRealizada) evento;
                    assertThat(realizada.matriculaId()).isEqualTo(matricula.getId());
                    assertThat(realizada.alunoId()).isEqualTo(alunoId);
                    assertThat(realizada.turmaId()).isEqualTo(turmaId);
                });
    }

    @Test
    void deveRejeitarRealizacaoSemAlunoOuTurma() {
        assertThatThrownBy(() -> Matricula.realizar(null, turmaId))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Aluno");

        assertThatThrownBy(() -> Matricula.realizar(alunoId, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Turma");
    }

    @Test
    void deveConfirmarMatriculaPendenteERegistrarEvento() {
        Matricula matricula = Matricula.realizar(alunoId, turmaId);
        matricula.pullDomainEvents();

        matricula.confirmar();

        assertThat(matricula.getStatus()).isEqualTo(StatusMatricula.CONFIRMADA);
        assertThat(matricula.estaConfirmada()).isTrue();
        assertThat(matricula.pullDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(MatriculaConfirmada.class);
    }

    @Test
    void naoDeveConfirmarMatriculaJaConfirmada() {
        Matricula matricula = matriculaConfirmada();

        assertThatThrownBy(matricula::confirmar)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Somente matrícula pendente")
                .hasMessageContaining("CONFIRMADA");
    }

    @Test
    void naoDeveConfirmarMatriculaCancelada() {
        Matricula matricula = matriculaCancelada();

        assertThatThrownBy(matricula::confirmar)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Somente matrícula pendente")
                .hasMessageContaining("CANCELADA");
    }

    @Test
    void deveCancelarMatriculaConfirmadaERegistrarEvento() {
        Matricula matricula = matriculaConfirmada();

        matricula.cancelar();

        assertThat(matricula.getStatus()).isEqualTo(StatusMatricula.CANCELADA);
        assertThat(matricula.estaCancelada()).isTrue();
        assertThat(matricula.pullDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(MatriculaCancelada.class);
    }

    @Test
    void naoDeveCancelarMatriculaPendente() {
        Matricula matricula = Matricula.realizar(alunoId, turmaId);

        assertThatThrownBy(matricula::cancelar)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Somente matrícula confirmada")
                .hasMessageContaining("PENDENTE");
    }

    @Test
    void cancelamentoEEstadoFinal_naoPermiteNovoCancelamento() {
        Matricula matricula = matriculaCancelada();

        assertThatThrownBy(matricula::cancelar)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Somente matrícula confirmada")
                .hasMessageContaining("CANCELADA");
    }

    @Test
    void cicloCompleto_pendenteConfirmadaCancelada() {
        Matricula matricula = Matricula.realizar(alunoId, turmaId);
        assertThat(matricula.estaPendente()).isTrue();

        matricula.confirmar();
        assertThat(matricula.estaConfirmada()).isTrue();

        matricula.cancelar();
        assertThat(matricula.estaCancelada()).isTrue();
        assertThat(matricula.estaPendente()).isFalse();
        assertThat(matricula.estaConfirmada()).isFalse();
    }

    private Matricula matriculaConfirmada() {
        Matricula matricula = Matricula.realizar(alunoId, turmaId);
        matricula.pullDomainEvents();
        matricula.confirmar();
        matricula.pullDomainEvents();
        return matricula;
    }

    private Matricula matriculaCancelada() {
        Matricula matricula = matriculaConfirmada();
        matricula.cancelar();
        matricula.pullDomainEvents();
        return matricula;
    }
}
