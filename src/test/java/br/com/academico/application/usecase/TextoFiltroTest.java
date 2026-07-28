package br.com.academico.application.usecase;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextoFiltroTest {

    @Test
    void filtroNuloOuEmBrancoAceitaQualquerValor() {
        assertThat(TextoFiltro.contem(null, null)).isTrue();
        assertThat(TextoFiltro.contem("abc", null)).isTrue();
        assertThat(TextoFiltro.contem("abc", "  ")).isTrue();
    }

    @Test
    void valorNuloComFiltroNaoBate() {
        assertThat(TextoFiltro.contem(null, "x")).isFalse();
    }

    @Test
    void contemIgnoraCaseEEspacosDoFiltro() {
        assertThat(TextoFiltro.contem("Ana Silva", " ana ")).isTrue();
        assertThat(TextoFiltro.contem("Ana Silva", "xyz")).isFalse();
    }
}
