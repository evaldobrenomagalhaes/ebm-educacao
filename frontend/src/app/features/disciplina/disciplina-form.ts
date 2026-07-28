import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { FeedbackService } from '../../core/services/feedback.service';
import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { applyFieldErrors, isNotFound } from '../../shared/utils/form-errors';
import { Curso } from '../curso/curso.model';
import { CursoService } from '../curso/curso.service';
import { DisciplinaService } from './disciplina.service';

@Component({
  selector: 'app-disciplina-form',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    EmptyState,
  ],
  templateUrl: './disciplina-form.html',
})
export class DisciplinaForm implements OnInit {
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly cursoService = inject(CursoService);
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly feedback = inject(FeedbackService);

  readonly loading = signal(false);
  readonly submitting = signal(false);
  readonly notFound = signal(false);
  readonly isEdit = signal(false);
  readonly cursos = signal<Curso[]>([]);

  private id: string | null = null;

  readonly form = this.fb.nonNullable.group({
    nome: ['', Validators.required],
    codigo: ['', Validators.required],
    cursoId: ['', Validators.required],
  });

  ngOnInit(): void {
    this.cursoService.listar().subscribe({
      next: (cursos) => this.cursos.set(cursos),
    });

    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id) {
      this.isEdit.set(true);
      this.load(this.id);
    }
  }

  load(id: string): void {
    this.loading.set(true);
    this.disciplinaService.buscarPorId(id).subscribe({
      next: (disciplina) => {
        this.form.patchValue({
          nome: disciplina.nome,
          codigo: disciplina.codigo,
          cursoId: disciplina.cursoId,
        });
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        if (isNotFound(err)) {
          this.notFound.set(true);
        }
      },
    });
  }

  salvar(): void {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    const body = this.form.getRawValue();
    const request$ = this.id
      ? this.disciplinaService.atualizar(this.id, body)
      : this.disciplinaService.cadastrar(body);

    request$.subscribe({
      next: (disciplina) => {
        this.submitting.set(false);
        this.feedback.success(
          this.id ? 'Disciplina atualizada com sucesso.' : 'Disciplina cadastrada com sucesso.',
        );
        void this.router.navigate(['/disciplinas', disciplina.id]);
      },
      error: (err) => {
        this.submitting.set(false);
        applyFieldErrors(err, (field, message) => {
          this.form.get(field)?.setErrors({ api: message });
        });
      },
    });
  }
}
