package br.com.academico.application.usecase;

import br.com.academico.application.dto.CursoDto;
import br.com.academico.application.query.ListarCursosQuery;
import br.com.academico.domain.model.Curso;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.valueobject.SituacaoCurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarCursosUseCaseTest {

    @Mock
    private CursoRepository cursoRepository;

    private ListarCursosUseCase useCase;
    private Curso ads;
    private Curso direito;

    @BeforeEach
    void setUp() {
        useCase = new ListarCursosUseCase(cursoRepository);
        ads = Curso.cadastrar("Análise de Sistemas", SituacaoCurso.ATIVO);
        direito = Curso.cadastrar("Direito", SituacaoCurso.INATIVO);
        when(cursoRepository.listar()).thenReturn(List.of(ads, direito));
    }

    @Test
    void deveListarTodos() {
        assertThat(useCase.executar(ListarCursosQuery.todos())).hasSize(2);
    }

    @Test
    void deveFiltrarPorNomeMatch() {
        List<CursoDto> resultado = useCase.executar(new ListarCursosQuery("análise", null));

        assertThat(resultado).extracting(CursoDto::nome).containsExactly("Análise de Sistemas");
    }

    @Test
    void deveRetornarVazioQuandoNomeNaoBate() {
        assertThat(useCase.executar(new ListarCursosQuery("medicina", null))).isEmpty();
    }

    @Test
    void deveFiltrarPorSituacao() {
        List<CursoDto> resultado = useCase.executar(new ListarCursosQuery(null, SituacaoCurso.INATIVO));

        assertThat(resultado).extracting(CursoDto::nome).containsExactly("Direito");
    }
}
