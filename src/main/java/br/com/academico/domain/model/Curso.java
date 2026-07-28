package br.com.academico.domain.model;

import br.com.academico.domain.valueobject.CursoId;
import br.com.academico.domain.valueobject.SituacaoCurso;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "LY_CURSO")
public class Curso {

    @EmbeddedId
    @AttributeOverride(name = "valor", column = @Column(name = "id"))
    private CursoId id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 30)
    private SituacaoCurso situacao;

    protected Curso() {
    }

    public static Curso cadastrar(String nome, SituacaoCurso situacao) {
        Curso curso = new Curso();
        curso.id = CursoId.novo();
        curso.aplicarDados(nome, situacao);
        return curso;
    }

    public void atualizar(String nome, SituacaoCurso situacao) {
        aplicarDados(nome, situacao);
    }

    private void aplicarDados(String nome, SituacaoCurso situacao) {
        Objects.requireNonNull(situacao, "Situação do curso é obrigatória");
        String nomeNormalizado = normalizarTextoObrigatorio(nome, "Nome");
        this.nome = nomeNormalizado;
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

    public CursoId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public SituacaoCurso getSituacao() {
        return situacao;
    }
}
