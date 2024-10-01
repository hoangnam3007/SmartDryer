import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICustomerEquipment } from '../customer-equipment.model';
import { CustomerEquipmentService } from '../service/customer-equipment.service';

@Component({
  standalone: true,
  templateUrl: './customer-equipment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CustomerEquipmentDeleteDialogComponent {
  customerEquipment?: ICustomerEquipment;

  protected customerEquipmentService = inject(CustomerEquipmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerEquipmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
