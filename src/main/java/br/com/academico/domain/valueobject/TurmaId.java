package br.com.academico.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public record TurmaId(UUID valor) implements Serializable {

    public TurmaId {
        Objects.requireNonNull(valor, "TurmaId é obrigatório");
    }

    public static TurmaId novo() {
        return new TurmaId(UUID.randomUUID());
    }

    public static TurmaId de(UUID valor) {
        return new TurmaId(valor);
    }

    public static TurmaId de(String valor) {
        return new TurmaId(UUID.fromString(valor));
    }
}
