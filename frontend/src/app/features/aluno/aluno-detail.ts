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
import { Aluno } from './aluno.model';
import { AlunoService } from './aluno.service';

@Component({
  selector: 'app-aluno-detail',
  imports: [
    RouterLink,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './aluno-detail.html',
})
export class AlunoDetail implements OnInit {
  private readonly alunoService = inject(AlunoService);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly confirmDialog = inject(ConfirmDialogService);
  private readonly feedback = inject(FeedbackService);

  readonly Labels = Labels;
  readonly loading = signal(true);
  readonly notFound = signal(false);
  readonly item = signal<Aluno | null>(null);

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
    this.alunoService.buscarPorId(id).subscribe({
      next: (aluno) => {
        this.item.set(aluno);
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

  excluir(): void {
    const aluno = this.item();
    if (!aluno) {
      return;
    }

    this.confirmDialog
      .confirm({
        title: 'Excluir aluno',
        message: `Deseja excluir o aluno "${aluno.nome}"? Esta ação não pode ser desfeita.`,
        confirmLabel: 'Excluir',
        danger: true,
      })
      .subscribe((ok) => {
        if (!ok) {
          return;
        }
        this.alunoService.excluir(aluno.id).subscribe({
          next: () => {
            this.feedback.success('Aluno excluído com sucesso.');
            void this.router.navigate(['/alunos']);
          },
        });
      });
  }
}
