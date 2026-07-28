package br.com.academico.application.usecase;

import org.springframework.transaction.annotation.Transactional;

import br.com.academico.application.dto.CursoDto;
import br.com.academico.application.query.ListarCursosQuery;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.repository.CursoRepository;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
public class ListarCursosUseCase {

    private final CursoRepository cursoRepository;

    public ListarCursosUseCase(CursoRepository cursoRepository) {
        this.cursoRepository = Objects.requireNonNull(cursoRepository);
    }

    public List<CursoDto> executar(ListarCursosQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return cursoRepository.listar().stream()
                .filter(curso -> corresponde(curso, query))
                .map(CursoDto::from)
                .toList();
    }

    private static boolean corresponde(Curso curso, ListarCursosQuery query) {
        if (!TextoFiltro.contem(curso.getNome(), query.nome())) {
            return false;
        }
        return query.situacao() == null || query.situacao() == curso.getSituacao();
    }
}
