package br.com.academico.application.usecase;

import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.application.query.ListarPeriodosLetivosQuery;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPeriodosLetivosUseCaseTest {

    @Mock
    private PeriodoLetivoRepository periodoLetivoRepository;

    private ListarPeriodosLetivosUseCase useCase;
    private PeriodoLetivo p1;
    private PeriodoLetivo p2;

    @BeforeEach
    void setUp() {
        useCase = new ListarPeriodosLetivosUseCase(periodoLetivoRepository);
        p1 = PeriodoLetivo.cadastrar(
                "2026.1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), SituacaoPeriodoLetivo.ABERTO);
        p2 = PeriodoLetivo.cadastrar(
                "2025.2", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 12, 20), SituacaoPeriodoLetivo.ENCERRADO);
        when(periodoLetivoRepository.listar()).thenReturn(List.of(p1, p2));
    }

    @Test
    void deveListarTodos() {
        assertThat(useCase.executar(ListarPeriodosLetivosQuery.todos())).hasSize(2);
    }

    @Test
    void deveFiltrarPorCodigoMatch() {
        List<PeriodoLetivoDto> resultado = useCase.executar(
                new ListarPeriodosLetivosQuery("2026", null, null, null, null, null, null));

        assertThat(resultado).extracting(PeriodoLetivoDto::codigo).containsExactly("2026.1");
    }

    @Test
    void deveRetornarVazioQuandoCodigoNaoBate() {
        assertThat(useCase.executar(
                new ListarPeriodosLetivosQuery("2099", null, null, null, null, null, null))).isEmpty();
    }

    @Test
    void deveFiltrarPorSituacao() {
        List<PeriodoLetivoDto> resultado = useCase.executar(
                new ListarPeriodosLetivosQuery(null, SituacaoPeriodoLetivo.ENCERRADO, null, null, null, null, null));

        assertThat(resultado).extracting(PeriodoLetivoDto::codigo).containsExactly("2025.2");
    }

    @Test
    void deveFiltrarPorIntervaloDeDatas() {
        List<PeriodoLetivoDto> resultado = useCase.executar(new ListarPeriodosLetivosQuery(
                null,
                null,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30),
                null));

        assertThat(resultado).extracting(PeriodoLetivoDto::codigo).containsExactly("2026.1");
    }

    @Test
    void deveExcluirQuandoForaDoIntervalo() {
        assertThat(useCase.executar(new ListarPeriodosLetivosQuery(
                null, null, LocalDate.of(2027, 1, 1), null, null, null, null))).isEmpty();
    }

    @Test
    void deveExcluirQuandoDataTerminoForaDoIntervalo() {
        assertThat(useCase.executar(new ListarPeriodosLetivosQuery(
                null, null, null, null, LocalDate.of(2027, 1, 1), null, null))).isEmpty();
    }

    @Test
    void deveFiltrarPorVigenteEm() {
        List<PeriodoLetivoDto> resultado = useCase.executar(
                new ListarPeriodosLetivosQuery(null, null, null, null, null, null, LocalDate.of(2026, 3, 15)));

        assertThat(resultado).extracting(PeriodoLetivoDto::codigo).containsExactly("2026.1");
    }

    @Test
    void vigenteEmForaDoPeriodoRetornaVazio() {
        assertThat(useCase.executar(
                new ListarPeriodosLetivosQuery(null, null, null, null, null, null, LocalDate.of(2024, 1, 1))))
                .isEmpty();
    }
}
