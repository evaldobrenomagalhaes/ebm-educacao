import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { Router, RouterLink } from '@angular/router';

import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { StatusChip } from '../../shared/components/status-chip/status-chip';
import { Labels } from '../../shared/utils/labels';
import { Disciplina } from '../disciplina/disciplina.model';
import { DisciplinaService } from '../disciplina/disciplina.service';
import { PeriodoLetivo } from '../periodo-letivo/periodo-letivo.model';
import { PeriodoLetivoService } from '../periodo-letivo/periodo-letivo.service';
import { StatusTurma, Turma } from './turma.model';
import { TurmaService } from './turma.service';

@Component({
  selector: 'app-turma-list',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatIconModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './turma-list.html',
})
export class TurmaList implements OnInit {
  private readonly turmaService = inject(TurmaService);
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly periodoService = inject(PeriodoLetivoService);
  private readonly fb = inject(FormBuilder);
  readonly router = inject(Router);

  readonly Labels = Labels;
  readonly displayedColumns = [
    'codigo',
    'disciplinaId',
    'periodoLetivoId',
    'status',
    'vagasDisponiveis',
    'acoes',
  ];
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly items = signal<Turma[]>([]);
  readonly disciplinas = signal<Disciplina[]>([]);
  readonly periodos = signal<PeriodoLetivo[]>([]);

  readonly filterForm = this.fb.nonNullable.group({
    codigo: '',
    status: '' as StatusTurma | '',
    disciplinaId: '',
    periodoLetivoId: '',
    comVagas: '' as '' | 'true' | 'false',
  });

  ngOnInit(): void {
    this.disciplinaService.listar().subscribe({
      next: (data) => this.disciplinas.set(data),
    });
    this.periodoService.listar().subscribe({
      next: (data) => this.periodos.set(data),
    });
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    const raw = this.filterForm.getRawValue();
    const filtros = {
      codigo: raw.codigo,
      status: raw.status,
      disciplinaId: raw.disciplinaId,
      periodoLetivoId: raw.periodoLetivoId,
      comVagas: raw.comVagas === '' ? ('' as const) : raw.comVagas === 'true',
    };
    this.turmaService.listar(filtros).subscribe({
      next: (data) => {
        this.items.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar as turmas.');
        this.loading.set(false);
      },
    });
  }

  filtrar(): void {
    this.load();
  }

  limpar(): void {
    this.filterForm.reset({
      codigo: '',
      status: '',
      disciplinaId: '',
      periodoLetivoId: '',
      comVagas: '',
    });
    this.load();
  }

  filtrarDisponiveis(): void {
    this.filterForm.patchValue({ status: 'ABERTA', comVagas: 'true' });
    this.load();
  }

  ver(id: string): void {
    void this.router.navigate(['/turmas', id]);
  }

  nomeDisciplina(id: string): string {
    return this.disciplinas().find((d) => d.id === id)?.nome ?? id;
  }

  codigoPeriodo(id: string): string {
    return this.periodos().find((p) => p.id === id)?.codigo ?? id;
  }
}
