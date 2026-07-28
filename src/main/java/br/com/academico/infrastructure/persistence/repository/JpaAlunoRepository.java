package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.AlunoId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaAlunoRepository implements AlunoRepository {

    private final SpringDataAlunoRepository springDataRepository;

    public JpaAlunoRepository(SpringDataAlunoRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository);
    }

    @Override
    public Optional<Aluno> buscarPorId(AlunoId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Aluno aluno) {
        springDataRepository.save(aluno);
    }

    @Override
    public void excluir(AlunoId id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<Aluno> listar() {
        return springDataRepository.findAll();
    }
}
