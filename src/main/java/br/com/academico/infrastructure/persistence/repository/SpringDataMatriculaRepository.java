package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.TurmaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataMatriculaRepository extends JpaRepository<Matricula, MatriculaId> {

    boolean existsByAlunoIdAndTurmaId(AlunoId alunoId, TurmaId turmaId);

    List<Matricula> findByAlunoId(AlunoId alunoId);

    List<Matricula> findByTurmaId(TurmaId turmaId);
}
