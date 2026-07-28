package br.com.academico.domain.model;

import br.com.academico.domain.event.TurmaAberta;
import br.com.academico.domain.event.TurmaFechada;
import br.com.academico.domain.exception.BusinessRuleViolationException;
import br.com.academico.domain.exception.SemVagasException;
import br.com.academico.domain.exception.TurmaEncerradaException;
import br.com.academico.domain.valueobject.DisciplinaId;
import br.com.academico.domain.valueobject.PeriodoLetivoId;
import br.com.academico.domain.valueobject.StatusTurma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TurmaTest {

    private DisciplinaId disciplinaId;
    private PeriodoLetivoId periodoLetivoId;

    @BeforeEach
    void setUp() {
        disciplinaId = DisciplinaId.novo();
        periodoLetivoId = PeriodoLetivoId.novo();
    }

    @Test
    void deveCadastrarTurmaComVagasIguaisACapacidade() {
        Turma turma = turmaAberta(30);

        assertThat(turma.getId()).isNotNull();
        assertThat(turma.getCodigo()).isEqualTo("TADS-01");
        assertThat(turma.getCapacidadeMaxima()).isEqualTo(30);
        assertThat(turma.getVagasDisponiveis()).isEqualTo(30);
        assertThat(turma.getStatus()).isEqualTo(StatusTurma.ABERTA);
        assertThat(turma.estaAberta()).isTrue();
        assertThat(turma.possuiVagas()).isTrue();
    }

    @Test
    void deveRejeitarCapacidadeZeroOuNegativa() {
        assertThatThrownBy(() -> turmaAberta(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maior que zero");

        assertThatThrownBy(() -> turmaAberta(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maior que zero");
    }

    @Test
    void deveAbrirTurmaFechadaERegistrarEvento() {
        Turma turma = turmaFechada(20);

        turma.abrir();

        assertThat(turma.getStatus()).isEqualTo(StatusTurma.ABERTA);
        assertThat(turma.pullDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(TurmaAberta.class);
    }

    @Test
    void naoDeveAbrirTurmaJaAberta() {
        Turma turma = turmaAberta(20);

        assertThatThrownBy(turma::abrir)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("já está aberta");
    }

    @Test
    void deveFecharTurmaAbertaERegistrarEvento() {
        Turma turma = turmaAberta(20);

        turma.fechar();

        assertThat(turma.getStatus()).isEqualTo(StatusTurma.FECHADA);
        assertThat(turma.estaAberta()).isFalse();
        assertThat(turma.pullDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(TurmaFechada.class);
    }

    @Test
    void naoDeveFecharTurmaJaFechada() {
        Turma turma = turmaFechada(20);

        assertThatThrownBy(turma::fechar)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("já está fechada");
    }

    @Test
    void inv03_turmaFechadaNaoAceitaNovasMatriculas() {
        Turma turma = turmaFechada(10);

        assertThatThrownBy(turma::garantirAbertaParaMatricula)
                .isInstanceOf(TurmaEncerradaException.class)
                .hasMessageContaining("fechada");
    }

    @Test
    void devePermitirMatriculaQuandoTurmaAberta() {
        Turma turma = turmaAberta(10);

        turma.garantirAbertaParaMatricula();
    }

    @Test
    void inv06_confirmacaoConsomeUmaVaga() {
        Turma turma = turmaAberta(2);

        turma.consumirVaga();

        assertThat(turma.getVagasDisponiveis()).isEqualTo(1);
        assertThat(turma.possuiVagas()).isTrue();
    }

    @Test
    void inv01_naoPermiteVagasNegativasAoConsumirSemDisponibilidade() {
        Turma turma = turmaAberta(1);
        turma.consumirVaga();

        assertThatThrownBy(turma::consumirVaga)
                .isInstanceOf(SemVagasException.class)
                .hasMessageContaining("sem vagas");
        assertThat(turma.getVagasDisponiveis()).isZero();
    }

    @Test
    void inv05_cancelamentoDevolveUmaVaga() {
        Turma turma = turmaAberta(2);
        turma.consumirVaga();

        turma.liberarVaga();

        assertThat(turma.getVagasDisponiveis()).isEqualTo(2);
    }

    @Test
    void inv02_naoPermiteLiberarVagaAlemDaCapacidade() {
        Turma turma = turmaAberta(2);

        assertThatThrownBy(turma::liberarVaga)
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Não há vagas ocupadas");
        assertThat(turma.getVagasDisponiveis()).isEqualTo(2);
    }

    @Test
    void deveAtualizarCapacidadePreservandoOcupacao() {
        Turma turma = turmaAberta(5);
        turma.consumirVaga();
        turma.consumirVaga();

        turma.atualizar("TADS-02", disciplinaId, periodoLetivoId, 8);

        assertThat(turma.getCodigo()).isEqualTo("TADS-02");
        assertThat(turma.getCapacidadeMaxima()).isEqualTo(8);
        assertThat(turma.getVagasDisponiveis()).isEqualTo(6);
    }

    @Test
    void naoDeveReduzirCapacidadeAbaixoDasVagasOcupadas() {
        Turma turma = turmaAberta(5);
        turma.consumirVaga();
        turma.consumirVaga();
        turma.consumirVaga();

        assertThatThrownBy(() -> turma.atualizar("TADS-01", disciplinaId, periodoLetivoId, 2))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("inferior às vagas já ocupadas");
    }

    @Test
    void pullDomainEventsDeveLimparEventosPendentes() {
        Turma turma = turmaFechada(10);
        turma.abrir();

        assertThat(turma.pullDomainEvents()).hasSize(1);
        assertThat(turma.pullDomainEvents()).isEmpty();
        assertThat(turma.domainEvents()).isEmpty();
    }

    private Turma turmaAberta(int capacidade) {
        return Turma.cadastrar(
                "TADS-01",
                disciplinaId,
                periodoLetivoId,
                capacidade,
                StatusTurma.ABERTA
        );
    }

    private Turma turmaFechada(int capacidade) {
        return Turma.cadastrar(
                "TADS-01",
                disciplinaId,
                periodoLetivoId,
                capacidade,
                StatusTurma.FECHADA
        );
    }
}
