package br.com.academico.domain.valueobject;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record PeriodoLetivoId(UUID valor) implements Serializable {

    public PeriodoLetivoId {
        Objects.requireNonNull(valor, "PeriodoLetivoId é obrigatório");
    }

    public static PeriodoLetivoId novo() {
        return new PeriodoLetivoId(UUID.randomUUID());
    }

    public static PeriodoLetivoId de(UUID valor) {
        return new PeriodoLetivoId(valor);
    }

    public static PeriodoLetivoId de(String valor) {
        return new PeriodoLetivoId(UUID.fromString(valor));
    }
}
