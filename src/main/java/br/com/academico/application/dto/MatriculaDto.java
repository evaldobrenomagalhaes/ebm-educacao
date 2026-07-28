package br.com.academico.application.dto;

import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.valueobject.StatusMatricula;

import java.util.UUID;

public record MatriculaDto(
        UUID id,
        UUID alunoId,
        UUID turmaId,
        StatusMatricula status
) {

    public static MatriculaDto from(Matricula matricula) {
        return new MatriculaDto(
                matricula.getId().valor(),
                matricula.getAlunoId().valor(),
                matricula.getTurmaId().valor(),
                matricula.getStatus()
        );
    }
}
