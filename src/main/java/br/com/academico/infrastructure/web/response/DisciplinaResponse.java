package br.com.academico.infrastructure.web.response;

import br.com.academico.application.dto.DisciplinaDto;

import java.util.UUID;

public record DisciplinaResponse(
        UUID id,
        String nome,
        String codigo,
        UUID cursoId
) {

    public static DisciplinaResponse from(DisciplinaDto dto) {
        return new DisciplinaResponse(dto.id(), dto.nome(), dto.codigo(), dto.cursoId());
    }
}
