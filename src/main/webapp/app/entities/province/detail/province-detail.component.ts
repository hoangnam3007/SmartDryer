import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProvince } from '../province.model';

@Component({
  standalone: true,
  selector: 'jhi-province-detail',
  templateUrl: './province-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProvinceDetailComponent {
  province = input<IProvince | null>(null);

  previousState(): void {
    window.history.back();
  }
}
