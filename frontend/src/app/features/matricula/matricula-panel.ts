import { SlicePipe } from '@angular/common';
import { Component, OnInit, inject, input, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { StatusChip } from '../../shared/components/status-chip/status-chip';
import { Labels } from '../../shared/utils/labels';
import { AlunoService } from '../aluno/aluno.service';
import { TurmaService } from '../turma/turma.service';
import { MatriculaLabelsService, MatriculaView } from './matricula-labels.service';

@Component({
  selector: 'app-matricula-panel',
  imports: [
    SlicePipe,
    RouterLink,
    MatButtonModule,
    MatTableModule,
    MatProgressSpinnerModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './matricula-panel.html',
})
export class MatriculaPanel implements OnInit {
  private readonly alunoService = inject(AlunoService);
  private readonly turmaService = inject(TurmaService);
  private readonly labelsService = inject(MatriculaLabelsService);

  readonly alunoId = input<string | null>(null);
  readonly turmaId = input<string | null>(null);

  readonly Labels = Labels;
  readonly loading = signal(true);
  readonly items = signal<MatriculaView[]>([]);
  readonly displayedColumns = signal<string[]>([]);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const alunoId = this.alunoId();
    const turmaId = this.turmaId();
    this.loading.set(true);

    const request = alunoId
      ? this.alunoService.listarMatriculas(alunoId)
      : turmaId
        ? this.turmaService.listarMatriculas(turmaId)
        : null;

    if (!request) {
      this.loading.set(false);
      return;
    }

    this.displayedColumns.set(
      alunoId
        ? ['disciplina', 'turma', 'periodo', 'status', 'acoes']
        : ['aluno', 'status', 'acoes'],
    );

    forkJoin({
      matriculas: request,
      lookups: this.labelsService.loadLookups(),
    }).subscribe({
      next: ({ matriculas, lookups }) => {
        this.items.set(matriculas.map((m) => this.labelsService.toView(m, lookups)));
        this.loading.set(false);
      },
      error: () => {
        this.items.set([]);
        this.loading.set(false);
      },
    });
  }

  novaLink(): string[] {
    return ['/matriculas', 'nova'];
  }

  novaQuery(): Record<string, string> {
    const alunoId = this.alunoId();
    const turmaId = this.turmaId();
    if (alunoId) {
      return { alunoId };
    }
    if (turmaId) {
      return { turmaId };
    }
    return {};
  }
}
