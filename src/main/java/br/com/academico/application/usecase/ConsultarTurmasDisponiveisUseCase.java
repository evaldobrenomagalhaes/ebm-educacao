package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.TurmaDto;
import br.com.academico.application.query.ConsultarTurmasDisponiveisQuery;
import br.com.academico.application.query.ListarTurmasQuery;
import br.com.academico.domain.valueobject.StatusTurma;

import java.util.List;
import java.util.Objects;

/**
 * Atalho semântico sobre {@link ListarTurmasUseCase}: status ABERTA e com vagas (doc 09 §4.3).
 */
@Transactional(readOnly = true)
public class ConsultarTurmasDisponiveisUseCase {

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
