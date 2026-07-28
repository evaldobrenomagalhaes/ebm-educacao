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
import { PeriodoLetivo } from './periodo-letivo.model';
import { PeriodoLetivoService } from './periodo-letivo.service';

@Component({
  selector: 'app-periodo-letivo-detail',
  imports: [
    RouterLink,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    EmptyState,
    StatusChip,
  ],
  templateUrl: './periodo-letivo-detail.html',
})
export class PeriodoLetivoDetail implements OnInit {
  private readonly periodoService = inject(PeriodoLetivoService);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly confirmDialog = inject(ConfirmDialogService);
  private readonly feedback = inject(FeedbackService);

  readonly Labels = Labels;
  readonly loading = signal(true);
  readonly notFound = signal(false);
  readonly item = signal<PeriodoLetivo | null>(null);

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
    this.periodoService.buscarPorId(id).subscribe({
      next: (periodo) => {
        this.item.set(periodo);
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
    const periodo = this.item();
    if (!periodo) {
      return;
    }

    this.confirmDialog
      .confirm({
        title: 'Excluir período letivo',
        message: `Deseja excluir o período "${periodo.codigo}"? Esta ação não pode ser desfeita.`,
        confirmLabel: 'Excluir',
        danger: true,
      })
      .subscribe((ok) => {
        if (!ok) {
          return;
        }
        this.periodoService.excluir(periodo.id).subscribe({
          next: () => {
            this.feedback.success('Período letivo excluído com sucesso.');
            void this.router.navigate(['/periodos-letivos']);
          },
        });
      });
  }
}
