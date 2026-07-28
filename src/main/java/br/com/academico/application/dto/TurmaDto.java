package br.com.academico.application.dto;

import br.com.academico.domain.model.Turma;
import br.com.academico.domain.valueobject.StatusTurma;

import java.util.UUID;

public record TurmaDto(
        UUID id,
        String codigo,
        UUID disciplinaId,
        UUID periodoLetivoId,
        int capacidadeMaxima,
        int vagasDisponiveis,
        StatusTurma status
) {

    public static TurmaDto from(Turma turma) {
        return new TurmaDto(
                turma.getId().valor(),
                turma.getCodigo(),
                turma.getDisciplinaId().valor(),
                turma.getPeriodoLetivoId().valor(),
                turma.getCapacidadeMaxima(),
                turma.getVagasDisponiveis(),
                turma.getStatus()
        );
    }
}
