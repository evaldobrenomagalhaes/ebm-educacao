package br.com.academico.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public record DisciplinaId(UUID valor) implements Serializable {

    public DisciplinaId {
        Objects.requireNonNull(valor, "DisciplinaId é obrigatório");
    }

    public static DisciplinaId novo() {
        return new DisciplinaId(UUID.randomUUID());
    }

    public static DisciplinaId de(UUID valor) {
        return new DisciplinaId(valor);
    }

    public static DisciplinaId de(String valor) {
        return new DisciplinaId(UUID.fromString(valor));
    }
}
