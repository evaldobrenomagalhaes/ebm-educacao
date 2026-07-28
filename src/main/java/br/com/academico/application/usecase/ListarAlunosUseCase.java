package br.com.academico.application.usecase;

import br.com.academico.application.dto.AlunoDto;
import br.com.academico.application.query.ListarAlunosQuery;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.repository.AlunoRepository;

import java.util.List;
import java.util.Objects;

public final class ListarAlunosUseCase {

    private final AlunoRepository alunoRepository;

    public ListarAlunosUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = Objects.requireNonNull(alunoRepository);
    }

    public List<AlunoDto> executar(ListarAlunosQuery query) {
        Objects.requireNonNull(query, "Query é obrigatória");
        return alunoRepository.listar().stream()
                .filter(aluno -> corresponde(aluno, query))
                .map(AlunoDto::from)
                .toList();
    }

    private static boolean corresponde(Aluno aluno, ListarAlunosQuery query) {
        if (!TextoFiltro.contem(aluno.getNome(), query.nome())) {
            return false;
        }
        if (!TextoFiltro.contem(aluno.getEmail().endereco(), query.email())) {
            return false;
        }
        return query.situacaoAcademica() == null
                || query.situacaoAcademica() == aluno.getSituacaoAcademica();
    }
}
