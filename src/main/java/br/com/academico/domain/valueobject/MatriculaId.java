package br.com.academico.domain.valueobject;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record MatriculaId(UUID valor) implements Serializable {

    public MatriculaId {
        Objects.requireNonNull(valor, "MatriculaId é obrigatório");
    }

    public static MatriculaId novo() {
        return new MatriculaId(UUID.randomUUID());
    }

    public static MatriculaId de(UUID valor) {
        return new MatriculaId(valor);
    }

    public static MatriculaId de(String valor) {
        return new MatriculaId(UUID.fromString(valor));
    }
}
