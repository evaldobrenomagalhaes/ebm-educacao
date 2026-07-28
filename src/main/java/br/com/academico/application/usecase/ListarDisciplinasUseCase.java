package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.application.query.ListarDisciplinasQuery;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.repository.DisciplinaRepository;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
public class ListarDisciplinasUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public ListarDisciplinasUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
    }

    public List<DisciplinaDto> executar(ListarDisciplinasQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return disciplinaRepository.listar().stream()
                .filter(disciplina -> corresponde(disciplina, query))
                .map(DisciplinaDto::from)
                .toList();
    }

    private static boolean corresponde(Disciplina disciplina, ListarDisciplinasQuery query) {
        if (!TextoFiltro.contem(disciplina.getNome(), query.nome())) {
            return false;
        }
        if (!TextoFiltro.contem(disciplina.getCodigo(), query.codigo())) {
            return false;
        }
        return query.cursoId() == null
                || query.cursoId().equals(disciplina.getCursoId().valor());
    }
}
