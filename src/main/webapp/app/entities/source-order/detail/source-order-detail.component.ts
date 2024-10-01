import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISourceOrder } from '../source-order.model';

@Component({
  standalone: true,
  selector: 'jhi-source-order-detail',
  templateUrl: './source-order-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SourceOrderDetailComponent {
  sourceOrder = input<ISourceOrder | null>(null);

  previousState(): void {
    window.history.back();
  }
}
