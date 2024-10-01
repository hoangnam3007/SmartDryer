import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../source-order.test-samples';

import { SourceOrderFormService } from './source-order-form.service';

describe('SourceOrder Form Service', () => {
  let service: SourceOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SourceOrderFormService);
  });

  describe('Service methods', () => {
    describe('createSourceOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSourceOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ISourceOrder should create a new form with FormGroup', () => {
        const formGroup = service.createSourceOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getSourceOrder', () => {
      it('should return NewSourceOrder for default SourceOrder initial value', () => {
        const formGroup = service.createSourceOrderFormGroup(sampleWithNewData);

        const sourceOrder = service.getSourceOrder(formGroup) as any;

        expect(sourceOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewSourceOrder for empty SourceOrder initial value', () => {
        const formGroup = service.createSourceOrderFormGroup();

        const sourceOrder = service.getSourceOrder(formGroup) as any;

        expect(sourceOrder).toMatchObject({});
      });

      it('should return ISourceOrder', () => {
        const formGroup = service.createSourceOrderFormGroup(sampleWithRequiredData);

        const sourceOrder = service.getSourceOrder(formGroup) as any;

        expect(sourceOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISourceOrder should not enable id FormControl', () => {
        const formGroup = service.createSourceOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSourceOrder should disable id FormControl', () => {
        const formGroup = service.createSourceOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
