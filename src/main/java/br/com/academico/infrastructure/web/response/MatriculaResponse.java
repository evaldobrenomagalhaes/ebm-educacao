package br.com.academico.infrastructure.web.response;

import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.domain.valueobject.StatusMatricula;

import java.util.UUID;

public record MatriculaResponse(
        UUID id,
        UUID alunoId,
        UUID turmaId,
        StatusMatricula status
) {

    public static MatriculaResponse from(MatriculaDto dto) {
        return new MatriculaResponse(dto.id(), dto.alunoId(), dto.turmaId(), dto.status());
    }
}
