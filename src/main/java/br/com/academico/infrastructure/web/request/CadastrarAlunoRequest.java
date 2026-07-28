package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.CadastrarAlunoCommand;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastrarAlunoRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotBlank @Email @Size(max = 255) String email,
        @NotNull SituacaoAcademica situacaoAcademica
) {

    public CadastrarAlunoCommand toCommand() {
        return new CadastrarAlunoCommand(nome, email, situacaoAcademica);
    }
}
