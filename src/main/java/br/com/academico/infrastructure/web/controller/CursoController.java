package br.com.academico.infrastructure.web.controller;

import br.com.academico.application.command.ExcluirCursoCommand;
import br.com.academico.application.query.BuscarCursoPorIdQuery;
import br.com.academico.application.query.ListarCursosQuery;
import br.com.academico.application.usecase.AtualizarCursoUseCase;
import br.com.academico.application.usecase.BuscarCursoPorIdUseCase;
import br.com.academico.application.usecase.CadastrarCursoUseCase;
import br.com.academico.application.usecase.ExcluirCursoUseCase;
import br.com.academico.application.usecase.ListarCursosUseCase;
import br.com.academico.domain.valueobject.SituacaoCurso;
import br.com.academico.infrastructure.web.request.AtualizarCursoRequest;
import br.com.academico.infrastructure.web.request.CadastrarCursoRequest;
import br.com.academico.infrastructure.web.response.CursoResponse;
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
@RequestMapping("/api/cursos")
@Tag(name = "Cursos")
public class CursoController {

    private final CadastrarCursoUseCase cadastrarCursoUseCase;
    private final AtualizarCursoUseCase atualizarCursoUseCase;
    private final ExcluirCursoUseCase excluirCursoUseCase;
    private final BuscarCursoPorIdUseCase buscarCursoPorIdUseCase;
    private final ListarCursosUseCase listarCursosUseCase;

    public CursoController(
            CadastrarCursoUseCase cadastrarCursoUseCase,
            AtualizarCursoUseCase atualizarCursoUseCase,
            ExcluirCursoUseCase excluirCursoUseCase,
            BuscarCursoPorIdUseCase buscarCursoPorIdUseCase,
            ListarCursosUseCase listarCursosUseCase
    ) {
        this.cadastrarCursoUseCase = cadastrarCursoUseCase;
        this.atualizarCursoUseCase = atualizarCursoUseCase;
        this.excluirCursoUseCase = excluirCursoUseCase;
        this.buscarCursoPorIdUseCase = buscarCursoPorIdUseCase;
        this.listarCursosUseCase = listarCursosUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar curso")
    public ResponseEntity<CursoResponse> cadastrar(@Valid @RequestBody CadastrarCursoRequest request) {
        CursoResponse response = CursoResponse.from(cadastrarCursoUseCase.executar(request.toCommand()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar cursos")
    public List<CursoResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) SituacaoCurso situacao
    ) {
        return listarCursosUseCase.executar(new ListarCursosQuery(nome, situacao)).stream()
                .map(CursoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por id")
    public CursoResponse buscar(@PathVariable UUID id) {
        return CursoResponse.from(buscarCursoPorIdUseCase.executar(new BuscarCursoPorIdQuery(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar curso")
    public CursoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody AtualizarCursoRequest request) {
        return CursoResponse.from(atualizarCursoUseCase.executar(request.toCommand(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir curso")
    public void excluir(@PathVariable UUID id) {
        excluirCursoUseCase.executar(new ExcluirCursoCommand(id));
    }
}
