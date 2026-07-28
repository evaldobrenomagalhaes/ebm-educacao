package br.com.academico.infrastructure.web.response;

import br.com.academico.application.dto.AlunoDto;
import br.com.academico.domain.valueobject.SituacaoAcademica;

import java.util.UUID;

public record AlunoResponse(
        UUID id,
        String nome,
        String email,
        SituacaoAcademica situacaoAcademica
) {

    public static AlunoResponse from(AlunoDto dto) {
        return new AlunoResponse(dto.id(), dto.nome(), dto.email(), dto.situacaoAcademica());
    }
}
