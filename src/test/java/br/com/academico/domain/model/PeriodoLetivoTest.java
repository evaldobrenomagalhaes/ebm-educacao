package br.com.academico.domain.model;

import br.com.academico.domain.exception.PeriodoLetivoEncerradoException;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PeriodoLetivoTest {

    @Test
    void periodoAbertoAceitaOfertaDeTurma() {
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2026.1",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 6, 30),
                SituacaoPeriodoLetivo.ABERTO
        );

        assertThatCode(periodo::garantirAbertoParaOferta).doesNotThrowAnyException();
    }

    @Test
    void periodoEncerradoNaoAceitaOfertaDeTurma() {
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2025.2",
                LocalDate.of(2025, 8, 1),
                LocalDate.of(2025, 12, 15),
                SituacaoPeriodoLetivo.ENCERRADO
        );

        assertThatThrownBy(periodo::garantirAbertoParaOferta)
                .isInstanceOf(PeriodoLetivoEncerradoException.class)
                .hasMessageContaining("Período letivo encerrado");
    }
}
