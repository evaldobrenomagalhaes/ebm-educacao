package br.com.academico.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void deveCriarEmailValido() {
        Email email = Email.de("aluno@escola.edu.br");

        assertThat(email.endereco()).isEqualTo("aluno@escola.edu.br");
    }

    @Test
    void deveNormalizarEspacosNasExtremidades() {
        Email email = Email.de("  aluno@escola.edu.br  ");

        assertThat(email.endereco()).isEqualTo("aluno@escola.edu.br");
    }

    @Test
    void deveRejeitarEmailNulo() {
        assertThatThrownBy(() -> Email.de(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    void deveRejeitarEmailVazioOuEmBranco(String endereco) {
        assertThatThrownBy(() -> Email.de(endereco))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser vazio");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "sem-arroba",
            "@semlocal.com",
            "semdominio@",
            "espaco @dominio.com",
            "aluno@",
            "aluno@dominio",
            "aluno@.com"
    })
    void deveRejeitarFormatoInvalido(String endereco) {
        assertThatThrownBy(() -> Email.de(endereco))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inválido");
    }
}
