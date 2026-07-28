import { Routes } from '@angular/router';

export const alunoRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./aluno-list').then((m) => m.AlunoList),
  },
  {
    path: 'novo',
    loadComponent: () => import('./aluno-form').then((m) => m.AlunoForm),
  },
  {
    path: ':id',
    loadComponent: () => import('./aluno-detail').then((m) => m.AlunoDetail),
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./aluno-form').then((m) => m.AlunoForm),
  },
];
