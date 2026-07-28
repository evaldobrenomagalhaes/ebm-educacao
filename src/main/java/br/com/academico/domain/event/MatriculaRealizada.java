package br.com.academico.domain.event;

import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.TurmaId;

/**
 * Nova matrícula registrada (caso de uso {@code RealizarMatricula}).
 * Nome canônico — não usar {@code MatriculaCriada}.
 */
public record MatriculaRealizada(
        MatriculaId matriculaId,
        AlunoId alunoId,
        TurmaId turmaId
) implements DomainEvent {
}
