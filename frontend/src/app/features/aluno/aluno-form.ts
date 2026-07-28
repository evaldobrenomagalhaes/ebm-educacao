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
import { SituacaoAcademica } from './aluno.model';
import { AlunoService } from './aluno.service';

@Component({
  selector: 'app-aluno-form',
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
  templateUrl: './aluno-form.html',
})
export class AlunoForm implements OnInit {
  private readonly alunoService = inject(AlunoService);
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
    email: ['', [Validators.required, Validators.email]],
    situacaoAcademica: ['ATIVO' as SituacaoAcademica, Validators.required],
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
    this.alunoService.buscarPorId(id).subscribe({
      next: (aluno) => {
        this.form.patchValue({
          nome: aluno.nome,
          email: aluno.email,
          situacaoAcademica: aluno.situacaoAcademica,
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
      ? this.alunoService.atualizar(this.id, body)
      : this.alunoService.cadastrar(body);

    request$.subscribe({
      next: (aluno) => {
        this.submitting.set(false);
        this.feedback.success(this.id ? 'Aluno atualizado com sucesso.' : 'Aluno cadastrado com sucesso.');
        void this.router.navigate(['/alunos', aluno.id]);
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
