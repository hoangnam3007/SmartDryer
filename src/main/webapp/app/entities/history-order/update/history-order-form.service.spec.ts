import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../history-order.test-samples';

import { HistoryOrderFormService } from './history-order-form.service';

describe('HistoryOrder Form Service', () => {
  let service: HistoryOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoryOrderFormService);
  });

  describe('Service methods', () => {
    describe('createHistoryOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHistoryOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            modifiedBy: expect.any(Object),
            statusNew: expect.any(Object),
            statusOld: expect.any(Object),
            modifiedDate: expect.any(Object),
            order: expect.any(Object),
          }),
        );
      });

      it('passing IHistoryOrder should create a new form with FormGroup', () => {
        const formGroup = service.createHistoryOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            modifiedBy: expect.any(Object),
            statusNew: expect.any(Object),
            statusOld: expect.any(Object),
            modifiedDate: expect.any(Object),
            order: expect.any(Object),
          }),
        );
      });
    });

    describe('getHistoryOrder', () => {
      it('should return NewHistoryOrder for default HistoryOrder initial value', () => {
        const formGroup = service.createHistoryOrderFormGroup(sampleWithNewData);

        const historyOrder = service.getHistoryOrder(formGroup) as any;

        expect(historyOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewHistoryOrder for empty HistoryOrder initial value', () => {
        const formGroup = service.createHistoryOrderFormGroup();

        const historyOrder = service.getHistoryOrder(formGroup) as any;

        expect(historyOrder).toMatchObject({});
      });

      it('should return IHistoryOrder', () => {
        const formGroup = service.createHistoryOrderFormGroup(sampleWithRequiredData);

        const historyOrder = service.getHistoryOrder(formGroup) as any;

        expect(historyOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHistoryOrder should not enable id FormControl', () => {
        const formGroup = service.createHistoryOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHistoryOrder should disable id FormControl', () => {
        const formGroup = service.createHistoryOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
