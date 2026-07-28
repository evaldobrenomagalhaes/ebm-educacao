import { Component, OnInit, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { FeedbackService } from '../../core/services/feedback.service';
import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { StatusChip } from '../../shared/components/status-chip/status-chip';
import { ConfirmDialogService } from '../../shared/services/confirm-dialog.service';
import { isNotFound } from '../../shared/utils/form-errors';
import { Labels } from '../../shared/utils/labels';
import { DisciplinaService } from '../disciplina/disciplina.service';
import { PeriodoLetivoService } from '../periodo-letivo/periodo-letivo.service';
import { Turma } from './turma.model';
import { TurmaService } from './turma.service';

@Component({
  selector: 'app-turma-detail',
  imports: [
    RouterLink,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './turma-detail.html',
})
export class TurmaDetail implements OnInit {
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
  readonly item = signal<Turma | null>(null);
  readonly disciplinaNome = signal('');
  readonly periodoCodigo = signal('');

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
    this.turmaService.buscarPorId(id).subscribe({
      next: (turma) => {
        this.item.set(turma);
        this.disciplinaNome.set(turma.disciplinaId);
        this.periodoCodigo.set(turma.periodoLetivoId);
        this.disciplinaService.buscarPorId(turma.disciplinaId).subscribe({
          next: (d) => this.disciplinaNome.set(d.nome),
          error: () => undefined,
        });
        this.periodoService.buscarPorId(turma.periodoLetivoId).subscribe({
          next: (p) => this.periodoCodigo.set(p.codigo),
          error: () => undefined,
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

  abrir(): void {
    const turma = this.item();
    if (!turma || this.actionBusy()) {
      return;
    }
    this.actionBusy.set(true);
    this.turmaService.abrir(turma.id).subscribe({
      next: (updated) => {
        this.item.set(updated);
        this.actionBusy.set(false);
        this.feedback.success('Turma aberta com sucesso.');
      },
      error: () => this.actionBusy.set(false),
    });
  }

  fechar(): void {
    const turma = this.item();
    if (!turma || this.actionBusy()) {
      return;
    }
    this.actionBusy.set(true);
    this.turmaService.fechar(turma.id).subscribe({
      next: (updated) => {
        this.item.set(updated);
        this.actionBusy.set(false);
        this.feedback.success('Turma fechada com sucesso.');
      },
      error: () => this.actionBusy.set(false),
    });
  }

  excluir(): void {
    const turma = this.item();
    if (!turma) {
      return;
    }

    this.confirmDialog
      .confirm({
        title: 'Excluir turma',
        message: `Deseja excluir a turma "${turma.codigo}"? Esta ação não pode ser desfeita.`,
        confirmLabel: 'Excluir',
        danger: true,
      })
      .subscribe((ok) => {
        if (!ok) {
          return;
        }
        this.turmaService.excluir(turma.id).subscribe({
          next: () => {
            this.feedback.success('Turma excluída com sucesso.');
            void this.router.navigate(['/turmas']);
          },
        });
      });
  }
}
