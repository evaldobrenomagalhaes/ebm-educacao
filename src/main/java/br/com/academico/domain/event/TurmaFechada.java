package br.com.academico.domain.event;

import br.com.academico.domain.valueobject.TurmaId;

/**
 * Turma deixou de aceitar novas matrículas.
 */
public record TurmaFechada(TurmaId turmaId) implements DomainEvent {
}
