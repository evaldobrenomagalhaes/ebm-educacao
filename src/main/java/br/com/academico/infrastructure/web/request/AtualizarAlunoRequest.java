package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.AtualizarAlunoCommand;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AtualizarAlunoRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotBlank @Email @Size(max = 255) String email,
        @NotNull SituacaoAcademica situacaoAcademica
) {

    public AtualizarAlunoCommand toCommand(UUID id) {
        return new AtualizarAlunoCommand(id, nome, email, situacaoAcademica);
    }
}
