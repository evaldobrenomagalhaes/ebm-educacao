import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { FeedbackService } from '../../core/services/feedback.service';
import { applyFieldErrors } from '../../shared/utils/form-errors';
import { Aluno } from '../aluno/aluno.model';
import { AlunoService } from '../aluno/aluno.service';
import { Turma } from '../turma/turma.model';
import { TurmaService } from '../turma/turma.service';
import { MatriculaService } from './matricula.service';

@Component({
  selector: 'app-matricula-form',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './matricula-form.html',
})
export class MatriculaForm implements OnInit {
  private readonly matriculaService = inject(MatriculaService);
  private readonly alunoService = inject(AlunoService);
  private readonly turmaService = inject(TurmaService);
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly feedback = inject(FeedbackService);

  readonly submitting = signal(false);
  readonly loadingOptions = signal(true);
  readonly alunos = signal<Aluno[]>([]);
  readonly turmas = signal<Turma[]>([]);

  readonly form = this.fb.nonNullable.group({
    alunoId: ['', Validators.required],
    turmaId: ['', Validators.required],
  });

  ngOnInit(): void {
    const alunoId = this.route.snapshot.queryParamMap.get('alunoId') ?? '';
    const turmaId = this.route.snapshot.queryParamMap.get('turmaId') ?? '';

    this.alunoService.listar().subscribe({
      next: (alunos) => {
        this.alunos.set(alunos);
        if (alunoId) {
          this.form.patchValue({ alunoId });
        }
      },
    });

    this.turmaService.listarDisponiveis().subscribe({
      next: (turmas) => {
        this.turmas.set(turmas);
        if (turmaId) {
          const exists = turmas.some((t) => t.id === turmaId);
          if (exists) {
            this.form.patchValue({ turmaId });
          } else {
            this.turmaService.buscarPorId(turmaId).subscribe({
              next: (turma) => {
                this.turmas.set([turma, ...turmas]);
                this.form.patchValue({ turmaId });
              },
            });
          }
        }
        this.loadingOptions.set(false);
      },
      error: () => this.loadingOptions.set(false),
    });
  }

  salvar(): void {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.matriculaService.realizar(this.form.getRawValue()).subscribe({
      next: (matricula) => {
        this.feedback.success('Matrícula realizada com sucesso.');
        void this.router.navigate(['/matriculas', matricula.id]);
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
