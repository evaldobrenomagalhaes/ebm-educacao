import { SlicePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { StatusChip } from '../../shared/components/status-chip/status-chip';
import { Labels } from '../../shared/utils/labels';
import { MatriculaLabelsService, MatriculaLookups, MatriculaView } from './matricula-labels.service';
import { StatusMatricula } from './matricula.model';
import { MatriculaService } from './matricula.service';

@Component({
  selector: 'app-matricula-list',
  imports: [
    SlicePipe,
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatIconModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './matricula-list.html',
})
export class MatriculaList implements OnInit {
  private readonly matriculaService = inject(MatriculaService);
  private readonly labelsService = inject(MatriculaLabelsService);
  private readonly fb = inject(FormBuilder);
  readonly router = inject(Router);

  readonly Labels = Labels;
  readonly displayedColumns = ['id', 'aluno', 'turma', 'status', 'acoes'];
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly items = signal<MatriculaView[]>([]);
  readonly lookups = signal<MatriculaLookups | null>(null);
  readonly hadFilters = signal(false);

  readonly filterForm = this.fb.nonNullable.group({
    status: '' as StatusMatricula | '',
    alunoId: '',
    turmaId: '',
    periodoLetivoId: '',
    disciplinaId: '',
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    const filtros = this.filterForm.getRawValue();
    this.hadFilters.set(Object.values(filtros).some((v) => v !== ''));

    forkJoin({
      matriculas: this.matriculaService.listar(filtros),
      lookups: this.labelsService.loadLookups(),
    }).subscribe({
      next: ({ matriculas, lookups }) => {
        this.lookups.set(lookups);
        this.items.set(matriculas.map((m) => this.labelsService.toView(m, lookups)));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar as matrículas.');
        this.loading.set(false);
      },
    });
  }

  filtrar(): void {
    this.load();
  }

  limpar(): void {
    this.filterForm.reset({
      status: '',
      alunoId: '',
      turmaId: '',
      periodoLetivoId: '',
      disciplinaId: '',
    });
    this.load();
  }

  ver(id: string): void {
    void this.router.navigate(['/matriculas', id]);
  }

  turmaLabel(row: MatriculaView): string {
    return this.labelsService.turmaLabel(row);
  }
}
