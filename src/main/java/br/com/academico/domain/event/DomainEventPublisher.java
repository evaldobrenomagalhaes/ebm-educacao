package br.com.academico.domain.event;

/**
 * Porta de publicação de eventos de domínio.
 * A entrega aos consumidores ocorre após o commit da transação (MD-011).
 */
public interface DomainEventPublisher {

    void publish(DomainEvent event);

    default void publishAll(Iterable<? extends DomainEvent> events) {
        events.forEach(this::publish);
    }
}
