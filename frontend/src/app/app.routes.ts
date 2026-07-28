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
  {
    path: 'disciplinas',
    loadChildren: () => import('./features/disciplina/disciplina.routes').then((m) => m.disciplinaRoutes),
  },
  {
    path: 'periodos-letivos',
    loadChildren: () =>
      import('./features/periodo-letivo/periodo-letivo.routes').then((m) => m.periodoLetivoRoutes),
  },
  { path: '**', redirectTo: '' },
];
