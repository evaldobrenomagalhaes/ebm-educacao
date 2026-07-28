package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.valueobject.AlunoId;
import br.com.academico.domain.valueobject.MatriculaId;
import br.com.academico.domain.valueobject.TurmaId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaMatriculaRepository implements MatriculaRepository {

    private final SpringDataMatriculaRepository springDataRepository;

    public JpaMatriculaRepository(SpringDataMatriculaRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository);
    }

    @Override
    public Optional<Matricula> buscarPorId(MatriculaId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Matricula matricula) {
        springDataRepository.save(matricula);
    }

    @Override
    public boolean existePorAlunoETurma(AlunoId alunoId, TurmaId turmaId) {
        return springDataRepository.existsByAlunoIdAndTurmaId(alunoId, turmaId);
    }

    @Override
    public List<Matricula> listarPorAluno(AlunoId alunoId) {
        return springDataRepository.findByAlunoId(alunoId);
    }

    @Override
    public List<Matricula> listarPorTurma(TurmaId turmaId) {
        return springDataRepository.findByTurmaId(turmaId);
    }
}
