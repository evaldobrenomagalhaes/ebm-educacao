package br.com.academico.application.query;

import java.util.UUID;

public record ConsultarTurmasDisponiveisQuery(
        UUID disciplinaId,
        UUID periodoLetivoId
) {

    public static ConsultarTurmasDisponiveisQuery todas() {
        return new ConsultarTurmasDisponiveisQuery(null, null);
    }
}
