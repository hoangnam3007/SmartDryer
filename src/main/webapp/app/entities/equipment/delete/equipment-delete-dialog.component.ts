import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';

@Component({
  standalone: true,
  templateUrl: './equipment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EquipmentDeleteDialogComponent {
  equipment?: IEquipment;

  protected equipmentService = inject(EquipmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.equipmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
