package br.com.academico.integration;

import br.com.academico.domain.valueobject.SituacaoAcademica;
import br.com.academico.domain.valueobject.SituacaoCurso;
import br.com.academico.domain.valueobject.SituacaoPeriodoLetivo;
import br.com.academico.domain.valueobject.StatusMatricula;
import br.com.academico.domain.valueobject.StatusTurma;
import br.com.academico.infrastructure.web.response.AlunoResponse;
import br.com.academico.infrastructure.web.response.CursoResponse;
import br.com.academico.infrastructure.web.response.DisciplinaResponse;
import br.com.academico.infrastructure.web.response.MatriculaResponse;
import br.com.academico.infrastructure.web.response.PeriodoLetivoResponse;
import br.com.academico.infrastructure.web.response.TurmaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MatriculaFlowIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRealizarConfirmarECancelarMatriculaAtualizandoVagas() throws Exception {
        Catalogo catalogo = cadastrarCatalogo(2, StatusTurma.ABERTA);
        AlunoResponse aluno = cadastrarAluno("Ana Silva");

        MatriculaResponse realizada = realizarMatricula(aluno.id(), catalogo.turmaId());
        assertThat(realizada.status()).isEqualTo(StatusMatricula.PENDENTE);
        assertThat(buscarTurma(catalogo.turmaId()).vagasDisponiveis()).isEqualTo(2);

        MatriculaResponse confirmada = confirmarMatricula(realizada.id());
        assertThat(confirmada.status()).isEqualTo(StatusMatricula.CONFIRMADA);
        assertThat(buscarTurma(catalogo.turmaId()).vagasDisponiveis()).isEqualTo(1);

        MatriculaResponse cancelada = cancelarMatricula(realizada.id());
        assertThat(cancelada.status()).isEqualTo(StatusMatricula.CANCELADA);
        assertThat(buscarTurma(catalogo.turmaId()).vagasDisponiveis()).isEqualTo(2);
    }

    @Test
    void deveRecusarMatriculaDuplicadaDoMesmoAlunoNaTurma() throws Exception {
        Catalogo catalogo = cadastrarCatalogo(5, StatusTurma.ABERTA);
        AlunoResponse aluno = cadastrarAluno("Bruno Costa");

        realizarMatricula(aluno.id(), catalogo.turmaId());

        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "alunoId", aluno.id(),
                                "turmaId", catalogo.turmaId()
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflito"))
                .andExpect(jsonPath("$.detail").value("Aluno já possui matrícula nesta turma"));
    }

    @Test
    void deveRecusarMatriculaQuandoAlunoInativo() throws Exception {
        Catalogo catalogo = cadastrarCatalogo(5, StatusTurma.ABERTA);
        AlunoResponse aluno = cadastrarAluno("Fábio Inativo", SituacaoAcademica.INATIVO);

        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "alunoId", aluno.id(),
                                "turmaId", catalogo.turmaId()
                        ))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Regra de negócio violada"))
                .andExpect(jsonPath("$.detail").value("Aluno inativo não pode realizar matrícula"));
    }

    @Test
    void deveRecusarMatriculaEmTurmaFechada() throws Exception {
        Catalogo catalogo = cadastrarCatalogo(5, StatusTurma.FECHADA);
        AlunoResponse aluno = cadastrarAluno("Carla Dias");

        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "alunoId", aluno.id(),
                                "turmaId", catalogo.turmaId()
                        ))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Regra de negócio violada"))
                .andExpect(jsonPath("$.detail").value("Turma fechada não aceita novas matrículas"));
    }

    @Test
    void deveRecusarConfirmacaoQuandoTurmaNaoPossuiVagas() throws Exception {
        Catalogo catalogo = cadastrarCatalogo(1, StatusTurma.ABERTA);
        AlunoResponse aluno1 = cadastrarAluno("Diego Alves");
        AlunoResponse aluno2 = cadastrarAluno("Elena Rocha");

        MatriculaResponse primeira = realizarMatricula(aluno1.id(), catalogo.turmaId());
        confirmarMatricula(primeira.id());
        assertThat(buscarTurma(catalogo.turmaId()).vagasDisponiveis()).isEqualTo(0);

        MatriculaResponse segunda = realizarMatricula(aluno2.id(), catalogo.turmaId());

        mockMvc.perform(post("/api/matriculas/{id}/confirmar", segunda.id()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflito"))
                .andExpect(jsonPath("$.detail").value("Turma sem vagas disponíveis"));
    }

    private Catalogo cadastrarCatalogo(int capacidadeMaxima, StatusTurma statusTurma) throws Exception {
        String sufixo = UUID.randomUUID().toString().substring(0, 8);

        CursoResponse curso = postJson("/api/cursos", Map.of(
                "nome", "Curso " + sufixo,
                "situacao", SituacaoCurso.ATIVO
        ), CursoResponse.class);

        DisciplinaResponse disciplina = postJson("/api/disciplinas", Map.of(
                "nome", "Disciplina " + sufixo,
                "codigo", "DISC-" + sufixo,
                "cursoId", curso.id()
        ), DisciplinaResponse.class);

        PeriodoLetivoResponse periodo = postJson("/api/periodos-letivos", Map.of(
                "codigo", "2026." + sufixo,
                "dataInicio", LocalDate.of(2026, 2, 1),
                "dataTermino", LocalDate.of(2026, 6, 30),
                "situacao", SituacaoPeriodoLetivo.ABERTO
        ), PeriodoLetivoResponse.class);

        TurmaResponse turma = postJson("/api/turmas", Map.of(
                "codigo", "TUR-" + sufixo,
                "disciplinaId", disciplina.id(),
                "periodoLetivoId", periodo.id(),
                "capacidadeMaxima", capacidadeMaxima,
                "status", statusTurma
        ), TurmaResponse.class);

        return new Catalogo(turma.id());
    }

    private AlunoResponse cadastrarAluno(String nome) throws Exception {
        return cadastrarAluno(nome, SituacaoAcademica.ATIVO);
    }

    private AlunoResponse cadastrarAluno(String nome, SituacaoAcademica situacao) throws Exception {
        String sufixo = UUID.randomUUID().toString().substring(0, 8);
        return postJson("/api/alunos", Map.of(
                "nome", nome,
                "email", "aluno." + sufixo + "@exemplo.com",
                "situacaoAcademica", situacao
        ), AlunoResponse.class);
    }

    private MatriculaResponse realizarMatricula(UUID alunoId, UUID turmaId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "alunoId", alunoId,
                                "turmaId", turmaId
                        ))))
                .andExpect(status().isCreated())
                .andReturn();
        return read(result, MatriculaResponse.class);
    }

    private MatriculaResponse confirmarMatricula(UUID matriculaId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/matriculas/{id}/confirmar", matriculaId))
                .andExpect(status().isOk())
                .andReturn();
        return read(result, MatriculaResponse.class);
    }

    private MatriculaResponse cancelarMatricula(UUID matriculaId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/matriculas/{id}/cancelar", matriculaId))
                .andExpect(status().isOk())
                .andReturn();
        return read(result, MatriculaResponse.class);
    }

    private TurmaResponse buscarTurma(UUID turmaId) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/turmas/{id}", turmaId))
                .andExpect(status().isOk())
                .andReturn();
        return read(result, TurmaResponse.class);
    }

    private <T> T postJson(String path, Object body, Class<T> type) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();
        return read(result, type);
    }

    private <T> T read(MvcResult result, Class<T> type) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), type);
    }

    private record Catalogo(UUID turmaId) {
    }
}
