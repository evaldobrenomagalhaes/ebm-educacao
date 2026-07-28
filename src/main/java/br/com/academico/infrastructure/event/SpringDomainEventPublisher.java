package br.com.academico.infrastructure.event;

import br.com.academico.domain.event.DomainEvent;
import br.com.academico.domain.event.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Adapta a porta de domínio para {@link ApplicationEventPublisher} (ADR-002 / MD-011).
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher);
    }

    @Override
    public void publish(DomainEvent event) {
        Objects.requireNonNull(event, "Evento é obrigatório");
        applicationEventPublisher.publishEvent(event);
    }
}
