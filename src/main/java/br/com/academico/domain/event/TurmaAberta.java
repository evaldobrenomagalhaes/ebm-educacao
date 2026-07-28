package br.com.academico.domain.event;

import br.com.academico.domain.valueobject.TurmaId;

/**
 * Turma disponibilizada para novas matrículas.
 */
public record TurmaAberta(TurmaId turmaId) implements DomainEvent {
}
