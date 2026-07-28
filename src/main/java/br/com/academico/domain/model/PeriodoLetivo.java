package br.com.academico.domain.model;

import br.com.academico.domain.exception.PeriodoLetivoEncerradoException;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "LY_PERIODO_LETIVO")
public class PeriodoLetivo {

    @EmbeddedId
    @AttributeOverride(name = "valor", column = @Column(name = "id"))
    private PeriodoLetivoId id;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_termino", nullable = false)
    private LocalDate dataTermino;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 30)
    private SituacaoPeriodoLetivo situacao;

    protected PeriodoLetivo() {
    }

    public static PeriodoLetivo cadastrar(
            String codigo,
            LocalDate dataInicio,
            LocalDate dataTermino,
            SituacaoPeriodoLetivo situacao
    ) {
        PeriodoLetivo periodo = new PeriodoLetivo();
        periodo.id = PeriodoLetivoId.novo();
        periodo.aplicarDados(codigo, dataInicio, dataTermino, situacao);
        return periodo;
    }

    public void atualizar(
            String codigo,
            LocalDate dataInicio,
            LocalDate dataTermino,
            SituacaoPeriodoLetivo situacao
    ) {
        aplicarDados(codigo, dataInicio, dataTermino, situacao);
    }

    private void aplicarDados(
            String codigo,
            LocalDate dataInicio,
            LocalDate dataTermino,
            SituacaoPeriodoLetivo situacao
    ) {
        Objects.requireNonNull(dataInicio, "Data de início é obrigatória");
        Objects.requireNonNull(dataTermino, "Data de término é obrigatória");
        Objects.requireNonNull(situacao, "Situação do período letivo é obrigatória");
        if (dataTermino.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de término não pode ser anterior à data de início");
        }
        this.codigo = normalizarTextoObrigatorio(codigo, "Código");
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.situacao = situacao;
    }

    private static String normalizarTextoObrigatorio(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " é obrigatório");
        String normalizado = valor.trim();
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
        return normalizado;
    }

    public boolean estaEncerrado() {
        return situacao == SituacaoPeriodoLetivo.ENCERRADO;
    }

    /**
     * Período encerrado não aceita oferta de novas turmas.
     */
    public void garantirAbertoParaOferta() {
        if (estaEncerrado()) {
            throw PeriodoLetivoEncerradoException.doPeriodo();
        }
    }

    public PeriodoLetivoId getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public SituacaoPeriodoLetivo getSituacao() {
        return situacao;
    }
}
