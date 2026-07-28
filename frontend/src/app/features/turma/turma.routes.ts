import { Routes } from '@angular/router';

export const turmaRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./turma-list').then((m) => m.TurmaList),
  },
  {
    path: 'novo',
    loadComponent: () => import('./turma-form').then((m) => m.TurmaForm),
  },
  {
    path: ':id',
    loadComponent: () => import('./turma-detail').then((m) => m.TurmaDetail),
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./turma-form').then((m) => m.TurmaForm),
  },
];
