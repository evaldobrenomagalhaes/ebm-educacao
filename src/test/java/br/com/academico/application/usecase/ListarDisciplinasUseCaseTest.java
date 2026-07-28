package br.com.academico.application.usecase;

import br.com.academico.application.dto.DisciplinaDto;
import br.com.academico.application.query.ListarDisciplinasQuery;
import br.com.academico.domain.model.Disciplina;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.valueobject.CursoId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarDisciplinasUseCaseTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    private ListarDisciplinasUseCase useCase;
    private CursoId cursoId;
    private Disciplina poo;
    private Disciplina bd;

    @BeforeEach
    void setUp() {
        useCase = new ListarDisciplinasUseCase(disciplinaRepository);
        cursoId = CursoId.novo();
        poo = Disciplina.cadastrar("Programação OO", "POO1", cursoId);
        bd = Disciplina.cadastrar("Banco de Dados", "BD1", CursoId.novo());
        when(disciplinaRepository.listar()).thenReturn(List.of(poo, bd));
    }

    @Test
    void deveListarTodos() {
        assertThat(useCase.executar(ListarDisciplinasQuery.todos())).hasSize(2);
    }

    @Test
    void deveFiltrarPorNomeMatch() {
        List<DisciplinaDto> resultado = useCase.executar(new ListarDisciplinasQuery("programação", null, null));

        assertThat(resultado).extracting(DisciplinaDto::codigo).containsExactly("POO1");
    }

    @Test
    void deveRetornarVazioQuandoCodigoNaoBate() {
        assertThat(useCase.executar(new ListarDisciplinasQuery(null, "XYZ", null))).isEmpty();
    }

    @Test
    void deveFiltrarPorCursoId() {
        List<DisciplinaDto> resultado = useCase.executar(
                new ListarDisciplinasQuery(null, null, cursoId.valor()));

        assertThat(resultado).extracting(DisciplinaDto::codigo).containsExactly("POO1");
    }

    @Test
    void deveRetornarVazioQuandoCursoIdNaoBate() {
        assertThat(useCase.executar(new ListarDisciplinasQuery(null, null, UUID.randomUUID()))).isEmpty();
    }
}
