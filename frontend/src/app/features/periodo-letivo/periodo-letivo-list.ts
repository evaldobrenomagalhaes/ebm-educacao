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
import { PeriodoLetivo, SituacaoPeriodoLetivo } from './periodo-letivo.model';
import { PeriodoLetivoService } from './periodo-letivo.service';

@Component({
  selector: 'app-periodo-letivo-list',
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
  templateUrl: './periodo-letivo-list.html',
})
export class PeriodoLetivoList implements OnInit {
  private readonly periodoService = inject(PeriodoLetivoService);
  private readonly fb = inject(FormBuilder);
  readonly router = inject(Router);

  readonly Labels = Labels;
  readonly displayedColumns = ['codigo', 'dataInicio', 'dataTermino', 'situacao', 'acoes'];
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly items = signal<PeriodoLetivo[]>([]);

  readonly filterForm = this.fb.nonNullable.group({
    codigo: '',
    situacao: '' as SituacaoPeriodoLetivo | '',
    vigenteEm: '',
    dataInicioDe: '',
    dataInicioAte: '',
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.periodoService.listar(this.filterForm.getRawValue()).subscribe({
      next: (data) => {
        this.items.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar os períodos letivos.');
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
      situacao: '',
      vigenteEm: '',
      dataInicioDe: '',
      dataInicioAte: '',
    });
    this.load();
  }

  ver(id: string): void {
    void this.router.navigate(['/periodos-letivos', id]);
  }
}
