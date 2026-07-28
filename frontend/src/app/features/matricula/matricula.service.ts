import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { apiUrl, toHttpParams } from '../../core/api/api.util';
import { Matricula, MatriculaFiltros, RealizarMatriculaRequest } from './matricula.model';

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  private readonly http = inject(HttpClient);
  private readonly base = apiUrl('/api/matriculas');

  listar(filtros: MatriculaFiltros = {}): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(this.base, { params: toHttpParams(filtros) });
  }

  buscarPorId(id: string): Observable<Matricula> {
    return this.http.get<Matricula>(`${this.base}/${id}`);
  }

  realizar(body: RealizarMatriculaRequest): Observable<Matricula> {
    return this.http.post<Matricula>(this.base, body);
  }

  confirmar(id: string): Observable<Matricula> {
    return this.http.post<Matricula>(`${this.base}/${id}/confirmar`, {});
  }

  cancelar(id: string): Observable<Matricula> {
    return this.http.post<Matricula>(`${this.base}/${id}/cancelar`, {});
  }
}
