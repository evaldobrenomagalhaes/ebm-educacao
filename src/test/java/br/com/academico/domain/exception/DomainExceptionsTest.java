package br.com.academico.domain.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionsTest {

    @Test
    void entityNotFoundExceptionOfDeveFormatarMensagem() {
        UUID id = UUID.randomUUID();

        EntityNotFoundException ex = EntityNotFoundException.of("Aluno", id);

        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(ex.getMessage()).isEqualTo("Aluno não encontrado(a): " + id);
    }

    @Test
    void entityNotFoundExceptionConstrutorDeveAceitarMensagem() {
        EntityNotFoundException ex = new EntityNotFoundException("custom");

        assertThat(ex.getMessage()).isEqualTo("custom");
    }

    @Test
    void periodoLetivoEncerradoExceptionDoPeriodo() {
        PeriodoLetivoEncerradoException ex = PeriodoLetivoEncerradoException.doPeriodo();

        assertThat(ex).isInstanceOf(BusinessRuleViolationException.class);
        assertThat(ex.getMessage()).isEqualTo("Período letivo encerrado");
    }

    @Test
    void periodoLetivoEncerradoExceptionConstrutor() {
        PeriodoLetivoEncerradoException ex = new PeriodoLetivoEncerradoException("encerrado");

        assertThat(ex.getMessage()).isEqualTo("encerrado");
    }
}
