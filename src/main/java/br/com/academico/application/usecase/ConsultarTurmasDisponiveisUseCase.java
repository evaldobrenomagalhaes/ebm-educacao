package br.com.academico.application.usecase;

import br.com.academico.application.dto.TurmaDto;
import br.com.academico.application.query.ConsultarTurmasDisponiveisQuery;
import br.com.academico.application.query.ListarTurmasQuery;
import br.com.academico.domain.valueobject.StatusTurma;

import java.util.List;
import java.util.Objects;

/**
 * Atalho semântico sobre {@link ListarTurmasUseCase}: status ABERTA e com vagas (doc 09 §4.3).
 */
public final class ConsultarTurmasDisponiveisUseCase {

    private final ListarTurmasUseCase listarTurmasUseCase;

    public ConsultarTurmasDisponiveisUseCase(ListarTurmasUseCase listarTurmasUseCase) {
        this.listarTurmasUseCase = Objects.requireNonNull(listarTurmasUseCase);
    }

    public List<TurmaDto> executar(ConsultarTurmasDisponiveisQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return listarTurmasUseCase.executar(new ListarTurmasQuery(
                null,
                StatusTurma.ABERTA,
                query.disciplinaId(),
                query.periodoLetivoId(),
                true
        ));
    }
}
