import { Component, OnInit, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

import { FeedbackService } from '../../core/services/feedback.service';
import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { StatusChip } from '../../shared/components/status-chip/status-chip';
import { ConfirmDialogService } from '../../shared/services/confirm-dialog.service';
import { isNotFound } from '../../shared/utils/form-errors';
import { Labels } from '../../shared/utils/labels';
import { DisciplinaService } from '../disciplina/disciplina.service';
import { PeriodoLetivoService } from '../periodo-letivo/periodo-letivo.service';
import { AlunoService } from '../aluno/aluno.service';
import { TurmaService } from '../turma/turma.service';
import { Matricula } from './matricula.model';
import { MatriculaService } from './matricula.service';

@Component({
  selector: 'app-matricula-detail',
  imports: [
    RouterLink,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './matricula-detail.html',
})
export class MatriculaDetail implements OnInit {
  private readonly matriculaService = inject(MatriculaService);
  private readonly alunoService = inject(AlunoService);
  private readonly turmaService = inject(TurmaService);
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly periodoService = inject(PeriodoLetivoService);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly confirmDialog = inject(ConfirmDialogService);
  private readonly feedback = inject(FeedbackService);

  readonly Labels = Labels;
  readonly loading = signal(true);
  readonly actionBusy = signal(false);
  readonly notFound = signal(false);
  readonly item = signal<Matricula | null>(null);
  readonly alunoNome = signal('');
  readonly turmaLabel = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.notFound.set(true);
      this.loading.set(false);
      return;
    }

    this.loading.set(true);
    this.notFound.set(false);
    this.matriculaService.buscarPorId(id).subscribe({
      next: (matricula) => {
        this.item.set(matricula);
        this.alunoNome.set(matricula.alunoId);
        this.turmaLabel.set(matricula.turmaId);
        this.resolveLabels(matricula);
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

  private resolveLabels(matricula: Matricula): void {
    this.alunoService.buscarPorId(matricula.alunoId).subscribe({
      next: (a) => this.alunoNome.set(a.nome),
      error: () => undefined,
    });

    this.turmaService
      .buscarPorId(matricula.turmaId)
      .pipe(
        switchMap((turma) =>
          forkJoin({
            turma: of(turma),
            disciplina: this.disciplinaService.buscarPorId(turma.disciplinaId).pipe(
              catchError(() => of(null)),
            ),
            periodo: this.periodoService.buscarPorId(turma.periodoLetivoId).pipe(
              catchError(() => of(null)),
            ),
          }),
        ),
        catchError(() => of(null)),
      )
      .subscribe((result) => {
        if (!result) {
          return;
        }
        const parts = [
          result.turma.codigo,
          result.disciplina?.nome,
          result.periodo?.codigo,
        ].filter(Boolean);
        this.turmaLabel.set(parts.join(' · '));
      });
  }

  confirmar(): void {
    const matricula = this.item();
    if (!matricula || this.actionBusy()) {
      return;
    }
    this.actionBusy.set(true);
    this.matriculaService.confirmar(matricula.id).subscribe({
      next: (updated) => {
        this.item.set(updated);
        this.actionBusy.set(false);
        this.feedback.success('Matrícula confirmada.');
      },
      error: () => this.actionBusy.set(false),
    });
  }

  cancelar(): void {
    const matricula = this.item();
    if (!matricula || this.actionBusy()) {
      return;
    }

    this.confirmDialog
      .confirm({
        title: 'Cancelar matrícula',
        message: 'Deseja cancelar esta matrícula?',
        confirmLabel: 'Cancelar matrícula',
        danger: true,
      })
      .subscribe((ok) => {
        if (!ok) {
          return;
        }
        this.actionBusy.set(true);
        this.matriculaService.cancelar(matricula.id).subscribe({
          next: (updated) => {
            this.item.set(updated);
            this.actionBusy.set(false);
            this.feedback.success('Matrícula cancelada.');
          },
          error: () => this.actionBusy.set(false),
        });
      });
  }
}
