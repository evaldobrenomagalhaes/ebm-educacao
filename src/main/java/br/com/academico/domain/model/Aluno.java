package br.com.academico.domain.model;

import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "LY_ALUNO")
public class Aluno {

    @EmbeddedId
    @AttributeOverride(name = "valor", column = @Column(name = "id"))
    private AlunoId id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Embedded
    @AttributeOverride(name = "endereco", column = @Column(name = "email", nullable = false))
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_academica", nullable = false, length = 30)
    private SituacaoAcademica situacaoAcademica;

    protected Aluno() {
    }

    public static Aluno cadastrar(String nome, Email email, SituacaoAcademica situacaoAcademica) {
        Aluno aluno = new Aluno();
        aluno.id = AlunoId.novo();
        aluno.aplicarDados(nome, email, situacaoAcademica);
        return aluno;
    }

    public void atualizar(String nome, Email email, SituacaoAcademica situacaoAcademica) {
        aplicarDados(nome, email, situacaoAcademica);
    }

    private void aplicarDados(String nome, Email email, SituacaoAcademica situacaoAcademica) {
        Objects.requireNonNull(email, "E-mail é obrigatório");
        Objects.requireNonNull(situacaoAcademica, "Situação acadêmica é obrigatória");
        String nomeNormalizado = normalizarTextoObrigatorio(nome, "Nome");
        this.nome = nomeNormalizado;
        this.email = email;
        this.situacaoAcademica = situacaoAcademica;
    }

    private static String normalizarTextoObrigatorio(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " é obrigatório");
        String normalizado = valor.trim();
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
        return normalizado;
    }

    /**
     * Doc 04 — situação acadêmica influencia operações; aluno inativo não pode se matricular.
     */
    public void garantirAptaParaMatricula() {
        if (situacaoAcademica == SituacaoAcademica.INATIVO) {
            throw new BusinessRuleViolationException(
                    "Aluno inativo não pode realizar matrícula"
            );
        }
    }

    public AlunoId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Email getEmail() {
        return email;
    }

    public SituacaoAcademica getSituacaoAcademica() {
        return situacaoAcademica;
    }
}
