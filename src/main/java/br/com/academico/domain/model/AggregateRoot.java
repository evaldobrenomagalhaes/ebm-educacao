package br.com.academico.domain.model;

import br.com.academico.domain.event.DomainEvent;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base para Aggregate Roots que coletam eventos de domínio pendentes.
 * A publicação efetiva ocorre após commit (infra / use case — MD-011).
 */
@MappedSuperclass
public abstract class AggregateRoot {

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
    }

    protected void registrarEvento(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event, "Evento é obrigatório"));
    }

    /**
     * Devolve e limpa os eventos pendentes para publicação externa.
     */
    public List<DomainEvent> pullDomainEvents() {
        if (domainEvents.isEmpty()) {
            return List.of();
        }
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    public List<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
