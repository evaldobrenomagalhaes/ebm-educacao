package br.com.academico.infrastructure.web.exception;

import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.exception.DuplicateMatriculaException;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.exception.SemVagasException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduz exceções de domínio e validação para RFC 7807 {@link ProblemDetail} (doc 14 / DA-018).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        log.info("Recurso não encontrado: {}", ex.getMessage());
        return problem(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(DuplicateMatriculaException.class)
    public ProblemDetail handleDuplicateMatricula(DuplicateMatriculaException ex) {
        log.info("Conflito de matrícula: {}", ex.getMessage());
        return problem(HttpStatus.CONFLICT, "Conflito", ex.getMessage());
    }

    @ExceptionHandler(SemVagasException.class)
    public ProblemDetail handleSemVagas(SemVagasException ex) {
        log.info("Turma sem vagas: {}", ex.getMessage());
        return problem(HttpStatus.CONFLICT, "Conflito", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleViolationException ex) {
        log.info("Regra de negócio violada: {}", ex.getMessage());
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de negócio violada", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail detail = problem(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                "Um ou mais campos são inválidos"
        );
        detail.setProperty("errors", errors);
        return detail;
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ProblemDetail handleBadRequest(Exception ex) {
        log.info("Requisição inválida: {}", ex.getMessage());
        String message = ex instanceof HttpMessageNotReadableException
                ? "Corpo da requisição inválido ou malformado"
                : ex.getMessage();
        return problem(HttpStatus.BAD_REQUEST, "Requisição inválida", message);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Erro interno inesperado", ex);
        return problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
