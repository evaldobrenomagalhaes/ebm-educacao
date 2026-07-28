package br.com.academico.infrastructure.persistence.repository;

import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.valueobject.AlunoId;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataAlunoRepository extends JpaRepository<Aluno, AlunoId> {
}
