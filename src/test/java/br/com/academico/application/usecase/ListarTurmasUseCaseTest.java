package br.com.academico.application.usecase;

import br.com.academico.application.dto.TurmaDto;
import br.com.academico.application.query.ListarTurmasQuery;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.StatusTurma;
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
class ListarTurmasUseCaseTest {

    @Mock
    private TurmaRepository turmaRepository;

    private ListarTurmasUseCase useCase;
    private DisciplinaId disciplinaId;
    private PeriodoLetivoId periodoId;
    private Turma aberta;
    private Turma fechada;

    @BeforeEach
    void setUp() {
        useCase = new ListarTurmasUseCase(turmaRepository);
        disciplinaId = DisciplinaId.novo();
        periodoId = PeriodoLetivoId.novo();
        aberta = Turma.cadastrar("TADS-01", disciplinaId, periodoId, 30, StatusTurma.ABERTA);
        fechada = Turma.cadastrar("TADS-02", DisciplinaId.novo(), PeriodoLetivoId.novo(), 20, StatusTurma.FECHADA);
        when(turmaRepository.listar()).thenReturn(List.of(aberta, fechada));
    }

    @Test
    void deveListarTodos() {
        assertThat(useCase.executar(ListarTurmasQuery.todos())).hasSize(2);
    }

    @Test
    void deveFiltrarPorCodigoMatch() {
        List<TurmaDto> resultado = useCase.executar(new ListarTurmasQuery("tads-01", null, null, null, null));

        assertThat(resultado).extracting(TurmaDto::codigo).containsExactly("TADS-01");
    }

    @Test
    void deveRetornarVazioQuandoCodigoNaoBate() {
        assertThat(useCase.executar(new ListarTurmasQuery("XYZ", null, null, null, null))).isEmpty();
    }

    @Test
    void deveFiltrarPorStatus() {
        List<TurmaDto> resultado = useCase.executar(
                new ListarTurmasQuery(null, StatusTurma.FECHADA, null, null, null));

        assertThat(resultado).extracting(TurmaDto::codigo).containsExactly("TADS-02");
    }

    @Test
    void deveFiltrarPorDisciplinaEPeriodo() {
        List<TurmaDto> resultado = useCase.executar(
                new ListarTurmasQuery(null, null, disciplinaId.valor(), periodoId.valor(), null));

        assertThat(resultado).extracting(TurmaDto::codigo).containsExactly("TADS-01");
    }

    @Test
    void deveRetornarVazioQuandoDisciplinaNaoBate() {
        assertThat(useCase.executar(
                new ListarTurmasQuery(null, null, UUID.randomUUID(), null, null))).isEmpty();
    }

    @Test
    void deveFiltrarPorComVagas() {
        List<TurmaDto> comVagas = useCase.executar(new ListarTurmasQuery(null, null, null, null, true));
        assertThat(comVagas).extracting(TurmaDto::codigo).contains("TADS-01", "TADS-02");

        Turma lotada = Turma.cadastrar("LOT", DisciplinaId.novo(), PeriodoLetivoId.novo(), 1, StatusTurma.ABERTA);
        lotada.consumirVaga();
        when(turmaRepository.listar()).thenReturn(List.of(lotada));

        assertThat(useCase.executar(new ListarTurmasQuery(null, null, null, null, true))).isEmpty();
        assertThat(useCase.executar(new ListarTurmasQuery(null, null, null, null, false))).hasSize(1);
    }

    @Test
    void deveRetornarVazioQuandoPeriodoNaoBate() {
        assertThat(useCase.executar(
                new ListarTurmasQuery(null, null, null, UUID.randomUUID(), null))).isEmpty();
    }
}
