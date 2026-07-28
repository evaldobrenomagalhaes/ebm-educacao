package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.TurmaDto;
import br.com.academico.application.query.ListarTurmasQuery;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.TurmaRepository;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
public class ListarTurmasUseCase {

    private final TurmaRepository turmaRepository;

    public ListarTurmasUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = Objects.requireNonNull(turmaRepository);
    }

    public List<TurmaDto> executar(ListarTurmasQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return turmaRepository.listar().stream()
                .filter(turma -> corresponde(turma, query))
                .map(TurmaDto::from)
                .toList();
    }

    private static boolean corresponde(Turma turma, ListarTurmasQuery query) {
        if (!TextoFiltro.contem(turma.getCodigo(), query.codigo())) {
            return false;
        }
        if (query.status() != null && query.status() != turma.getStatus()) {
            return false;
        }
        if (query.disciplinaId() != null
                && !query.disciplinaId().equals(turma.getDisciplinaId().valor())) {
            return false;
        }
        if (query.periodoLetivoId() != null
                && !query.periodoLetivoId().equals(turma.getPeriodoLetivoId().valor())) {
            return false;
        }
        return query.comVagas() == null || query.comVagas() == turma.possuiVagas();
    }
}
