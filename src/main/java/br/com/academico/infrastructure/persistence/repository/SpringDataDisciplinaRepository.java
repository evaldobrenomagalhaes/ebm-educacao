package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.valueobject.DisciplinaId;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataDisciplinaRepository extends JpaRepository<Disciplina, DisciplinaId> {
}
