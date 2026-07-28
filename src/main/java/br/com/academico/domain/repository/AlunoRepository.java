package br.com.academico.domain.repository;

import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.valueobject.AlunoId;

import java.util.List;
import java.util.Optional;

/**
 * Port de saída para persistência do Aggregate {@link Aluno} (doc 11).
 */
public interface AlunoRepository {

    Optional<Aluno> buscarPorId(AlunoId id);

    void salvar(Aluno aluno);

    void excluir(AlunoId id);

    List<Aluno> listar();
}
