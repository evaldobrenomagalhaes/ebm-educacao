package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.CadastrarCursoCommand;
import br.com.academico.domain.valueobject.SituacaoCurso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastrarCursoRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotNull SituacaoCurso situacao
) {

    public CadastrarCursoCommand toCommand() {
        return new CadastrarCursoCommand(nome, situacao);
    }
}
