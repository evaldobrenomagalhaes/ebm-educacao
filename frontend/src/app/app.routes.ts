import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/dashboard/dashboard').then((m) => m.Dashboard),
  },
  {
    path: 'cursos',
    loadChildren: () => import('./features/curso/curso.routes').then((m) => m.cursoRoutes),
  },
  { path: '**', redirectTo: '' },
];
