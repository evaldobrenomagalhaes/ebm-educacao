package br.com.academico.domain.repository;

import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.TurmaId;

import java.util.List;
import java.util.Optional;

/**
 * Port de saída para persistência do Aggregate {@link Matricula} (doc 11).
 */
public interface MatriculaRepository {

    Optional<Matricula> buscarPorId(MatriculaId id);

    void salvar(Matricula matricula);

    /**
     * INV-04 / RN-08 — verifica se já existe matrícula do aluno na turma.
     */
    boolean existePorAlunoETurma(AlunoId alunoId, TurmaId turmaId);

    List<Matricula> listarPorAluno(AlunoId alunoId);

    List<Matricula> listarPorTurma(TurmaId turmaId);

    /**
     * Listagem global de matrículas ({@code ListarMatriculas}, doc 09 §4.4).
     * Filtros opcionais são aplicados na camada de aplicação.
     */
    List<Matricula> listar();
}
