import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISendSMS } from '../send-sms.model';
import { SendSMSService } from '../service/send-sms.service';
import { SendSMSFormGroup, SendSMSFormService } from './send-sms-form.service';

@Component({
  standalone: true,
  selector: 'jhi-send-sms-update',
  templateUrl: './send-sms-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SendSMSUpdateComponent implements OnInit {
  isSaving = false;
  sendSMS: ISendSMS | null = null;

  protected sendSMSService = inject(SendSMSService);
  protected sendSMSFormService = inject(SendSMSFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SendSMSFormGroup = this.sendSMSFormService.createSendSMSFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sendSMS }) => {
      this.sendSMS = sendSMS;
      if (sendSMS) {
        this.updateForm(sendSMS);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sendSMS = this.sendSMSFormService.getSendSMS(this.editForm);
    if (sendSMS.id !== null) {
      this.subscribeToSaveResponse(this.sendSMSService.update(sendSMS));
    } else {
      this.subscribeToSaveResponse(this.sendSMSService.create(sendSMS));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISendSMS>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sendSMS: ISendSMS): void {
    this.sendSMS = sendSMS;
    this.sendSMSFormService.resetForm(this.editForm, sendSMS);
  }
}
