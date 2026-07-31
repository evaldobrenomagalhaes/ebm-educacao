package br.com.academico.application.usecase;

import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

final class MatriculaFiltro {

    private MatriculaFiltro() {
    }

    static Map<TurmaId, Turma> indiceTurmas(TurmaRepository turmaRepository) {
        return turmaRepository.listar().stream()
                .collect(Collectors.toMap(Turma::getId, Function.identity()));
    }

    static boolean correspondeStatusPeriodoDisciplina(
            Matricula matricula,
            StatusMatricula status,
            UUID periodoLetivoId,
            UUID disciplinaId,
            Map<TurmaId, Turma> turmasPorId
    ) {
        if (status != null && status != matricula.getStatus()) {
            return false;
        }
        if (periodoLetivoId == null && disciplinaId == null) {
            return true;
        }
        Turma turma = turmasPorId.get(matricula.getTurmaId());
        if (turma == null) {
            return false;
        }
        if (periodoLetivoId != null
                && !periodoLetivoId.equals(turma.getPeriodoLetivoId().valor())) {
            return false;
        }
        return disciplinaId == null
                || disciplinaId.equals(turma.getDisciplinaId().valor());
    }
}
