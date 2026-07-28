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
import { Curso } from '../curso/curso.model';
import { CursoService } from '../curso/curso.service';
import { Disciplina } from './disciplina.model';
import { DisciplinaService } from './disciplina.service';

@Component({
  selector: 'app-disciplina-list',
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
  ],
  templateUrl: './disciplina-list.html',
})
export class DisciplinaList implements OnInit {
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly cursoService = inject(CursoService);
  private readonly fb = inject(FormBuilder);
  readonly router = inject(Router);

  readonly displayedColumns = ['nome', 'codigo', 'cursoId', 'acoes'];
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly items = signal<Disciplina[]>([]);
  readonly cursos = signal<Curso[]>([]);

  readonly filterForm = this.fb.nonNullable.group({
    nome: '',
    codigo: '',
    cursoId: '',
  });

  ngOnInit(): void {
    this.cursoService.listar().subscribe({
      next: (cursos) => this.cursos.set(cursos),
    });
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.disciplinaService.listar(this.filterForm.getRawValue()).subscribe({
      next: (data) => {
        this.items.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar as disciplinas.');
        this.loading.set(false);
      },
    });
  }

  filtrar(): void {
    this.load();
  }

  limpar(): void {
    this.filterForm.reset({ nome: '', codigo: '', cursoId: '' });
    this.load();
  }

  ver(id: string): void {
    void this.router.navigate(['/disciplinas', id]);
  }

  nomeCurso(cursoId: string): string {
    return this.cursos().find((c) => c.id === cursoId)?.nome ?? cursoId;
  }
}
