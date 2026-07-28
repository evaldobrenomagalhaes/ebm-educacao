import { Component, OnInit, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { FeedbackService } from '../../core/services/feedback.service';
import { EmptyState } from '../../shared/components/empty-state/empty-state';
import { ConfirmDialogService } from '../../shared/services/confirm-dialog.service';
import { isNotFound } from '../../shared/utils/form-errors';
import { CursoService } from '../curso/curso.service';
import { Disciplina } from './disciplina.model';
import { DisciplinaService } from './disciplina.service';

@Component({
  selector: 'app-disciplina-detail',
  imports: [RouterLink, MatButtonModule, MatIconModule, MatProgressSpinnerModule, EmptyState],
  templateUrl: './disciplina-detail.html',
})
export class DisciplinaDetail implements OnInit {
  private readonly disciplinaService = inject(DisciplinaService);
  private readonly cursoService = inject(CursoService);
  private readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  private readonly confirmDialog = inject(ConfirmDialogService);
  private readonly feedback = inject(FeedbackService);

  readonly loading = signal(true);
  readonly notFound = signal(false);
  readonly item = signal<Disciplina | null>(null);
  readonly cursoNome = signal<string>('');

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
    this.disciplinaService.buscarPorId(id).subscribe({
      next: (disciplina) => {
        this.item.set(disciplina);
        this.cursoNome.set(disciplina.cursoId);
        this.cursoService.buscarPorId(disciplina.cursoId).subscribe({
          next: (curso) => this.cursoNome.set(curso.nome),
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

  excluir(): void {
    const disciplina = this.item();
    if (!disciplina) {
      return;
    }

    this.confirmDialog
      .confirm({
        title: 'Excluir disciplina',
        message: `Deseja excluir a disciplina "${disciplina.nome}"? Esta ação não pode ser desfeita.`,
        confirmLabel: 'Excluir',
        danger: true,
      })
      .subscribe((ok) => {
        if (!ok) {
          return;
        }
        this.disciplinaService.excluir(disciplina.id).subscribe({
          next: () => {
            this.feedback.success('Disciplina excluída com sucesso.');
            void this.router.navigate(['/disciplinas']);
          },
        });
      });
  }
}
