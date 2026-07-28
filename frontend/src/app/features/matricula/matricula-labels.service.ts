import { Injectable, inject } from '@angular/core';
import { Observable, forkJoin, map, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AlunoService } from '../aluno/aluno.service';
import { DisciplinaService } from '../disciplina/disciplina.service';
import { PeriodoLetivoService } from '../periodo-letivo/periodo-letivo.service';
import { TurmaService } from '../turma/turma.service';
import { Matricula } from './matricula.model';

export interface MatriculaView {
  id: string;
  status: Matricula['status'];
  alunoId: string;
  turmaId: string;
  alunoNome: string;
  turmaCodigo: string;
  disciplinaNome: string;
  periodoCodigo: string;
}

export interface MatriculaLookups {
  alunos: { id: string; nome: string }[];
  turmas: { id: string; codigo: string; disciplinaId: string; periodoLetivoId: string }[];
  disciplinas: { id: string; nome: string; codigo: string }[];
  periodos: { id: string; codigo: string }[];
}

@Injectable({ providedIn: 'root' })
export class MatriculaLabelsService {
  private readonly alunoService = inject(AlunoService);
  private readonly turmaService = inject(TurmaService);
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly periodoService = inject(PeriodoLetivoService);

  loadLookups(): Observable<MatriculaLookups> {
    return forkJoin({
      alunos: this.alunoService.listar().pipe(catchError(() => of([]))),
      turmas: this.turmaService.listar().pipe(catchError(() => of([]))),
      disciplinas: this.disciplinaService.listar().pipe(catchError(() => of([]))),
      periodos: this.periodoService.listar().pipe(catchError(() => of([]))),
    });
  }

  enrich(matriculas: Matricula[], lookups?: MatriculaLookups): Observable<MatriculaView[]> {
    const source = lookups ? of(lookups) : this.loadLookups();
    return source.pipe(map((data) => matriculas.map((m) => this.toView(m, data))));
  }

  toView(matricula: Matricula, lookups: MatriculaLookups): MatriculaView {
    const aluno = lookups.alunos.find((a) => a.id === matricula.alunoId);
    const turma = lookups.turmas.find((t) => t.id === matricula.turmaId);
    const disciplina = turma
      ? lookups.disciplinas.find((d) => d.id === turma.disciplinaId)
      : undefined;
    const periodo = turma
      ? lookups.periodos.find((p) => p.id === turma.periodoLetivoId)
      : undefined;

    return {
      id: matricula.id,
      status: matricula.status,
      alunoId: matricula.alunoId,
      turmaId: matricula.turmaId,
      alunoNome: aluno?.nome ?? matricula.alunoId,
      turmaCodigo: turma?.codigo ?? matricula.turmaId,
      disciplinaNome: disciplina?.nome ?? '—',
      periodoCodigo: periodo?.codigo ?? '—',
    };
  }

  turmaLabel(view: Pick<MatriculaView, 'turmaCodigo' | 'disciplinaNome'>): string {
    if (view.disciplinaNome && view.disciplinaNome !== '—') {
      return `${view.turmaCodigo} · ${view.disciplinaNome}`;
    }
    return view.turmaCodigo;
  }
}
