package br.com.academico.integration;

import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.SituacaoCurso;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import br.com.academico.domain.valueobject.StatusTurma;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * IT do adapter JPA de matrícula — unicidade aluno+turma e {@code existePorAlunoETurma} (doc 15 §8).
 */
class JpaMatriculaRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private PeriodoLetivoRepository periodoLetivoRepository;

    @Test
    void existePorAlunoETurmaDeveRefletirPersistencia() {
        Fixture fixture = cadastrarFixture();

        assertThat(matriculaRepository.existePorAlunoETurma(fixture.aluno().getId(), fixture.turma().getId()))
                .isFalse();

        matriculaRepository.salvar(Matricula.realizar(fixture.aluno().getId(), fixture.turma().getId()));

        assertThat(matriculaRepository.existePorAlunoETurma(fixture.aluno().getId(), fixture.turma().getId()))
                .isTrue();
    }

    @Test
    void deveRejeitarMatriculaDuplicadaPelaConstraintUnica() {
        Fixture fixture = cadastrarFixture();
        matriculaRepository.salvar(Matricula.realizar(fixture.aluno().getId(), fixture.turma().getId()));

        assertThatThrownBy(() ->
                matriculaRepository.salvar(Matricula.realizar(fixture.aluno().getId(), fixture.turma().getId()))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    private Fixture cadastrarFixture() {
        String sufixo = UUID.randomUUID().toString().substring(0, 8);

        Curso curso = Curso.cadastrar("Curso " + sufixo, SituacaoCurso.ATIVO);
        cursoRepository.salvar(curso);

        Disciplina disciplina = Disciplina.cadastrar(
                "Disciplina " + sufixo, "DISC-" + sufixo, curso.getId());
        disciplinaRepository.salvar(disciplina);

        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2026." + sufixo,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 6, 30),
                SituacaoPeriodoLetivo.ABERTO
        );
        periodoLetivoRepository.salvar(periodo);

        Turma turma = Turma.cadastrar(
                "TUR-" + sufixo,
                disciplina.getId(),
                periodo.getId(),
                30,
                StatusTurma.ABERTA
        );
        turmaRepository.salvar(turma);

        Aluno aluno = Aluno.cadastrar(
                "Aluno " + sufixo,
                Email.de("aluno." + sufixo + "@exemplo.com"),
                SituacaoAcademica.ATIVO
        );
        alunoRepository.salvar(aluno);

        return new Fixture(aluno, turma);
    }

    private record Fixture(Aluno aluno, Turma turma) {
    }
}
