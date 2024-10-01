import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IHistoryOrder } from '../history-order.model';

@Component({
  standalone: true,
  selector: 'jhi-history-order-detail',
  templateUrl: './history-order-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HistoryOrderDetailComponent {
  historyOrder = input<IHistoryOrder | null>(null);

  previousState(): void {
    window.history.back();
  }
}
