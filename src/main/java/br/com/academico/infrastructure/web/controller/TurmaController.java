package br.com.academico.infrastructure.web.controller;

import br.com.academico.application.command.AbrirTurmaCommand;
import br.com.academico.application.command.ExcluirTurmaCommand;
import br.com.academico.application.command.FecharTurmaCommand;
import br.com.academico.application.query.BuscarTurmaPorIdQuery;
import br.com.academico.application.query.ConsultarMatriculasPorTurmaQuery;
import br.com.academico.application.query.ConsultarTurmasDisponiveisQuery;
import br.com.academico.application.query.ListarTurmasQuery;
import br.com.academico.application.usecase.AbrirTurmaUseCase;
import br.com.academico.application.usecase.AtualizarTurmaUseCase;
import br.com.academico.application.usecase.BuscarTurmaPorIdUseCase;
import br.com.academico.application.usecase.CadastrarTurmaUseCase;
import br.com.academico.application.usecase.ConsultarMatriculasPorTurmaUseCase;
import br.com.academico.application.usecase.ConsultarTurmasDisponiveisUseCase;
import br.com.academico.application.usecase.ExcluirTurmaUseCase;
import br.com.academico.application.usecase.FecharTurmaUseCase;
import br.com.academico.application.usecase.ListarTurmasUseCase;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.StatusTurma;
import br.com.academico.infrastructure.web.request.AtualizarTurmaRequest;
import br.com.academico.infrastructure.web.request.CadastrarTurmaRequest;
import br.com.academico.infrastructure.web.response.MatriculaResponse;
import br.com.academico.infrastructure.web.response.TurmaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/turmas")
@Tag(name = "Turmas")
public class TurmaController {

    private final CadastrarTurmaUseCase cadastrarTurmaUseCase;
    private final AtualizarTurmaUseCase atualizarTurmaUseCase;
    private final ExcluirTurmaUseCase excluirTurmaUseCase;
    private final BuscarTurmaPorIdUseCase buscarTurmaPorIdUseCase;
    private final ListarTurmasUseCase listarTurmasUseCase;
    private final ConsultarTurmasDisponiveisUseCase consultarTurmasDisponiveisUseCase;
    private final AbrirTurmaUseCase abrirTurmaUseCase;
    private final FecharTurmaUseCase fecharTurmaUseCase;
    private final ConsultarMatriculasPorTurmaUseCase consultarMatriculasPorTurmaUseCase;

    public TurmaController(
            CadastrarTurmaUseCase cadastrarTurmaUseCase,
            AtualizarTurmaUseCase atualizarTurmaUseCase,
            ExcluirTurmaUseCase excluirTurmaUseCase,
            BuscarTurmaPorIdUseCase buscarTurmaPorIdUseCase,
            ListarTurmasUseCase listarTurmasUseCase,
            ConsultarTurmasDisponiveisUseCase consultarTurmasDisponiveisUseCase,
            AbrirTurmaUseCase abrirTurmaUseCase,
            FecharTurmaUseCase fecharTurmaUseCase,
            ConsultarMatriculasPorTurmaUseCase consultarMatriculasPorTurmaUseCase
    ) {
        this.cadastrarTurmaUseCase = cadastrarTurmaUseCase;
        this.atualizarTurmaUseCase = atualizarTurmaUseCase;
        this.excluirTurmaUseCase = excluirTurmaUseCase;
        this.buscarTurmaPorIdUseCase = buscarTurmaPorIdUseCase;
        this.listarTurmasUseCase = listarTurmasUseCase;
        this.consultarTurmasDisponiveisUseCase = consultarTurmasDisponiveisUseCase;
        this.abrirTurmaUseCase = abrirTurmaUseCase;
        this.fecharTurmaUseCase = fecharTurmaUseCase;
        this.consultarMatriculasPorTurmaUseCase = consultarMatriculasPorTurmaUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar turma")
    public ResponseEntity<TurmaResponse> cadastrar(@Valid @RequestBody CadastrarTurmaRequest request) {
        TurmaResponse response = TurmaResponse.from(cadastrarTurmaUseCase.executar(request.toCommand()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar turmas")
    public List<TurmaResponse> listar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) StatusTurma status,
            @RequestParam(required = false) UUID disciplinaId,
            @RequestParam(required = false) UUID periodoLetivoId,
            @RequestParam(required = false) Boolean comVagas
    ) {
        return listarTurmasUseCase
                .executar(new ListarTurmasQuery(codigo, status, disciplinaId, periodoLetivoId, comVagas))
                .stream()
                .map(TurmaResponse::from)
                .toList();
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Consultar turmas disponíveis (abertas com vagas)")
    public List<TurmaResponse> disponiveis(
            @RequestParam(required = false) UUID disciplinaId,
            @RequestParam(required = false) UUID periodoLetivoId
    ) {
        return consultarTurmasDisponiveisUseCase
                .executar(new ConsultarTurmasDisponiveisQuery(disciplinaId, periodoLetivoId))
                .stream()
                .map(TurmaResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar turma por id")
    public TurmaResponse buscar(@PathVariable UUID id) {
        return TurmaResponse.from(buscarTurmaPorIdUseCase.executar(new BuscarTurmaPorIdQuery(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar turma")
    public TurmaResponse atualizar(@PathVariable UUID id, @Valid @RequestBody AtualizarTurmaRequest request) {
        return TurmaResponse.from(atualizarTurmaUseCase.executar(request.toCommand(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir turma")
    public void excluir(@PathVariable UUID id) {
        excluirTurmaUseCase.executar(new ExcluirTurmaCommand(id));
    }

    @PostMapping("/{id}/abrir")
    @Operation(summary = "Abrir turma para matrículas")
    public TurmaResponse abrir(@PathVariable UUID id) {
        return TurmaResponse.from(abrirTurmaUseCase.executar(new AbrirTurmaCommand(id)));
    }

    @PostMapping("/{id}/fechar")
    @Operation(summary = "Fechar turma")
    public TurmaResponse fechar(@PathVariable UUID id) {
        return TurmaResponse.from(fecharTurmaUseCase.executar(new FecharTurmaCommand(id)));
    }

    @GetMapping("/{id}/matriculas")
    @Operation(summary = "Consultar matrículas da turma")
    public List<MatriculaResponse> matriculas(
            @PathVariable UUID id,
            @RequestParam(required = false) StatusMatricula status,
            @RequestParam(required = false) UUID periodoLetivoId,
            @RequestParam(required = false) UUID disciplinaId
    ) {
        return consultarMatriculasPorTurmaUseCase
                .executar(new ConsultarMatriculasPorTurmaQuery(id, status, periodoLetivoId, disciplinaId))
                .stream()
                .map(MatriculaResponse::from)
                .toList();
    }
}
