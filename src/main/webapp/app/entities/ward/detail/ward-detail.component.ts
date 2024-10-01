import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IWard } from '../ward.model';

@Component({
  standalone: true,
  selector: 'jhi-ward-detail',
  templateUrl: './ward-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WardDetailComponent {
  ward = input<IWard | null>(null);

  previousState(): void {
    window.history.back();
  }
}
