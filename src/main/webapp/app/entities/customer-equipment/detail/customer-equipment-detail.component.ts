import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICustomerEquipment } from '../customer-equipment.model';

@Component({
  standalone: true,
  selector: 'jhi-customer-equipment-detail',
  templateUrl: './customer-equipment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomerEquipmentDetailComponent {
  customerEquipment = input<ICustomerEquipment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
