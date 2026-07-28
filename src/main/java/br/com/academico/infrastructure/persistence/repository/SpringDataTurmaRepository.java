package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Turma;
import br.com.academico.domain.valueobject.TurmaId;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTurmaRepository extends JpaRepository<Turma, TurmaId> {
}
