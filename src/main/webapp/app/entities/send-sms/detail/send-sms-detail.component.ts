import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISendSMS } from '../send-sms.model';

@Component({
  standalone: true,
  selector: 'jhi-send-sms-detail',
  templateUrl: './send-sms-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SendSMSDetailComponent {
  sendSMS = input<ISendSMS | null>(null);

  previousState(): void {
    window.history.back();
  }
}
