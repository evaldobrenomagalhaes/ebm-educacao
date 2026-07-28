package br.com.academico.infrastructure.web.response;

import br.com.academico.application.dto.CursoDto;
import br.com.academico.domain.valueobject.SituacaoCurso;

import java.util.UUID;

public record CursoResponse(
        UUID id,
        String nome,
        SituacaoCurso situacao
) {

    public static CursoResponse from(CursoDto dto) {
        return new CursoResponse(dto.id(), dto.nome(), dto.situacao());
    }
}
