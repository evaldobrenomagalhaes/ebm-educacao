package br.com.academico.domain.model;

import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlunoTest {

    @Test
    void alunoAtivoPodeRealizarMatricula() {
        Aluno aluno = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);

        assertThatCode(aluno::garantirAptaParaMatricula).doesNotThrowAnyException();
    }

    @Test
    void alunoInativoNaoPodeRealizarMatricula() {
        Aluno aluno = Aluno.cadastrar("Bruno", Email.de("bruno@email.com"), SituacaoAcademica.INATIVO);

        assertThatThrownBy(aluno::garantirAptaParaMatricula)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Aluno inativo não pode realizar matrícula");
    }
}
