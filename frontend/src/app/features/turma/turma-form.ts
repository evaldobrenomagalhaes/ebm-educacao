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
import { Disciplina } from '../disciplina/disciplina.model';
import { DisciplinaService } from '../disciplina/disciplina.service';
import { PeriodoLetivo } from '../periodo-letivo/periodo-letivo.model';
import { PeriodoLetivoService } from '../periodo-letivo/periodo-letivo.service';
import { StatusTurma } from './turma.model';
import { TurmaService } from './turma.service';

@Component({
  selector: 'app-turma-form',
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
  templateUrl: './turma-form.html',
})
export class TurmaForm implements OnInit {
  private readonly turmaService = inject(TurmaService);
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly periodoService = inject(PeriodoLetivoService);
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly feedback = inject(FeedbackService);

  readonly loading = signal(false);
  readonly submitting = signal(false);
  readonly notFound = signal(false);
  readonly isEdit = signal(false);
  readonly disciplinas = signal<Disciplina[]>([]);
  readonly periodos = signal<PeriodoLetivo[]>([]);

  private id: string | null = null;

  readonly form = this.fb.nonNullable.group({
    codigo: ['', Validators.required],
    disciplinaId: ['', Validators.required],
    periodoLetivoId: ['', Validators.required],
    capacidadeMaxima: [30, [Validators.required, Validators.min(1)]],
    status: ['ABERTA' as StatusTurma, Validators.required],
  });

  ngOnInit(): void {
    this.disciplinaService.listar().subscribe({
      next: (data) => this.disciplinas.set(data),
    });
    this.periodoService.listar().subscribe({
      next: (data) => this.periodos.set(data),
    });

    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id) {
      this.isEdit.set(true);
      this.form.controls.status.clearValidators();
      this.form.controls.status.updateValueAndValidity();
      this.load(this.id);
    }
  }

  load(id: string): void {
    this.loading.set(true);
    this.turmaService.buscarPorId(id).subscribe({
      next: (turma) => {
        this.form.patchValue({
          codigo: turma.codigo,
          disciplinaId: turma.disciplinaId,
          periodoLetivoId: turma.periodoLetivoId,
          capacidadeMaxima: turma.capacidadeMaxima,
          status: turma.status,
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
    const raw = this.form.getRawValue();

    const request$ = this.id
      ? this.turmaService.atualizar(this.id, {
          codigo: raw.codigo,
          disciplinaId: raw.disciplinaId,
          periodoLetivoId: raw.periodoLetivoId,
          capacidadeMaxima: raw.capacidadeMaxima,
        })
      : this.turmaService.cadastrar({
          codigo: raw.codigo,
          disciplinaId: raw.disciplinaId,
          periodoLetivoId: raw.periodoLetivoId,
          capacidadeMaxima: raw.capacidadeMaxima,
          status: raw.status,
        });

    request$.subscribe({
      next: (turma) => {
        this.submitting.set(false);
        this.feedback.success(this.id ? 'Turma atualizada com sucesso.' : 'Turma cadastrada com sucesso.');
        void this.router.navigate(['/turmas', turma.id]);
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
