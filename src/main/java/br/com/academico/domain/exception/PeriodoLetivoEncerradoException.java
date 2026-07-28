package br.com.academico.domain.exception;

/**
 * Operação impedida porque o período letivo está encerrado.
 */
public class PeriodoLetivoEncerradoException extends BusinessRuleViolationException {

    public PeriodoLetivoEncerradoException(String message) {
        super(message);
    }

    public static PeriodoLetivoEncerradoException doPeriodo() {
        return new PeriodoLetivoEncerradoException("Período letivo encerrado");
    }
}
