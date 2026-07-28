package br.com.academico.infrastructure.web.controller;

import br.com.academico.application.command.ExcluirPeriodoLetivoCommand;
import br.com.academico.application.query.BuscarPeriodoLetivoPorIdQuery;
import br.com.academico.application.query.ListarPeriodosLetivosQuery;
import br.com.academico.application.usecase.AtualizarPeriodoLetivoUseCase;
import br.com.academico.application.usecase.BuscarPeriodoLetivoPorIdUseCase;
import br.com.academico.application.usecase.CadastrarPeriodoLetivoUseCase;
import br.com.academico.application.usecase.ExcluirPeriodoLetivoUseCase;
import br.com.academico.application.usecase.ListarPeriodosLetivosUseCase;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import br.com.academico.infrastructure.web.request.AtualizarPeriodoLetivoRequest;
import br.com.academico.infrastructure.web.request.CadastrarPeriodoLetivoRequest;
import br.com.academico.infrastructure.web.response.PeriodoLetivoResponse;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/periodos-letivos")
@Tag(name = "Períodos Letivos")
public class PeriodoLetivoController {

    private final CadastrarPeriodoLetivoUseCase cadastrarPeriodoLetivoUseCase;
    private final AtualizarPeriodoLetivoUseCase atualizarPeriodoLetivoUseCase;
    private final ExcluirPeriodoLetivoUseCase excluirPeriodoLetivoUseCase;
    private final BuscarPeriodoLetivoPorIdUseCase buscarPeriodoLetivoPorIdUseCase;
    private final ListarPeriodosLetivosUseCase listarPeriodosLetivosUseCase;

    public PeriodoLetivoController(
            CadastrarPeriodoLetivoUseCase cadastrarPeriodoLetivoUseCase,
            AtualizarPeriodoLetivoUseCase atualizarPeriodoLetivoUseCase,
            ExcluirPeriodoLetivoUseCase excluirPeriodoLetivoUseCase,
            BuscarPeriodoLetivoPorIdUseCase buscarPeriodoLetivoPorIdUseCase,
            ListarPeriodosLetivosUseCase listarPeriodosLetivosUseCase
    ) {
        this.cadastrarPeriodoLetivoUseCase = cadastrarPeriodoLetivoUseCase;
        this.atualizarPeriodoLetivoUseCase = atualizarPeriodoLetivoUseCase;
        this.excluirPeriodoLetivoUseCase = excluirPeriodoLetivoUseCase;
        this.buscarPeriodoLetivoPorIdUseCase = buscarPeriodoLetivoPorIdUseCase;
        this.listarPeriodosLetivosUseCase = listarPeriodosLetivosUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar período letivo")
    public ResponseEntity<PeriodoLetivoResponse> cadastrar(
            @Valid @RequestBody CadastrarPeriodoLetivoRequest request
    ) {
        PeriodoLetivoResponse response = PeriodoLetivoResponse.from(
                cadastrarPeriodoLetivoUseCase.executar(request.toCommand())
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar períodos letivos")
    public List<PeriodoLetivoResponse> listar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) SituacaoPeriodoLetivo situacao,
            @RequestParam(required = false) LocalDate dataInicioDe,
            @RequestParam(required = false) LocalDate dataInicioAte,
            @RequestParam(required = false) LocalDate dataTerminoDe,
            @RequestParam(required = false) LocalDate dataTerminoAte,
            @RequestParam(required = false) LocalDate vigenteEm
    ) {
        return listarPeriodosLetivosUseCase
                .executar(new ListarPeriodosLetivosQuery(
                        codigo,
                        situacao,
                        dataInicioDe,
                        dataInicioAte,
                        dataTerminoDe,
                        dataTerminoAte,
                        vigenteEm
                ))
                .stream()
                .map(PeriodoLetivoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar período letivo por id")
    public PeriodoLetivoResponse buscar(@PathVariable UUID id) {
        return PeriodoLetivoResponse.from(
                buscarPeriodoLetivoPorIdUseCase.executar(new BuscarPeriodoLetivoPorIdQuery(id))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar período letivo")
    public PeriodoLetivoResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarPeriodoLetivoRequest request
    ) {
        return PeriodoLetivoResponse.from(atualizarPeriodoLetivoUseCase.executar(request.toCommand(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir período letivo")
    public void excluir(@PathVariable UUID id) {
        excluirPeriodoLetivoUseCase.executar(new ExcluirPeriodoLetivoCommand(id));
    }
}
