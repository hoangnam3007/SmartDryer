import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IEquipment } from '../equipment.model';

@Component({
  standalone: true,
  selector: 'jhi-equipment-detail',
  templateUrl: './equipment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EquipmentDetailComponent {
  equipment = input<IEquipment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
