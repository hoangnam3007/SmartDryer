import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cus-note.test-samples';

import { CusNoteFormService } from './cus-note-form.service';

describe('CusNote Form Service', () => {
  let service: CusNoteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CusNoteFormService);
  });

  describe('Service methods', () => {
    describe('createCusNoteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCusNoteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createBy: expect.any(Object),
            content: expect.any(Object),
            createDate: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing ICusNote should create a new form with FormGroup', () => {
        const formGroup = service.createCusNoteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createBy: expect.any(Object),
            content: expect.any(Object),
            createDate: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getCusNote', () => {
      it('should return NewCusNote for default CusNote initial value', () => {
        const formGroup = service.createCusNoteFormGroup(sampleWithNewData);

        const cusNote = service.getCusNote(formGroup) as any;

        expect(cusNote).toMatchObject(sampleWithNewData);
      });

      it('should return NewCusNote for empty CusNote initial value', () => {
        const formGroup = service.createCusNoteFormGroup();

        const cusNote = service.getCusNote(formGroup) as any;

        expect(cusNote).toMatchObject({});
      });

      it('should return ICusNote', () => {
        const formGroup = service.createCusNoteFormGroup(sampleWithRequiredData);

        const cusNote = service.getCusNote(formGroup) as any;

        expect(cusNote).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICusNote should not enable id FormControl', () => {
        const formGroup = service.createCusNoteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCusNote should disable id FormControl', () => {
        const formGroup = service.createCusNoteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
