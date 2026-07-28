package br.com.academico.domain.model;

import br.com.academico.domain.valueobject.CursoId;
import br.com.academico.domain.valueobject.DisciplinaId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "LY_DISCIPLINA")
public class Disciplina {

    @EmbeddedId
    @AttributeOverride(name = "valor", column = @Column(name = "id"))
    private DisciplinaId id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "curso_id", nullable = false))
    private CursoId cursoId;

    protected Disciplina() {
    }

    public static Disciplina cadastrar(String nome, String codigo, CursoId cursoId) {
        Disciplina disciplina = new Disciplina();
        disciplina.id = DisciplinaId.novo();
        disciplina.aplicarDados(nome, codigo, cursoId);
        return disciplina;
    }

    public void atualizar(String nome, String codigo, CursoId cursoId) {
        aplicarDados(nome, codigo, cursoId);
    }

    private void aplicarDados(String nome, String codigo, CursoId cursoId) {
        Objects.requireNonNull(cursoId, "Curso é obrigatório");
        this.nome = normalizarTextoObrigatorio(nome, "Nome");
        this.codigo = normalizarTextoObrigatorio(codigo, "Código");
        this.cursoId = cursoId;
    }

    private static String normalizarTextoObrigatorio(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " é obrigatório");
        String normalizado = valor.trim();
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
        return normalizado;
    }

    public DisciplinaId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public CursoId getCursoId() {
        return cursoId;
    }
}
