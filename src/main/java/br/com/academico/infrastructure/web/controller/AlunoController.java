package br.com.academico.infrastructure.web.controller;

import br.com.academico.application.command.ExcluirAlunoCommand;
import br.com.academico.application.query.BuscarAlunoPorIdQuery;
import br.com.academico.application.query.ConsultarMatriculasPorAlunoQuery;
import br.com.academico.application.query.ListarAlunosQuery;
import br.com.academico.application.usecase.AtualizarAlunoUseCase;
import br.com.academico.application.usecase.BuscarAlunoPorIdUseCase;
import br.com.academico.application.usecase.CadastrarAlunoUseCase;
import br.com.academico.application.usecase.ConsultarMatriculasPorAlunoUseCase;
import br.com.academico.application.usecase.ExcluirAlunoUseCase;
import br.com.academico.application.usecase.ListarAlunosUseCase;
import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.infrastructure.web.request.AtualizarAlunoRequest;
import br.com.academico.infrastructure.web.request.CadastrarAlunoRequest;
import br.com.academico.infrastructure.web.response.AlunoResponse;
import br.com.academico.infrastructure.web.response.MatriculaResponse;
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
@RequestMapping("/api/alunos")
@Tag(name = "Alunos")
public class AlunoController {

    private final CadastrarAlunoUseCase cadastrarAlunoUseCase;
    private final AtualizarAlunoUseCase atualizarAlunoUseCase;
    private final ExcluirAlunoUseCase excluirAlunoUseCase;
    private final BuscarAlunoPorIdUseCase buscarAlunoPorIdUseCase;
    private final ListarAlunosUseCase listarAlunosUseCase;
    private final ConsultarMatriculasPorAlunoUseCase consultarMatriculasPorAlunoUseCase;

    public AlunoController(
            CadastrarAlunoUseCase cadastrarAlunoUseCase,
            AtualizarAlunoUseCase atualizarAlunoUseCase,
            ExcluirAlunoUseCase excluirAlunoUseCase,
            BuscarAlunoPorIdUseCase buscarAlunoPorIdUseCase,
            ListarAlunosUseCase listarAlunosUseCase,
            ConsultarMatriculasPorAlunoUseCase consultarMatriculasPorAlunoUseCase
    ) {
        this.cadastrarAlunoUseCase = cadastrarAlunoUseCase;
        this.atualizarAlunoUseCase = atualizarAlunoUseCase;
        this.excluirAlunoUseCase = excluirAlunoUseCase;
        this.buscarAlunoPorIdUseCase = buscarAlunoPorIdUseCase;
        this.listarAlunosUseCase = listarAlunosUseCase;
        this.consultarMatriculasPorAlunoUseCase = consultarMatriculasPorAlunoUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar aluno")
    public ResponseEntity<AlunoResponse> cadastrar(@Valid @RequestBody CadastrarAlunoRequest request) {
        AlunoResponse response = AlunoResponse.from(cadastrarAlunoUseCase.executar(request.toCommand()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar alunos")
    public List<AlunoResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) SituacaoAcademica situacaoAcademica
    ) {
        return listarAlunosUseCase.executar(new ListarAlunosQuery(nome, email, situacaoAcademica)).stream()
                .map(AlunoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por id")
    public AlunoResponse buscar(@PathVariable UUID id) {
        return AlunoResponse.from(buscarAlunoPorIdUseCase.executar(new BuscarAlunoPorIdQuery(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno")
    public AlunoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody AtualizarAlunoRequest request) {
        return AlunoResponse.from(atualizarAlunoUseCase.executar(request.toCommand(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir aluno")
    public void excluir(@PathVariable UUID id) {
        excluirAlunoUseCase.executar(new ExcluirAlunoCommand(id));
    }

    @GetMapping("/{id}/matriculas")
    @Operation(summary = "Consultar matrículas do aluno")
    public List<MatriculaResponse> matriculas(
            @PathVariable UUID id,
            @RequestParam(required = false) StatusMatricula status,
            @RequestParam(required = false) UUID periodoLetivoId,
            @RequestParam(required = false) UUID disciplinaId
    ) {
        return consultarMatriculasPorAlunoUseCase
                .executar(new ConsultarMatriculasPorAlunoQuery(id, status, periodoLetivoId, disciplinaId))
                .stream()
                .map(MatriculaResponse::from)
                .toList();
    }
}
