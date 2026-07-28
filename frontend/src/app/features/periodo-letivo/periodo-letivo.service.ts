import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { apiUrl, toHttpParams } from '../../core/api/api.util';
import { PeriodoLetivo, PeriodoLetivoFiltros, PeriodoLetivoRequest } from './periodo-letivo.model';

@Injectable({ providedIn: 'root' })
export class PeriodoLetivoService {
  private readonly http = inject(HttpClient);
  private readonly base = apiUrl('/api/periodos-letivos');

  listar(filtros: PeriodoLetivoFiltros = {}): Observable<PeriodoLetivo[]> {
    return this.http.get<PeriodoLetivo[]>(this.base, { params: toHttpParams(filtros) });
  }

  buscarPorId(id: string): Observable<PeriodoLetivo> {
    return this.http.get<PeriodoLetivo>(`${this.base}/${id}`);
  }

  cadastrar(body: PeriodoLetivoRequest): Observable<PeriodoLetivo> {
    return this.http.post<PeriodoLetivo>(this.base, body);
  }

  atualizar(id: string, body: PeriodoLetivoRequest): Observable<PeriodoLetivo> {
    return this.http.put<PeriodoLetivo>(`${this.base}/${id}`, body);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
