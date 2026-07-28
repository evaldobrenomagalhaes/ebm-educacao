import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { apiUrl, toHttpParams } from '../../core/api/api.util';
import { Curso, CursoFiltros, CursoRequest } from './curso.model';

@Injectable({ providedIn: 'root' })
export class CursoService {
  private readonly http = inject(HttpClient);
  private readonly base = apiUrl('/api/cursos');

  listar(filtros: CursoFiltros = {}): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.base, { params: toHttpParams(filtros) });
  }

  buscarPorId(id: string): Observable<Curso> {
    return this.http.get<Curso>(`${this.base}/${id}`);
  }

  cadastrar(body: CursoRequest): Observable<Curso> {
    return this.http.post<Curso>(this.base, body);
  }

  atualizar(id: string, body: CursoRequest): Observable<Curso> {
    return this.http.put<Curso>(`${this.base}/${id}`, body);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
