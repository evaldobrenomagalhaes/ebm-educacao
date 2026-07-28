package br.com.academico.application.usecase;

import br.com.academico.application.command.CancelarMatriculaCommand;
import br.com.academico.application.command.ConfirmarMatriculaCommand;
import br.com.academico.application.command.RealizarMatriculaCommand;
import br.com.academico.application.dto.MatriculaDto;
import br.com.academico.domain.event.DomainEventPublisher;
import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.exception.DuplicateMatriculaException;
import br.com.academico.domain.exception.EntityNotFoundException;
import br.com.academico.domain.model.Aluno;
import br.com.academico.domain.model.Matricula;
import br.com.academico.domain.model.Turma;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.TurmaRepository;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.Email;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.StatusMatricula;
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
class MatriculaUseCasesTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private TurmaRepository turmaRepository;
    @Mock
    private DomainEventPublisher domainEventPublisher;

    private RealizarMatriculaUseCase realizar;
    private ConfirmarMatriculaUseCase confirmar;
    private CancelarMatriculaUseCase cancelar;

    private Aluno aluno;
    private Turma turma;

    @BeforeEach
    void setUp() {
        realizar = new RealizarMatriculaUseCase(
                matriculaRepository, alunoRepository, turmaRepository, domainEventPublisher);
        confirmar = new ConfirmarMatriculaUseCase(matriculaRepository, turmaRepository, domainEventPublisher);
        cancelar = new CancelarMatriculaUseCase(matriculaRepository, turmaRepository, domainEventPublisher);
        aluno = Aluno.cadastrar("Ana", Email.de("ana@email.com"), SituacaoAcademica.ATIVO);
        turma = Turma.cadastrar("T1", DisciplinaId.novo(), PeriodoLetivoId.novo(), 30, StatusTurma.ABERTA);
    }

    @Test
    void deveRealizarMatricula() {
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(matriculaRepository.existePorAlunoETurma(aluno.getId(), turma.getId())).thenReturn(false);

        MatriculaDto dto = realizar.executar(
                new RealizarMatriculaCommand(aluno.getId().valor(), turma.getId().valor()));

        assertThat(dto.status()).isEqualTo(StatusMatricula.PENDENTE);
        verify(matriculaRepository).salvar(any());
        verify(domainEventPublisher).publishAll(any());
    }

    @Test
    void realizarDeveFalharQuandoAlunoNaoExiste() {
        when(alunoRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> realizar.executar(
                new RealizarMatriculaCommand(UUID.randomUUID(), turma.getId().valor())))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Aluno");
    }

    @Test
    void realizarDeveFalharQuandoTurmaNaoExiste() {
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));
        when(turmaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> realizar.executar(
                new RealizarMatriculaCommand(aluno.getId().valor(), UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Turma");
    }

    @Test
    void realizarDeveFalharQuandoDuplicada() {
        when(alunoRepository.buscarPorId(aluno.getId())).thenReturn(Optional.of(aluno));
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));
        when(matriculaRepository.existePorAlunoETurma(aluno.getId(), turma.getId())).thenReturn(true);

        assertThatThrownBy(() -> realizar.executar(
                new RealizarMatriculaCommand(aluno.getId().valor(), turma.getId().valor())))
                .isInstanceOf(DuplicateMatriculaException.class);
    }

    @Test
    void realizarDeveFalharQuandoAlunoInativo() {
        Aluno inativo = Aluno.cadastrar("Inativo", Email.de("inativo@email.com"), SituacaoAcademica.INATIVO);
        when(alunoRepository.buscarPorId(inativo.getId())).thenReturn(Optional.of(inativo));

        assertThatThrownBy(() -> realizar.executar(
                new RealizarMatriculaCommand(inativo.getId().valor(), turma.getId().valor())))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Aluno inativo não pode realizar matrícula");
    }

    @Test
    void deveConfirmarMatricula() {
        Matricula matricula = Matricula.realizar(aluno.getId(), turma.getId());
        matricula.pullDomainEvents();
        when(matriculaRepository.buscarPorId(matricula.getId())).thenReturn(Optional.of(matricula));
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        MatriculaDto dto = confirmar.executar(new ConfirmarMatriculaCommand(matricula.getId().valor()));

        assertThat(dto.status()).isEqualTo(StatusMatricula.CONFIRMADA);
        verify(turmaRepository).salvar(turma);
        verify(domainEventPublisher).publishAll(any());
    }

    @Test
    void confirmarDeveFalharQuandoMatriculaNaoExiste() {
        when(matriculaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> confirmar.executar(new ConfirmarMatriculaCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveCancelarMatricula() {
        Matricula matricula = Matricula.realizar(aluno.getId(), turma.getId());
        matricula.pullDomainEvents();
        matricula.confirmar();
        matricula.pullDomainEvents();
        turma.consumirVaga();
        when(matriculaRepository.buscarPorId(matricula.getId())).thenReturn(Optional.of(matricula));
        when(turmaRepository.buscarPorId(turma.getId())).thenReturn(Optional.of(turma));

        MatriculaDto dto = cancelar.executar(new CancelarMatriculaCommand(matricula.getId().valor()));

        assertThat(dto.status()).isEqualTo(StatusMatricula.CANCELADA);
        verify(turmaRepository).salvar(turma);
        verify(domainEventPublisher).publishAll(any());
    }

    @Test
    void cancelarDeveFalharQuandoMatriculaNaoExiste() {
        when(matriculaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cancelar.executar(new CancelarMatriculaCommand(UUID.randomUUID())))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
