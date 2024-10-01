import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISourceOrder } from '../source-order.model';
import { SourceOrderService } from '../service/source-order.service';

@Component({
  standalone: true,
  templateUrl: './source-order-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SourceOrderDeleteDialogComponent {
  sourceOrder?: ISourceOrder;

  protected sourceOrderService = inject(SourceOrderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sourceOrderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
