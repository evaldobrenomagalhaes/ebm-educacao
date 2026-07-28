import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { map, shareReplay, take } from 'rxjs/operators';

interface NavItem {
  label: string;
  path: string;
  icon: string;
}

@Component({
  selector: 'app-shell',
  imports: [
    AsyncPipe,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './shell.html',
  styleUrl: './shell.scss',
})
export class Shell {
  private readonly breakpointObserver = inject(BreakpointObserver);

  readonly isHandset$ = this.breakpointObserver.observe([Breakpoints.Handset, '(max-width: 959.98px)']).pipe(
    map((result) => result.matches),
    shareReplay({ bufferSize: 1, refCount: true }),
  );

  readonly navItems: NavItem[] = [
    { label: 'Início', path: '/', icon: 'home' },
    { label: 'Cursos', path: '/cursos', icon: 'school' },
    { label: 'Disciplinas', path: '/disciplinas', icon: 'menu_book' },
    { label: 'Períodos Letivos', path: '/periodos-letivos', icon: 'date_range' },
    { label: 'Alunos', path: '/alunos', icon: 'people' },
    { label: 'Turmas', path: '/turmas', icon: 'groups' },
    { label: 'Matrículas', path: '/matriculas', icon: 'assignment' },
  ];

  closeIfHandset(drawer: MatSidenav): void {
    this.isHandset$.pipe(take(1)).subscribe((isHandset) => {
      if (isHandset) {
        drawer.close();
      }
    });
  }
}
