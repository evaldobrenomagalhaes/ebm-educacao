package br.com.academico.application.dto;

import br.com.academico.domain.model.Curso;
import br.com.academico.domain.valueobject.SituacaoCurso;

import java.util.UUID;

public record CursoDto(
        UUID id,
        String nome,
        SituacaoCurso situacao
) {

    public static CursoDto from(Curso curso) {
        return new CursoDto(
                curso.getId().valor(),
                curso.getNome(),
                curso.getSituacao()
        );
    }
}
