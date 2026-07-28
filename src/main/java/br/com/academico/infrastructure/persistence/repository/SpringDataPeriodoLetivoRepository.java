package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPeriodoLetivoRepository extends JpaRepository<PeriodoLetivo, PeriodoLetivoId> {
}
