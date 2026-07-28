package br.com.academico.application.usecase;

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
        return valor.toLowerCase().contains(filtro.trim().toLowerCase());
    }
}
