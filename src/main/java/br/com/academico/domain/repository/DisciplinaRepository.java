package br.com.academico.domain.repository;

import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.valueobject.DisciplinaId;

import java.util.List;
import java.util.Optional;

/**
 * Port de saída para persistência do Aggregate {@link Disciplina} (doc 11).
 */
public interface DisciplinaRepository {

    Optional<Disciplina> buscarPorId(DisciplinaId id);

    void salvar(Disciplina disciplina);

    void excluir(DisciplinaId id);

    List<Disciplina> listar();
}
