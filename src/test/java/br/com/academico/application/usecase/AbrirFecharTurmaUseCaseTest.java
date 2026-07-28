package br.com.academico.application.usecase;

import br.com.academico.application.command.AbrirTurmaCommand;
import br.com.academico.application.command.FecharTurmaCommand;
import br.com.academico.application.dto.TurmaDto;
import br.com.academico.domain.event.DomainEventPublisher;
import br.com.academico.domain.exception.EntityNotFoundException;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbrirFecharTurmaUseCaseTest {

    @Mock
    private TurmaRepository turmaRepository;
    @Mock
    private DomainEventPublisher domainEventPublisher;

    private AbrirTurmaUseCase abrirTurmaUseCase;
    private FecharTurmaUseCase fecharTurmaUseCase;

    @BeforeEach
    void setUp() {
        abrirTurmaUseCase = new AbrirTurmaUseCase(turmaRepository, domainEventPublisher);
        fecharTurmaUseCase = new FecharTurmaUseCase(turmaRepository, domainEventPublisher);
    }

    @Test
    void deveAbrirTurma() {
        Turma turma = turma(StatusTurma.FECHADA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        TurmaDto dto = abrirTurmaUseCase.executar(new AbrirTurmaCommand(turma.getId().valor()));

        assertThat(dto.status()).isEqualTo(StatusTurma.ABERTA);
        verify(turmaRepository).salvar(turma);
        verify(domainEventPublisher).publishAll(any());
    }

    @Test
    void abrirDeveFalharQuandoTurmaNaoExiste() {
        UUID id = UUID.randomUUID();
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> abrirTurmaUseCase.executar(new AbrirTurmaCommand(id)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveFecharTurma() {
        Turma turma = turma(StatusTurma.ABERTA);
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        TurmaDto dto = fecharTurmaUseCase.executar(new FecharTurmaCommand(turma.getId().valor()));

        assertThat(dto.status()).isEqualTo(StatusTurma.FECHADA);
        verify(turmaRepository).salvar(turma);
        verify(domainEventPublisher).publishAll(any());
    }

    @Test
    void fecharDeveFalharQuandoTurmaNaoExiste() {
        UUID id = UUID.randomUUID();
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fecharTurmaUseCase.executar(new FecharTurmaCommand(id)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private static Turma turma(StatusTurma status) {
        return Turma.cadastrar("TADS-01", DisciplinaId.novo(), PeriodoLetivoId.novo(), 30, status);
    }
}
