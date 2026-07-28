package br.com.academico.infrastructure.web.response;

import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.valueobject.StatusTurma;

import java.util.UUID;

public record TurmaResponse(
        UUID id,
        String codigo,
        UUID disciplinaId,
        UUID periodoLetivoId,
        int capacidadeMaxima,
        int vagasDisponiveis,
        StatusTurma status
) {

    public static TurmaResponse from(TurmaDto dto) {
        return new TurmaResponse(
                dto.id(),
                dto.codigo(),
                dto.disciplinaId(),
                dto.periodoLetivoId(),
                dto.capacidadeMaxima(),
                dto.vagasDisponiveis(),
                dto.status()
        );
    }
}
