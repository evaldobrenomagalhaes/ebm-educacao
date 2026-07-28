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
import { SituacaoCurso } from './curso.model';
import { CursoService } from './curso.service';

@Component({
  selector: 'app-curso-form',
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
  templateUrl: './curso-form.html',
})
export class CursoForm implements OnInit {
  private readonly cursoService = inject(CursoService);
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly feedback = inject(FeedbackService);

  readonly loading = signal(false);
  readonly submitting = signal(false);
  readonly notFound = signal(false);
  readonly isEdit = signal(false);

  private id: string | null = null;

  readonly form = this.fb.nonNullable.group({
    nome: ['', Validators.required],
    situacao: ['ATIVO' as SituacaoCurso, Validators.required],
  });

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id) {
      this.isEdit.set(true);
      this.load(this.id);
    }
  }

  load(id: string): void {
    this.loading.set(true);
    this.cursoService.buscarPorId(id).subscribe({
      next: (curso) => {
        this.form.patchValue({
          nome: curso.nome,
          situacao: curso.situacao,
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
      ? this.cursoService.atualizar(this.id, body)
      : this.cursoService.cadastrar(body);

    request$.subscribe({
      next: (curso) => {
        this.submitting.set(false);
        this.feedback.success(this.id ? 'Curso atualizado com sucesso.' : 'Curso cadastrado com sucesso.');
        void this.router.navigate(['/cursos', curso.id]);
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
