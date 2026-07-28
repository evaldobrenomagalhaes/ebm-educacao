import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { apiUrl, toHttpParams } from '../../core/api/api.util';
import { Disciplina, DisciplinaFiltros, DisciplinaRequest } from './disciplina.model';

@Injectable({ providedIn: 'root' })
export class DisciplinaService {
  private readonly http = inject(HttpClient);
  private readonly base = apiUrl('/api/disciplinas');

  listar(filtros: DisciplinaFiltros = {}): Observable<Disciplina[]> {
    return this.http.get<Disciplina[]>(this.base, { params: toHttpParams(filtros) });
  }

  buscarPorId(id: string): Observable<Disciplina> {
    return this.http.get<Disciplina>(`${this.base}/${id}`);
  }

  cadastrar(body: DisciplinaRequest): Observable<Disciplina> {
    return this.http.post<Disciplina>(this.base, body);
  }

  atualizar(id: string, body: DisciplinaRequest): Observable<Disciplina> {
    return this.http.put<Disciplina>(`${this.base}/${id}`, body);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
