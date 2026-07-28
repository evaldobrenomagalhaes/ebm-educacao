package br.com.academico.domain.model;

import br.com.academico.domain.event.MatriculaCancelada;
import br.com.academico.domain.event.MatriculaConfirmada;
import br.com.academico.domain.event.MatriculaRealizada;
import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.TurmaId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;

/**
 * Aggregate Root do vínculo aluno–turma e do ciclo de vida da matrícula (doc 07).
 * Status inicial: {@link StatusMatricula#PENDENTE} (RN-05 / PA-01).
 * Transições: PENDENTE → CONFIRMADA → CANCELADA (estado final, PA-04).
 */
@Entity
@Table(
        name = "LY_MATRICULA",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_matricula_aluno_turma",
                columnNames = {"aluno_id", "turma_id"}
        )
)
public class Matricula extends AggregateRoot {

    @EmbeddedId
    @AttributeOverride(name = "valor", column = @Column(name = "id"))
    private MatriculaId id;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "aluno_id", nullable = false))
    private AlunoId alunoId;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "turma_id", nullable = false))
    private TurmaId turmaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusMatricula status;

    protected Matricula() {
    }

    /**
     * Realiza uma nova matrícula em status PENDENTE (caso de uso RealizarMatricula).
     */
    public static Matricula realizar(AlunoId alunoId, TurmaId turmaId) {
        Objects.requireNonNull(alunoId, "Aluno é obrigatório");
        Objects.requireNonNull(turmaId, "Turma é obrigatória");

        Matricula matricula = new Matricula();
        matricula.id = MatriculaId.novo();
        matricula.alunoId = alunoId;
        matricula.turmaId = turmaId;
        matricula.status = StatusMatricula.PENDENTE;
        matricula.registrarEvento(new MatriculaRealizada(matricula.id, alunoId, turmaId));
        return matricula;
    }

    /**
     * Confirma a matrícula. O consumo de vaga fica a cargo da {@link Turma} na orquestração do use case (INV-06).
     */
    public void confirmar() {
        if (status != StatusMatricula.PENDENTE) {
            throw new BusinessRuleViolationException(
                    "Somente matrícula pendente pode ser confirmada. Status atual: " + status
            );
        }
        this.status = StatusMatricula.CONFIRMADA;
        registrarEvento(new MatriculaConfirmada(id, alunoId, turmaId));
    }

    /**
     * Cancela uma matrícula confirmada. A liberação de vaga fica a cargo da {@link Turma} (INV-05).
     * Cancelamento é estado final (PA-04).
     */
    public void cancelar() {
        if (status != StatusMatricula.CONFIRMADA) {
            throw new BusinessRuleViolationException(
                    "Somente matrícula confirmada pode ser cancelada. Status atual: " + status
            );
        }
        this.status = StatusMatricula.CANCELADA;
        registrarEvento(new MatriculaCancelada(id, alunoId, turmaId));
    }

    public boolean estaPendente() {
        return status == StatusMatricula.PENDENTE;
    }

    public boolean estaConfirmada() {
        return status == StatusMatricula.CONFIRMADA;
    }

    public boolean estaCancelada() {
        return status == StatusMatricula.CANCELADA;
    }

    public MatriculaId getId() {
        return id;
    }

    public AlunoId getAlunoId() {
        return alunoId;
    }

    public TurmaId getTurmaId() {
        return turmaId;
    }

    public StatusMatricula getStatus() {
        return status;
    }
}
