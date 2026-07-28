package br.com.academico.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record Email(String endereco) {

    private static final Pattern PADRAO = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email {
        Objects.requireNonNull(endereco, "Endereço de e-mail é obrigatório");
        String normalizado = endereco.trim();
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException("Endereço de e-mail não pode ser vazio");
        }
        if (!PADRAO.matcher(normalizado).matches()) {
            throw new IllegalArgumentException("Endereço de e-mail inválido");
        }
        endereco = normalizado;
    }

    public static Email de(String endereco) {
        return new Email(endereco);
    }
}
