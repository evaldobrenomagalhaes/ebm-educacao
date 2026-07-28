package br.com.academico.application.query;

import java.util.UUID;

public record ListarDisciplinasQuery(
        String nome,
        String codigo,
        UUID cursoId
) {

    public static ListarDisciplinasQuery todos() {
        return new ListarDisciplinasQuery(null, null, null);
    }
}
