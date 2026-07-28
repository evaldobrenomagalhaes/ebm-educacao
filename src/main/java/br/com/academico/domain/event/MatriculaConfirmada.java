package br.com.academico.domain.event;

import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.TurmaId;

/**
 * Matrícula confirmada. Consumo de vaga ocorre no use case, não neste evento (doc 08).
 */
public record MatriculaConfirmada(
        MatriculaId matriculaId,
        AlunoId alunoId,
        TurmaId turmaId
) implements DomainEvent {
}
