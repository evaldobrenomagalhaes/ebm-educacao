package br.com.academico.domain.event;

import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.TurmaId;

/**
 * Matrícula cancelada. Liberação de vaga ocorre no use case, não neste evento (doc 08).
 */
public record MatriculaCancelada(
        MatriculaId matriculaId,
        AlunoId alunoId,
        TurmaId turmaId
) implements DomainEvent {
}
