import { Routes } from '@angular/router';

export const disciplinaRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./disciplina-list').then((m) => m.DisciplinaList),
  },
  {
    path: 'novo',
    loadComponent: () => import('./disciplina-form').then((m) => m.DisciplinaForm),
  },
  {
    path: ':id',
    loadComponent: () => import('./disciplina-detail').then((m) => m.DisciplinaDetail),
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./disciplina-form').then((m) => m.DisciplinaForm),
  },
];
