import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISendSMS, NewSendSMS } from '../send-sms.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISendSMS for edit and NewSendSMSFormGroupInput for create.
 */
type SendSMSFormGroupInput = ISendSMS | PartialWithRequiredKeyOf<NewSendSMS>;

type SendSMSFormDefaults = Pick<NewSendSMS, 'id'>;

type SendSMSFormGroupContent = {
  id: FormControl<ISendSMS['id'] | NewSendSMS['id']>;
  mobile: FormControl<ISendSMS['mobile']>;
  content: FormControl<ISendSMS['content']>;
  status: FormControl<ISendSMS['status']>;
  createDate: FormControl<ISendSMS['createDate']>;
  sendedDate: FormControl<ISendSMS['sendedDate']>;
  type: FormControl<ISendSMS['type']>;
};

export type SendSMSFormGroup = FormGroup<SendSMSFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SendSMSFormService {
  createSendSMSFormGroup(sendSMS: SendSMSFormGroupInput = { id: null }): SendSMSFormGroup {
    const sendSMSRawValue = {
      ...this.getFormDefaults(),
      ...sendSMS,
    };
    return new FormGroup<SendSMSFormGroupContent>({
      id: new FormControl(
        { value: sendSMSRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mobile: new FormControl(sendSMSRawValue.mobile, {
        validators: [Validators.required],
      }),
      content: new FormControl(sendSMSRawValue.content),
      status: new FormControl(sendSMSRawValue.status),
      createDate: new FormControl(sendSMSRawValue.createDate),
      sendedDate: new FormControl(sendSMSRawValue.sendedDate),
      type: new FormControl(sendSMSRawValue.type),
    });
  }

  getSendSMS(form: SendSMSFormGroup): ISendSMS | NewSendSMS {
    return form.getRawValue() as ISendSMS | NewSendSMS;
  }

  resetForm(form: SendSMSFormGroup, sendSMS: SendSMSFormGroupInput): void {
    const sendSMSRawValue = { ...this.getFormDefaults(), ...sendSMS };
    form.reset(
      {
        ...sendSMSRawValue,
        id: { value: sendSMSRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SendSMSFormDefaults {
    return {
      id: null,
    };
  }
}
