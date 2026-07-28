package br.com.academico.infrastructure.web.controller;

import br.com.academico.application.command.ExcluirDisciplinaCommand;
import br.com.academico.application.query.BuscarDisciplinaPorIdQuery;
import br.com.academico.application.query.ListarDisciplinasQuery;
import br.com.academico.application.usecase.AtualizarDisciplinaUseCase;
import br.com.academico.application.usecase.BuscarDisciplinaPorIdUseCase;
import br.com.academico.application.usecase.CadastrarDisciplinaUseCase;
import br.com.academico.application.usecase.ExcluirDisciplinaUseCase;
import br.com.academico.application.usecase.ListarDisciplinasUseCase;
import br.com.academico.infrastructure.web.request.AtualizarDisciplinaRequest;
import br.com.academico.infrastructure.web.request.CadastrarDisciplinaRequest;
import br.com.academico.infrastructure.web.response.DisciplinaResponse;
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
@RequestMapping("/api/disciplinas")
@Tag(name = "Disciplinas")
public class DisciplinaController {

    private final CadastrarDisciplinaUseCase cadastrarDisciplinaUseCase;
    private final AtualizarDisciplinaUseCase atualizarDisciplinaUseCase;
    private final ExcluirDisciplinaUseCase excluirDisciplinaUseCase;
    private final BuscarDisciplinaPorIdUseCase buscarDisciplinaPorIdUseCase;
    private final ListarDisciplinasUseCase listarDisciplinasUseCase;

    public DisciplinaController(
            CadastrarDisciplinaUseCase cadastrarDisciplinaUseCase,
            AtualizarDisciplinaUseCase atualizarDisciplinaUseCase,
            ExcluirDisciplinaUseCase excluirDisciplinaUseCase,
            BuscarDisciplinaPorIdUseCase buscarDisciplinaPorIdUseCase,
            ListarDisciplinasUseCase listarDisciplinasUseCase
    ) {
        this.cadastrarDisciplinaUseCase = cadastrarDisciplinaUseCase;
        this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;
        this.excluirDisciplinaUseCase = excluirDisciplinaUseCase;
        this.buscarDisciplinaPorIdUseCase = buscarDisciplinaPorIdUseCase;
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar disciplina")
    public ResponseEntity<DisciplinaResponse> cadastrar(@Valid @RequestBody CadastrarDisciplinaRequest request) {
        DisciplinaResponse response = DisciplinaResponse.from(
                cadastrarDisciplinaUseCase.executar(request.toCommand())
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar disciplinas")
    public List<DisciplinaResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) UUID cursoId
    ) {
        return listarDisciplinasUseCase.executar(new ListarDisciplinasQuery(nome, codigo, cursoId)).stream()
                .map(DisciplinaResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar disciplina por id")
    public DisciplinaResponse buscar(@PathVariable UUID id) {
        return DisciplinaResponse.from(buscarDisciplinaPorIdUseCase.executar(new BuscarDisciplinaPorIdQuery(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar disciplina")
    public DisciplinaResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarDisciplinaRequest request
    ) {
        return DisciplinaResponse.from(atualizarDisciplinaUseCase.executar(request.toCommand(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir disciplina")
    public void excluir(@PathVariable UUID id) {
        excluirDisciplinaUseCase.executar(new ExcluirDisciplinaCommand(id));
    }
}
