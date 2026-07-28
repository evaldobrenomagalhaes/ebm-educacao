package br.com.academico.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public record AlunoId(UUID valor) implements Serializable {

    public AlunoId {
        Objects.requireNonNull(valor, "AlunoId é obrigatório");
    }

    public static AlunoId novo() {
        return new AlunoId(UUID.randomUUID());
    }

    public static AlunoId de(UUID valor) {
        return new AlunoId(valor);
    }

    public static AlunoId de(String valor) {
        return new AlunoId(UUID.fromString(valor));
    }
}
