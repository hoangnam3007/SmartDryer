import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDistrict } from '../district.model';

@Component({
  standalone: true,
  selector: 'jhi-district-detail',
  templateUrl: './district-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DistrictDetailComponent {
  district = input<IDistrict | null>(null);

  previousState(): void {
    window.history.back();
  }
}
