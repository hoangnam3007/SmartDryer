import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ward.test-samples';

import { WardFormService } from './ward-form.service';

describe('Ward Form Service', () => {
  let service: WardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WardFormService);
  });

  describe('Service methods', () => {
    describe('createWardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            district: expect.any(Object),
          }),
        );
      });

      it('passing IWard should create a new form with FormGroup', () => {
        const formGroup = service.createWardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            district: expect.any(Object),
          }),
        );
      });
    });

    describe('getWard', () => {
      it('should return NewWard for default Ward initial value', () => {
        const formGroup = service.createWardFormGroup(sampleWithNewData);

        const ward = service.getWard(formGroup) as any;

        expect(ward).toMatchObject(sampleWithNewData);
      });

      it('should return NewWard for empty Ward initial value', () => {
        const formGroup = service.createWardFormGroup();

        const ward = service.getWard(formGroup) as any;

        expect(ward).toMatchObject({});
      });

      it('should return IWard', () => {
        const formGroup = service.createWardFormGroup(sampleWithRequiredData);

        const ward = service.getWard(formGroup) as any;

        expect(ward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWard should not enable id FormControl', () => {
        const formGroup = service.createWardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWard should disable id FormControl', () => {
        const formGroup = service.createWardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
