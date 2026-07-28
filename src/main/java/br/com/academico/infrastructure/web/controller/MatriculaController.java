package br.com.academico.infrastructure.web.controller;

import br.com.academico.application.command.CancelarMatriculaCommand;
import br.com.academico.application.command.ConfirmarMatriculaCommand;
import br.com.academico.application.query.BuscarMatriculaPorIdQuery;
import br.com.academico.application.query.ListarMatriculasQuery;
import br.com.academico.application.usecase.BuscarMatriculaPorIdUseCase;
import br.com.academico.application.usecase.CancelarMatriculaUseCase;
import br.com.academico.application.usecase.ConfirmarMatriculaUseCase;
import br.com.academico.application.usecase.ListarMatriculasUseCase;
import br.com.academico.application.usecase.RealizarMatriculaUseCase;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.infrastructure.web.request.RealizarMatriculaRequest;
import br.com.academico.infrastructure.web.response.MatriculaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matriculas")
@Tag(name = "Matrículas")
public class MatriculaController {

    private final RealizarMatriculaUseCase realizarMatriculaUseCase;
    private final ConfirmarMatriculaUseCase confirmarMatriculaUseCase;
    private final CancelarMatriculaUseCase cancelarMatriculaUseCase;
    private final ListarMatriculasUseCase listarMatriculasUseCase;
    private final BuscarMatriculaPorIdUseCase buscarMatriculaPorIdUseCase;

    public MatriculaController(
            RealizarMatriculaUseCase realizarMatriculaUseCase,
            ConfirmarMatriculaUseCase confirmarMatriculaUseCase,
            CancelarMatriculaUseCase cancelarMatriculaUseCase,
            ListarMatriculasUseCase listarMatriculasUseCase,
            BuscarMatriculaPorIdUseCase buscarMatriculaPorIdUseCase
    ) {
        this.realizarMatriculaUseCase = realizarMatriculaUseCase;
        this.confirmarMatriculaUseCase = confirmarMatriculaUseCase;
        this.cancelarMatriculaUseCase = cancelarMatriculaUseCase;
        this.listarMatriculasUseCase = listarMatriculasUseCase;
        this.buscarMatriculaPorIdUseCase = buscarMatriculaPorIdUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar matrículas")
    public List<MatriculaResponse> listar(
            @RequestParam(required = false) StatusMatricula status,
            @RequestParam(required = false) UUID alunoId,
            @RequestParam(required = false) UUID turmaId,
            @RequestParam(required = false) UUID periodoLetivoId,
            @RequestParam(required = false) UUID disciplinaId
    ) {
        return listarMatriculasUseCase
                .executar(new ListarMatriculasQuery(status, alunoId, turmaId, periodoLetivoId, disciplinaId))
                .stream()
                .map(MatriculaResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar matrícula por id")
    public MatriculaResponse buscar(@PathVariable UUID id) {
        return MatriculaResponse.from(buscarMatriculaPorIdUseCase.executar(new BuscarMatriculaPorIdQuery(id)));
    }

    @PostMapping
    @Operation(summary = "Realizar matrícula (status PENDENTE)")
    public ResponseEntity<MatriculaResponse> realizar(@Valid @RequestBody RealizarMatriculaRequest request) {
        MatriculaResponse response = MatriculaResponse.from(
                realizarMatriculaUseCase.executar(request.toCommand())
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar matrícula e consumir vaga")
    public MatriculaResponse confirmar(@PathVariable UUID id) {
        return MatriculaResponse.from(confirmarMatriculaUseCase.executar(new ConfirmarMatriculaCommand(id)));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar matrícula confirmada e liberar vaga")
    public MatriculaResponse cancelar(@PathVariable UUID id) {
        return MatriculaResponse.from(cancelarMatriculaUseCase.executar(new CancelarMatriculaCommand(id)));
    }
}
