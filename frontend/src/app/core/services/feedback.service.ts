import { Injectable, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class FeedbackService {
  private readonly snackBar = inject(MatSnackBar);

  success(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 3500,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-success'],
    });
  }

  error(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 6000,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-error'],
    });
  }

  info(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 4000,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
    });
  }
}
