package br.com.academico.application.usecase;

import br.com.academico.application.command.AtualizarPeriodoLetivoCommand;
import br.com.academico.application.dto.PeriodoLetivoDto;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.PeriodoLetivo;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarPeriodoLetivoUseCaseTest {

    @Mock
    private PeriodoLetivoRepository periodoLetivoRepository;

    private AtualizarPeriodoLetivoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarPeriodoLetivoUseCase(periodoLetivoRepository);
    }

    @Test
    void deveAtualizarPeriodoLetivo() {
        PeriodoLetivo periodo = PeriodoLetivo.cadastrar(
                "2026.1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), SituacaoPeriodoLetivo.ABERTO);
        when(periodoLetivoRepository.buscarPorId(periodo.getId())).thenReturn(Optional.of(periodo));

        PeriodoLetivoDto dto = useCase.executar(new AtualizarPeriodoLetivoCommand(
                periodo.getId().valor(),
                "2026.2",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 12, 20),
                SituacaoPeriodoLetivo.ENCERRADO));

        assertThat(dto.codigo()).isEqualTo("2026.2");
        assertThat(dto.situacao()).isEqualTo(SituacaoPeriodoLetivo.ENCERRADO);
        verify(periodoLetivoRepository).salvar(periodo);
    }

    @Test
    void deveFalharQuandoPeriodoNaoExiste() {
        when(periodoLetivoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(new AtualizarPeriodoLetivoCommand(
                UUID.randomUUID(),
                "2026.1",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                SituacaoPeriodoLetivo.ABERTO)))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
