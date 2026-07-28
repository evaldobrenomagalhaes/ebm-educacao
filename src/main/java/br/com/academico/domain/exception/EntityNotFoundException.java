package br.com.academico.domain.exception;

/**
 * Entidade ou Aggregate inexistente para o identificador informado.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException of(String entidade, Object id) {
        return new EntityNotFoundException(entidade + " não encontrado(a): " + id);
    }
}
