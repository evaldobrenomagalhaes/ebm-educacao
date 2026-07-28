package br.com.academico.domain.exception;

/**
 * Aluno já possui matrícula na mesma turma (RN-08 / INV-04).
 */
public class DuplicateMatriculaException extends BusinessRuleViolationException {

    public DuplicateMatriculaException(String message) {
        super(message);
    }

    public static DuplicateMatriculaException alunoNaTurma() {
        return new DuplicateMatriculaException("Aluno já possui matrícula nesta turma");
    }
}
