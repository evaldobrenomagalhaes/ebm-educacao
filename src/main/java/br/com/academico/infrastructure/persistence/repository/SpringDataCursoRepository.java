package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Curso;
import br.com.academico.domain.valueobject.CursoId;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCursoRepository extends JpaRepository<Curso, CursoId> {
}
