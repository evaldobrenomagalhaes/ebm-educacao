package br.com.academico.application.usecase;

import br.com.academico.application.dto.AlunoDto;
import br.com.academico.application.query.ListarAlunosQuery;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAlunosUseCaseTest {

    @Mock
    private AlunoRepository alunoRepository;

    private ListarAlunosUseCase useCase;
    private Aluno ana;
    private Aluno bruno;

    @BeforeEach
    void setUp() {
        useCase = new ListarAlunosUseCase(alunoRepository);
        ana = Aluno.cadastrar("Ana Silva", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        bruno = Aluno.cadastrar("Bruno Costa", Email.de("bruno@email.com"), SituacaoAcademica.INATIVO);
        when(alunoRepository.listar()).thenReturn(List.of(ana, bruno));
    }

    @Test
    void deveListarTodosComFiltroNulo() {
        List<AlunoDto> resultado = useCase.executar(ListarAlunosQuery.todos());

        assertThat(resultado).hasSize(2);
    }

    @Test
    void deveFiltrarPorNomeComMatch() {
        List<AlunoDto> resultado = useCase.executar(new ListarAlunosQuery("ana", null, null));

        assertThat(resultado).extracting(AlunoDto::nome).containsExactly("Ana Silva");
    }

    @Test
    void deveRetornarVazioQuandoNomeNaoBate() {
        List<AlunoDto> resultado = useCase.executar(new ListarAlunosQuery("xyz", null, null));

        assertThat(resultado).isEmpty();
    }

    @Test
    void filtroEmBrancoNaoRestringe() {
        List<AlunoDto> resultado = useCase.executar(new ListarAlunosQuery("  ", " ", null));

        assertThat(resultado).hasSize(2);
    }

    @Test
    void deveFiltrarPorEmailESituacao() {
        List<AlunoDto> resultado = useCase.executar(
                new ListarAlunosQuery(null, "bruno@", SituacaoAcademica.INATIVO));

        assertThat(resultado).extracting(AlunoDto::email).containsExactly("bruno@email.com");
    }

    @Test
    void deveExcluirQuandoSituacaoNaoBate() {
        List<AlunoDto> resultado = useCase.executar(
                new ListarAlunosQuery(null, null, SituacaoAcademica.ATIVO));

        assertThat(resultado).extracting(AlunoDto::nome).containsExactly("Ana Silva");
    }
}
