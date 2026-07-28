package br.com.academico.domain.exception;

/**
 * Base das exceções do domínio. Não carrega conceitos de HTTP ou infraestrutura (DA-017).
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
