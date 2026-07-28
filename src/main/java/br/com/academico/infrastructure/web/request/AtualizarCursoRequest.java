package br.com.academico.infrastructure.web.request;

import br.com.academico.application.command.AtualizarCursoCommand;
import br.com.academico.domain.valueobject.SituacaoCurso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AtualizarCursoRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotNull SituacaoCurso situacao
) {

    public AtualizarCursoCommand toCommand(UUID id) {
        return new AtualizarCursoCommand(id, nome, situacao);
    }
}
