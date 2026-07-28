package br.com.academico.domain.repository;

import br.com.academico.domain.model.Turma;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.List;
import java.util.Optional;

/**
 * Port de saída para persistência do Aggregate {@link Turma} (doc 11).
 */
public interface TurmaRepository {

    Optional<Turma> buscarPorId(TurmaId id);

    void salvar(Turma turma);

    void excluir(TurmaId id);

    List<Turma> listar();
}
