package br.com.academico.domain.exception;

/**
 * Tentativa de matrícula em turma fechada (RN-04 / INV-03).
 */
public class TurmaEncerradaException extends BusinessRuleViolationException {

    public TurmaEncerradaException(String message) {
        super(message);
    }

    public static TurmaEncerradaException daTurma() {
        return new TurmaEncerradaException("Turma fechada não aceita novas matrículas");
    }
}
