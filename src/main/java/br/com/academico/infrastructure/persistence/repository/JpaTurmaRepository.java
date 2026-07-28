package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.TurmaId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaTurmaRepository implements TurmaRepository {

    private final SpringDataTurmaRepository springDataRepository;

    public JpaTurmaRepository(SpringDataTurmaRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository);
    }

    @Override
    public Optional<Turma> buscarPorId(TurmaId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(Turma turma) {
        springDataRepository.save(turma);
    }

    @Override
    public void excluir(TurmaId id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<Turma> listar() {
        return springDataRepository.findAll();
    }
}
