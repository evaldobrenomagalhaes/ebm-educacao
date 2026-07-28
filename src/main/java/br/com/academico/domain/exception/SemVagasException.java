package br.com.academico.domain.exception;

/**
 * Turma sem vagas disponíveis para confirmação de matrícula (RN-01 / INV-06).
 */
public class SemVagasException extends BusinessRuleViolationException {

    public SemVagasException(String message) {
        super(message);
    }

    public static SemVagasException daTurma() {
        return new SemVagasException("Turma sem vagas disponíveis");
    }
}
