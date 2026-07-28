package br.com.academico.domain.event;

/**
 * Marcador de fatos de domínio já ocorridos (doc 08).
 * Eventos não executam regras — apenas notificam.
 */
public sealed interface DomainEvent
        permits MatriculaRealizada,
        MatriculaConfirmada,
        MatriculaCancelada,
        TurmaAberta,
        TurmaFechada {
}
