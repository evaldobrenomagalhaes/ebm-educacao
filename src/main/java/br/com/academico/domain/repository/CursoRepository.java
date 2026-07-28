package br.com.academico.domain.repository;

import br.com.academico.domain.model.Curso;
import br.com.academico.domain.valueobject.CursoId;

import java.util.List;
import java.util.Optional;

/**
 * Port de saída para persistência do Aggregate {@link Curso} (doc 11).
 */
public interface CursoRepository {

    Optional<Curso> buscarPorId(CursoId id);

    void salvar(Curso curso);

    void excluir(CursoId id);

    List<Curso> listar();
}
