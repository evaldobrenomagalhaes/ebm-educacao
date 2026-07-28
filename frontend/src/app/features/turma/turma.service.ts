import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { apiUrl, toHttpParams } from '../../core/api/api.util';
import { Matricula, MatriculaFiltros } from '../matricula/matricula.model';
import {
  AtualizarTurmaRequest,
  CadastrarTurmaRequest,
  Turma,
  TurmaFiltros,
  TurmasDisponiveisFiltros,
} from './turma.model';

@Injectable({ providedIn: 'root' })
export class TurmaService {
  private readonly http = inject(HttpClient);
  private readonly base = apiUrl('/api/turmas');

  listar(filtros: TurmaFiltros = {}): Observable<Turma[]> {
    return this.http.get<Turma[]>(this.base, { params: toHttpParams(filtros) });
  }

  listarDisponiveis(filtros: TurmasDisponiveisFiltros = {}): Observable<Turma[]> {
    return this.http.get<Turma[]>(`${this.base}/disponiveis`, { params: toHttpParams(filtros) });
  }

  buscarPorId(id: string): Observable<Turma> {
    return this.http.get<Turma>(`${this.base}/${id}`);
  }

  cadastrar(body: CadastrarTurmaRequest): Observable<Turma> {
    return this.http.post<Turma>(this.base, body);
  }

  atualizar(id: string, body: AtualizarTurmaRequest): Observable<Turma> {
    return this.http.put<Turma>(`${this.base}/${id}`, body);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  abrir(id: string): Observable<Turma> {
    return this.http.post<Turma>(`${this.base}/${id}/abrir`, {});
  }

  fechar(id: string): Observable<Turma> {
    return this.http.post<Turma>(`${this.base}/${id}/fechar`, {});
  }

  listarMatriculas(turmaId: string, filtros: MatriculaFiltros = {}): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.base}/${turmaId}/matriculas`, {
      params: toHttpParams(filtros),
    });
  }
}
