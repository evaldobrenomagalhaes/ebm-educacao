import { Component, input, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-empty-state',
  imports: [MatButtonModule],
  template: `
    <div class="state-block" role="status">
      <p>{{ message() }}</p>
      @if (actionLabel()) {
        <button mat-stroked-button type="button" color="primary" (click)="action.emit()">
          {{ actionLabel() }}
        </button>
      }
    </div>
  `,
})
export class EmptyState {
  readonly message = input.required<string>();
  readonly actionLabel = input<string | null>(null);
  readonly action = output<void>();
}
