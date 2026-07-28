import { Routes } from '@angular/router';

export const cursoRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./curso-list').then((m) => m.CursoList),
  },
  {
    path: 'novo',
    loadComponent: () => import('./curso-form').then((m) => m.CursoForm),
  },
  {
    path: ':id',
    loadComponent: () => import('./curso-detail').then((m) => m.CursoDetail),
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./curso-form').then((m) => m.CursoForm),
  },
];
