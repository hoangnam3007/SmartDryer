import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../send-sms.test-samples';

import { SendSMSFormService } from './send-sms-form.service';

describe('SendSMS Form Service', () => {
  let service: SendSMSFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendSMSFormService);
  });

  describe('Service methods', () => {
    describe('createSendSMSFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSendSMSFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mobile: expect.any(Object),
            content: expect.any(Object),
            status: expect.any(Object),
            createDate: expect.any(Object),
            sendedDate: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });

      it('passing ISendSMS should create a new form with FormGroup', () => {
        const formGroup = service.createSendSMSFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mobile: expect.any(Object),
            content: expect.any(Object),
            status: expect.any(Object),
            createDate: expect.any(Object),
            sendedDate: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });
    });

    describe('getSendSMS', () => {
      it('should return NewSendSMS for default SendSMS initial value', () => {
        const formGroup = service.createSendSMSFormGroup(sampleWithNewData);

        const sendSMS = service.getSendSMS(formGroup) as any;

        expect(sendSMS).toMatchObject(sampleWithNewData);
      });

      it('should return NewSendSMS for empty SendSMS initial value', () => {
        const formGroup = service.createSendSMSFormGroup();

        const sendSMS = service.getSendSMS(formGroup) as any;

        expect(sendSMS).toMatchObject({});
      });

      it('should return ISendSMS', () => {
        const formGroup = service.createSendSMSFormGroup(sampleWithRequiredData);

        const sendSMS = service.getSendSMS(formGroup) as any;

        expect(sendSMS).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISendSMS should not enable id FormControl', () => {
        const formGroup = service.createSendSMSFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSendSMS should disable id FormControl', () => {
        const formGroup = service.createSendSMSFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
