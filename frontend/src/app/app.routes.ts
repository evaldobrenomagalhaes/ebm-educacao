import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home-placeholder').then((m) => m.HomePlaceholder),
  },
  { path: '**', redirectTo: '' },
];
