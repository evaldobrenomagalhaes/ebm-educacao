import { Component, input } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-status-chip',
  imports: [MatChipsModule],
  template: `
    <mat-chip [highlighted]="highlighted()">{{ label() }}</mat-chip>
  `,
})
export class StatusChip {
  readonly label = input.required<string>();
  readonly highlighted = input(false);
}
