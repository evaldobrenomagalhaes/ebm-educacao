package br.com.academico.infrastructure.configuration;

import br.com.academico.application.usecase.AbrirTurmaUseCase;
import br.com.academico.application.usecase.AtualizarAlunoUseCase;
import br.com.academico.application.usecase.AtualizarCursoUseCase;
import br.com.academico.application.usecase.AtualizarDisciplinaUseCase;
import br.com.academico.application.usecase.AtualizarPeriodoLetivoUseCase;
import br.com.academico.application.usecase.AtualizarTurmaUseCase;
import br.com.academico.application.usecase.BuscarAlunoPorIdUseCase;
import br.com.academico.application.usecase.BuscarCursoPorIdUseCase;
import br.com.academico.application.usecase.BuscarDisciplinaPorIdUseCase;
import br.com.academico.application.usecase.BuscarMatriculaPorIdUseCase;
import br.com.academico.application.usecase.BuscarPeriodoLetivoPorIdUseCase;
import br.com.academico.application.usecase.BuscarTurmaPorIdUseCase;
import br.com.academico.application.usecase.CadastrarAlunoUseCase;
import br.com.academico.application.usecase.CadastrarCursoUseCase;
import br.com.academico.application.usecase.CadastrarDisciplinaUseCase;
import br.com.academico.application.usecase.CadastrarPeriodoLetivoUseCase;
import br.com.academico.application.usecase.CadastrarTurmaUseCase;
import br.com.academico.application.usecase.CancelarMatriculaUseCase;
import br.com.academico.application.usecase.ConfirmarMatriculaUseCase;
import br.com.academico.application.usecase.ConsultarMatriculasPorAlunoUseCase;
import br.com.academico.application.usecase.ConsultarMatriculasPorTurmaUseCase;
import br.com.academico.application.usecase.ConsultarTurmasDisponiveisUseCase;
import br.com.academico.application.usecase.ExcluirAlunoUseCase;
import br.com.academico.application.usecase.ExcluirCursoUseCase;
import br.com.academico.application.usecase.ExcluirDisciplinaUseCase;
import br.com.academico.application.usecase.ExcluirPeriodoLetivoUseCase;
import br.com.academico.application.usecase.ExcluirTurmaUseCase;
import br.com.academico.application.usecase.FecharTurmaUseCase;
import br.com.academico.application.usecase.ListarAlunosUseCase;
import br.com.academico.application.usecase.ListarCursosUseCase;
import br.com.academico.application.usecase.ListarDisciplinasUseCase;
import br.com.academico.application.usecase.ListarMatriculasUseCase;
import br.com.academico.application.usecase.ListarPeriodosLetivosUseCase;
import br.com.academico.application.usecase.ListarTurmasUseCase;
import br.com.academico.application.usecase.RealizarMatriculaUseCase;
import br.com.academico.domain.event.DomainEventPublisher;
import br.com.academico.domain.repository.AlunoRepository;
import br.com.academico.domain.repository.CursoRepository;
import br.com.academico.domain.repository.DisciplinaRepository;
import br.com.academico.domain.repository.MatriculaRepository;
import br.com.academico.domain.repository.PeriodoLetivoRepository;
import br.com.academico.domain.repository.TurmaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class UseCaseConfiguration {

    @Bean
    public CadastrarAlunoUseCase cadastrarAlunoUseCase(AlunoRepository alunoRepository) {
        return new CadastrarAlunoUseCase(alunoRepository);
    }

    @Bean
    public AtualizarAlunoUseCase atualizarAlunoUseCase(AlunoRepository alunoRepository) {
        return new AtualizarAlunoUseCase(alunoRepository);
    }

    @Bean
    public ExcluirAlunoUseCase excluirAlunoUseCase(AlunoRepository alunoRepository) {
        return new ExcluirAlunoUseCase(alunoRepository);
    }

    @Bean
    public BuscarAlunoPorIdUseCase buscarAlunoPorIdUseCase(AlunoRepository alunoRepository) {
        return new BuscarAlunoPorIdUseCase(alunoRepository);
    }

    @Bean
    public ListarAlunosUseCase listarAlunosUseCase(AlunoRepository alunoRepository) {
        return new ListarAlunosUseCase(alunoRepository);
    }

    @Bean
    public CadastrarCursoUseCase cadastrarCursoUseCase(CursoRepository cursoRepository) {
        return new CadastrarCursoUseCase(cursoRepository);
    }

    @Bean
    public AtualizarCursoUseCase atualizarCursoUseCase(CursoRepository cursoRepository) {
        return new AtualizarCursoUseCase(cursoRepository);
    }

    @Bean
    public ExcluirCursoUseCase excluirCursoUseCase(CursoRepository cursoRepository) {
        return new ExcluirCursoUseCase(cursoRepository);
    }

    @Bean
    public BuscarCursoPorIdUseCase buscarCursoPorIdUseCase(CursoRepository cursoRepository) {
        return new BuscarCursoPorIdUseCase(cursoRepository);
    }

    @Bean
    public ListarCursosUseCase listarCursosUseCase(CursoRepository cursoRepository) {
        return new ListarCursosUseCase(cursoRepository);
    }

    @Bean
    public CadastrarDisciplinaUseCase cadastrarDisciplinaUseCase(
            DisciplinaRepository disciplinaRepository,
            CursoRepository cursoRepository
    ) {
        return new CadastrarDisciplinaUseCase(disciplinaRepository, cursoRepository);
    }

    @Bean
    public AtualizarDisciplinaUseCase atualizarDisciplinaUseCase(
            DisciplinaRepository disciplinaRepository,
            CursoRepository cursoRepository
    ) {
        return new AtualizarDisciplinaUseCase(disciplinaRepository, cursoRepository);
    }

    @Bean
    public ExcluirDisciplinaUseCase excluirDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        return new ExcluirDisciplinaUseCase(disciplinaRepository);
    }

    @Bean
    public BuscarDisciplinaPorIdUseCase buscarDisciplinaPorIdUseCase(DisciplinaRepository disciplinaRepository) {
        return new BuscarDisciplinaPorIdUseCase(disciplinaRepository);
    }

    @Bean
    public ListarDisciplinasUseCase listarDisciplinasUseCase(DisciplinaRepository disciplinaRepository) {
        return new ListarDisciplinasUseCase(disciplinaRepository);
    }

    @Bean
    public CadastrarPeriodoLetivoUseCase cadastrarPeriodoLetivoUseCase(
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new CadastrarPeriodoLetivoUseCase(periodoLetivoRepository);
    }

    @Bean
    public AtualizarPeriodoLetivoUseCase atualizarPeriodoLetivoUseCase(
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new AtualizarPeriodoLetivoUseCase(periodoLetivoRepository);
    }

    @Bean
    public ExcluirPeriodoLetivoUseCase excluirPeriodoLetivoUseCase(
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new ExcluirPeriodoLetivoUseCase(periodoLetivoRepository);
    }

    @Bean
    public BuscarPeriodoLetivoPorIdUseCase buscarPeriodoLetivoPorIdUseCase(
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new BuscarPeriodoLetivoPorIdUseCase(periodoLetivoRepository);
    }

    @Bean
    public ListarPeriodosLetivosUseCase listarPeriodosLetivosUseCase(
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new ListarPeriodosLetivosUseCase(periodoLetivoRepository);
    }

    @Bean
    public CadastrarTurmaUseCase cadastrarTurmaUseCase(
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new CadastrarTurmaUseCase(turmaRepository, disciplinaRepository, periodoLetivoRepository);
    }

    @Bean
    public AtualizarTurmaUseCase atualizarTurmaUseCase(
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            PeriodoLetivoRepository periodoLetivoRepository
    ) {
        return new AtualizarTurmaUseCase(turmaRepository, disciplinaRepository, periodoLetivoRepository);
    }

    @Bean
    public ExcluirTurmaUseCase excluirTurmaUseCase(TurmaRepository turmaRepository) {
        return new ExcluirTurmaUseCase(turmaRepository);
    }

    @Bean
    public BuscarTurmaPorIdUseCase buscarTurmaPorIdUseCase(TurmaRepository turmaRepository) {
        return new BuscarTurmaPorIdUseCase(turmaRepository);
    }

    @Bean
    public ListarTurmasUseCase listarTurmasUseCase(TurmaRepository turmaRepository) {
        return new ListarTurmasUseCase(turmaRepository);
    }

    @Bean
    public RealizarMatriculaUseCase realizarMatriculaUseCase(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        return new RealizarMatriculaUseCase(
                matriculaRepository,
                alunoRepository,
                turmaRepository,
                domainEventPublisher
        );
    }

    @Bean
    public ConfirmarMatriculaUseCase confirmarMatriculaUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        return new ConfirmarMatriculaUseCase(matriculaRepository, turmaRepository, domainEventPublisher);
    }

    @Bean
    public CancelarMatriculaUseCase cancelarMatriculaUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        return new CancelarMatriculaUseCase(matriculaRepository, turmaRepository, domainEventPublisher);
    }

    @Bean
    public AbrirTurmaUseCase abrirTurmaUseCase(
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        return new AbrirTurmaUseCase(turmaRepository, domainEventPublisher);
    }

    @Bean
    public FecharTurmaUseCase fecharTurmaUseCase(
            TurmaRepository turmaRepository,
            DomainEventPublisher domainEventPublisher
    ) {
        return new FecharTurmaUseCase(turmaRepository, domainEventPublisher);
    }

    @Bean
    public ConsultarTurmasDisponiveisUseCase consultarTurmasDisponiveisUseCase(
            ListarTurmasUseCase listarTurmasUseCase
    ) {
        return new ConsultarTurmasDisponiveisUseCase(listarTurmasUseCase);
    }

    @Bean
    public ListarMatriculasUseCase listarMatriculasUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository
    ) {
        return new ListarMatriculasUseCase(matriculaRepository, turmaRepository);
    }

    @Bean
    public BuscarMatriculaPorIdUseCase buscarMatriculaPorIdUseCase(MatriculaRepository matriculaRepository) {
        return new BuscarMatriculaPorIdUseCase(matriculaRepository);
    }

    @Bean
    public ConsultarMatriculasPorAlunoUseCase consultarMatriculasPorAlunoUseCase(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository
    ) {
        return new ConsultarMatriculasPorAlunoUseCase(matriculaRepository, alunoRepository, turmaRepository);
    }

    @Bean
    public ConsultarMatriculasPorTurmaUseCase consultarMatriculasPorTurmaUseCase(
            MatriculaRepository matriculaRepository,
            TurmaRepository turmaRepository
    ) {
        return new ConsultarMatriculasPorTurmaUseCase(matriculaRepository, turmaRepository);
    }
}
