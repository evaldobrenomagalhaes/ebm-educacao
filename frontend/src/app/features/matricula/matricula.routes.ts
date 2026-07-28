import { Routes } from '@angular/router';

export const matriculaRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./matricula-list').then((m) => m.MatriculaList),
  },
  {
    path: 'nova',
    loadComponent: () => import('./matricula-form').then((m) => m.MatriculaForm),
  },
  {
    path: ':id',
    loadComponent: () => import('./matricula-detail').then((m) => m.MatriculaDetail),
  },
];
