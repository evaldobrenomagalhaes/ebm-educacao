import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { apiUrl, toHttpParams } from '../../core/api/api.util';
import { Matricula, MatriculaFiltros } from '../matricula/matricula.model';
import { Aluno, AlunoFiltros, AlunoRequest } from './aluno.model';

@Injectable({ providedIn: 'root' })
export class AlunoService {
  private readonly http = inject(HttpClient);
  private readonly base = apiUrl('/api/alunos');

  listar(filtros: AlunoFiltros = {}): Observable<Aluno[]> {
    return this.http.get<Aluno[]>(this.base, { params: toHttpParams(filtros) });
  }

  buscarPorId(id: string): Observable<Aluno> {
    return this.http.get<Aluno>(`${this.base}/${id}`);
  }

  cadastrar(body: AlunoRequest): Observable<Aluno> {
    return this.http.post<Aluno>(this.base, body);
  }

  atualizar(id: string, body: AlunoRequest): Observable<Aluno> {
    return this.http.put<Aluno>(`${this.base}/${id}`, body);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  listarMatriculas(alunoId: string, filtros: MatriculaFiltros = {}): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.base}/${alunoId}/matriculas`, {
      params: toHttpParams(filtros),
    });
  }
}
