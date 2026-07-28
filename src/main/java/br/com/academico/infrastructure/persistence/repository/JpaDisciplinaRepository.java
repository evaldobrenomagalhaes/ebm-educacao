package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaDisciplinaRepository implements DisciplinaRepository {

    private final SpringDataDisciplinaRepository springDataRepository;

    public JpaDisciplinaRepository(SpringDataDisciplinaRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository);
    }

    @Override
    public Optional<Disciplina> buscarPorId(DisciplinaId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Disciplina disciplina) {
        springDataRepository.save(disciplina);
    }

    @Override
    public void excluir(DisciplinaId id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<Disciplina> listar() {
        return springDataRepository.findAll();
    }
}
