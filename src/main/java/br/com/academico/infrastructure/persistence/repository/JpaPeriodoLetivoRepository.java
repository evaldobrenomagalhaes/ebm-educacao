package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaPeriodoLetivoRepository implements PeriodoLetivoRepository {

    private final SpringDataPeriodoLetivoRepository springDataRepository;

    public JpaPeriodoLetivoRepository(SpringDataPeriodoLetivoRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository);
    }

    @Override
    public Optional<PeriodoLetivo> buscarPorId(PeriodoLetivoId id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void salvar(PeriodoLetivo periodoLetivo) {
        springDataRepository.save(periodoLetivo);
    }

    @Override
    public void excluir(PeriodoLetivoId id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<PeriodoLetivo> listar() {
        return springDataRepository.findAll();
    }
}
