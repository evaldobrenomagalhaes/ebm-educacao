package br.com.academico.domain.repository;

import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.valueobject.PeriodoLetivoId;

import java.util.List;
import java.util.Optional;

/**
 * Port de saída para persistência do Aggregate {@link PeriodoLetivo} (doc 11).
 */
public interface PeriodoLetivoRepository {

    Optional<PeriodoLetivo> buscarPorId(PeriodoLetivoId id);

    void salvar(PeriodoLetivo periodoLetivo);

    void excluir(PeriodoLetivoId id);

    List<PeriodoLetivo> listar();
}
