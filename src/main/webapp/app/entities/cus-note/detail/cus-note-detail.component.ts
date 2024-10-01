import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICusNote } from '../cus-note.model';

@Component({
  standalone: true,
  selector: 'jhi-cus-note-detail',
  templateUrl: './cus-note-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CusNoteDetailComponent {
  cusNote = input<ICusNote | null>(null);

  previousState(): void {
    window.history.back();
  }
}
