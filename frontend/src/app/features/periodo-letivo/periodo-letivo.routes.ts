import { Routes } from '@angular/router';

export const periodoLetivoRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./periodo-letivo-list').then((m) => m.PeriodoLetivoList),
  },
  {
    path: 'novo',
    loadComponent: () => import('./periodo-letivo-form').then((m) => m.PeriodoLetivoForm),
  },
  {
    path: ':id',
    loadComponent: () => import('./periodo-letivo-detail').then((m) => m.PeriodoLetivoDetail),
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./periodo-letivo-form').then((m) => m.PeriodoLetivoForm),
  },
];
