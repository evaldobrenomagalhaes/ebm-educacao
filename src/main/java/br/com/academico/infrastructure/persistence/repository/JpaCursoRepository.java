package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Curso;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.valueobject.CursoId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaCursoRepository implements CursoRepository {

    private final SpringDataCursoRepository springDataRepository;

    public JpaCursoRepository(SpringDataCursoRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository);
    }

    @Override
    public Optional<Curso> buscarPorId(CursoId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Curso curso) {
        springDataRepository.save(curso);
    }

    @Override
    public void excluir(CursoId id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<Curso> listar() {
        return springDataRepository.findAll();
    }
}
