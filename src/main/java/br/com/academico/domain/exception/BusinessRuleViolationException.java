package br.com.academico.domain.exception;

/**
 * Violação de regra de negócio. Exceções específicas (sem vagas, turma encerrada, etc.) herdam desta.
 */
public class BusinessRuleViolationException extends DomainException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
