package br.com.academico.domain.model;

import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.exception.SemVagasException;
import br.com.academico.domain.exception.TurmaEncerradaException;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.StatusTurma;
import br.com.academico.domain.valueobject.TurmaId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Aggregate Root responsável pela oferta da disciplina, capacidade e disponibilidade (doc 07).
 * Protege INV-01, INV-02 e INV-03.
 */
@Entity
@Table(name = "LY_TURMA")
public class Turma {

    @EmbeddedId
    @AttributeOverride(name = "valor", column = @Column(name = "id"))
    private TurmaId id;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "disciplina_id", nullable = false))
    private DisciplinaId disciplinaId;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "periodo_letivo_id", nullable = false))
    private PeriodoLetivoId periodoLetivoId;

    @Column(name = "capacidade_maxima", nullable = false)
    private int capacidadeMaxima;

    @Column(name = "vagas_disponiveis", nullable = false)
    private int vagasDisponiveis;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusTurma status;

    protected Turma() {
    }

    public static Turma cadastrar(
            String codigo,
            DisciplinaId disciplinaId,
            PeriodoLetivoId periodoLetivoId,
            int capacidadeMaxima,
            StatusTurma status
    ) {
        Objects.requireNonNull(disciplinaId, "Disciplina é obrigatória");
        Objects.requireNonNull(periodoLetivoId, "Período letivo é obrigatório");
        Objects.requireNonNull(status, "Status da turma é obrigatório");
        validarCapacidade(capacidadeMaxima);

        Turma turma = new Turma();
        turma.id = TurmaId.novo();
        turma.codigo = normalizarTextoObrigatorio(codigo, "Código");
        turma.disciplinaId = disciplinaId;
        turma.periodoLetivoId = periodoLetivoId;
        turma.capacidadeMaxima = capacidadeMaxima;
        turma.vagasDisponiveis = capacidadeMaxima;
        turma.status = status;
        return turma;
    }

    public void atualizar(
            String codigo,
            DisciplinaId disciplinaId,
            PeriodoLetivoId periodoLetivoId,
            int capacidadeMaxima
    ) {
        Objects.requireNonNull(disciplinaId, "Disciplina é obrigatória");
        Objects.requireNonNull(periodoLetivoId, "Período letivo é obrigatório");
        validarCapacidade(capacidadeMaxima);

        int ocupadas = this.capacidadeMaxima - this.vagasDisponiveis;
        if (capacidadeMaxima < ocupadas) {
            throw new BusinessRuleViolationException(
                    "Capacidade máxima não pode ser inferior às vagas já ocupadas"
            );
        }

        this.codigo = normalizarTextoObrigatorio(codigo, "Código");
        this.disciplinaId = disciplinaId;
        this.periodoLetivoId = periodoLetivoId;
        this.capacidadeMaxima = capacidadeMaxima;
        this.vagasDisponiveis = capacidadeMaxima - ocupadas;
        garantirInvariantesDeVagas();
    }

    public void abrir() {
        if (status == StatusTurma.ABERTA) {
            throw new BusinessRuleViolationException("Turma já está aberta");
        }
        this.status = StatusTurma.ABERTA;
    }

    public void fechar() {
        if (status == StatusTurma.FECHADA) {
            throw new BusinessRuleViolationException("Turma já está fechada");
        }
        this.status = StatusTurma.FECHADA;
    }

    /**
     * INV-03 / RN-04 — turmas fechadas não aceitam novas matrículas.
     */
    public void garantirAbertaParaMatricula() {
        if (status == StatusTurma.FECHADA) {
            throw TurmaEncerradaException.daTurma();
        }
    }

    /**
     * INV-06 — confirmação consome uma vaga.
     */
    public void consumirVaga() {
        if (vagasDisponiveis <= 0) {
            throw SemVagasException.daTurma();
        }
        this.vagasDisponiveis--;
        garantirInvariantesDeVagas();
    }

    /**
     * INV-05 — cancelamento de matrícula confirmada devolve uma vaga.
     */
    public void liberarVaga() {
        if (vagasDisponiveis >= capacidadeMaxima) {
            throw new BusinessRuleViolationException(
                    "Não há vagas ocupadas para liberar nesta turma"
            );
        }
        this.vagasDisponiveis++;
        garantirInvariantesDeVagas();
    }

    public boolean estaAberta() {
        return status == StatusTurma.ABERTA;
    }

    public boolean possuiVagas() {
        return vagasDisponiveis > 0;
    }

    private void garantirInvariantesDeVagas() {
        if (vagasDisponiveis < 0) {
            throw new BusinessRuleViolationException("Vagas disponíveis não podem ser negativas");
        }
        if (vagasDisponiveis > capacidadeMaxima) {
            throw new BusinessRuleViolationException(
                    "Vagas disponíveis não podem exceder a capacidade máxima"
            );
        }
    }

    private static void validarCapacidade(int capacidadeMaxima) {
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("Capacidade máxima deve ser maior que zero");
        }
    }

    private static String normalizarTextoObrigatorio(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " é obrigatório");
        String normalizado = valor.trim();
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
        return normalizado;
    }

    public TurmaId getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public DisciplinaId getDisciplinaId() {
        return disciplinaId;
    }

    public PeriodoLetivoId getPeriodoLetivoId() {
        return periodoLetivoId;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public StatusTurma getStatus() {
        return status;
    }
}
