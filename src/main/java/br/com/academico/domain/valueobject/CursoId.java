package br.com.academico.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public record CursoId(UUID valor) implements Serializable {

    public CursoId {
        Objects.requireNonNull(valor, "CursoId é obrigatório");
    }

    public static CursoId novo() {
        return new CursoId(UUID.randomUUID());
    }

    public static CursoId de(UUID valor) {
        return new CursoId(valor);
    }

    public static CursoId de(String valor) {
        return new CursoId(UUID.fromString(valor));
    }
}
