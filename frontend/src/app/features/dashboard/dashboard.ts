import { Component, OnInit, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { AlunoService } from '../aluno/aluno.service';
import { CursoService } from '../curso/curso.service';
import { MatriculaService } from '../matricula/matricula.service';
import { TurmaService } from '../turma/turma.service';

interface DashCard {
  title: string;
  value: string | number;
  link: string;
  linkLabel: string;
  icon: string;
}

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  private readonly alunoService = inject(AlunoService);
  private readonly cursoService = inject(CursoService);
  private readonly turmaService = inject(TurmaService);
  private readonly matriculaService = inject(MatriculaService);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly cards = signal<DashCard[]>([]);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);

    forkJoin({
      alunos: this.alunoService.listar(),
      cursos: this.cursoService.listar(),
      turmasAbertas: this.turmaService.listar({ status: 'ABERTA' }),
      matriculasPendentes: this.matriculaService.listar({ status: 'PENDENTE' }),
    }).subscribe({
      next: (data) => {
        this.cards.set([
          {
            title: 'Alunos',
            value: data.alunos.length,
            link: '/alunos',
            linkLabel: 'Ver alunos',
            icon: 'people',
          },
          {
            title: 'Cursos',
            value: data.cursos.length,
            link: '/cursos',
            linkLabel: 'Ver cursos',
            icon: 'school',
          },
          {
            title: 'Turmas abertas',
            value: data.turmasAbertas.length,
            link: '/turmas',
            linkLabel: 'Ver turmas',
            icon: 'groups',
          },
          {
            title: 'Matrículas pendentes',
            value: data.matriculasPendentes.length,
            link: '/matriculas',
            linkLabel: 'Ver matrículas',
            icon: 'assignment',
          },
        ]);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar o dashboard.');
        this.loading.set(false);
        this.cards.set([
          {
            title: 'Nova matrícula',
            value: '—',
            link: '/matriculas/nova',
            linkLabel: 'Realizar matrícula',
            icon: 'add_circle',
          },
        ]);
      },
    });
  }
}
