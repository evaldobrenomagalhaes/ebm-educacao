package br.com.academico.application.usecase;

import java.util.Locale;

final class TextoFiltro {

    private TextoFiltro() {
    }

    static boolean contem(String valor, String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return true;
        }
        if (valor == null) {
            return false;
        }
        return valor.toLowerCase(Locale.ROOT).contains(filtro.trim().toLowerCase(Locale.ROOT));
    }
}
