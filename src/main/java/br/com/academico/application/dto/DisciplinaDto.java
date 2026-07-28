package br.com.academico.application.dto;

import br.com.academico.domain.model.Disciplina;

import java.util.UUID;

public record DisciplinaDto(
        UUID id,
        String nome,
        String codigo,
        UUID cursoId
) {

    public static DisciplinaDto from(Disciplina disciplina) {
        return new DisciplinaDto(
                disciplina.getId().valor(),
                disciplina.getNome(),
                disciplina.getCodigo(),
                disciplina.getCursoId().valor()
        );
    }
}
