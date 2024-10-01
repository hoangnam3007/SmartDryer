import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICusNote } from '../cus-note.model';
import { CusNoteService } from '../service/cus-note.service';

@Component({
  standalone: true,
  templateUrl: './cus-note-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CusNoteDeleteDialogComponent {
  cusNote?: ICusNote;

  protected cusNoteService = inject(CusNoteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cusNoteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
