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
import { Curso, SituacaoCurso } from './curso.model';
import { CursoService } from './curso.service';

@Component({
  selector: 'app-curso-list',
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
  templateUrl: './curso-list.html',
})
export class CursoList implements OnInit {
  private readonly cursoService = inject(CursoService);
  private readonly fb = inject(FormBuilder);
  readonly router = inject(Router);

  readonly Labels = Labels;
  readonly displayedColumns = ['nome', 'situacao', 'acoes'];
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly items = signal<Curso[]>([]);

  readonly filterForm = this.fb.nonNullable.group({
    nome: '',
    situacao: '' as SituacaoCurso | '',
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.cursoService.listar(this.filterForm.getRawValue()).subscribe({
      next: (data) => {
        this.items.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar os cursos.');
        this.loading.set(false);
      },
    });
  }

  filtrar(): void {
    this.load();
  }

  limpar(): void {
    this.filterForm.reset({ nome: '', situacao: '' });
    this.load();
  }

  ver(id: string): void {
    void this.router.navigate(['/cursos', id]);
  }
}
