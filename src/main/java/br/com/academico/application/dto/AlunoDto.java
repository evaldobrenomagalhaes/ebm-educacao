package br.com.academico.application.dto;

import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.valueobject.SituacaoAcademica;

import java.util.UUID;

public record AlunoDto(
        UUID id,
        String nome,
        String email,
        SituacaoAcademica situacaoAcademica
) {

    public static AlunoDto from(Aluno aluno) {
        return new AlunoDto(
                aluno.getId().valor(),
                aluno.getNome(),
                aluno.getEmail().endereco(),
                aluno.getSituacaoAcademica()
        );
    }
}
