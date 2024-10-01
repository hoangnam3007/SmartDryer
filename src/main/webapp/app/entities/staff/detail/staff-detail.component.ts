import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IStaff } from '../staff.model';

@Component({
  standalone: true,
  selector: 'jhi-staff-detail',
  templateUrl: './staff-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StaffDetailComponent {
  staff = input<IStaff | null>(null);

  previousState(): void {
    window.history.back();
  }
}
