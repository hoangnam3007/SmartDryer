import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../customer-equipment.test-samples';

import { CustomerEquipmentFormService } from './customer-equipment-form.service';

describe('CustomerEquipment Form Service', () => {
  let service: CustomerEquipmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerEquipmentFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerEquipmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerEquipmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantily: expect.any(Object),
            customer: expect.any(Object),
            equipment: expect.any(Object),
          }),
        );
      });

      it('passing ICustomerEquipment should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerEquipmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantily: expect.any(Object),
            customer: expect.any(Object),
            equipment: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomerEquipment', () => {
      it('should return NewCustomerEquipment for default CustomerEquipment initial value', () => {
        const formGroup = service.createCustomerEquipmentFormGroup(sampleWithNewData);

        const customerEquipment = service.getCustomerEquipment(formGroup) as any;

        expect(customerEquipment).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerEquipment for empty CustomerEquipment initial value', () => {
        const formGroup = service.createCustomerEquipmentFormGroup();

        const customerEquipment = service.getCustomerEquipment(formGroup) as any;

        expect(customerEquipment).toMatchObject({});
      });

      it('should return ICustomerEquipment', () => {
        const formGroup = service.createCustomerEquipmentFormGroup(sampleWithRequiredData);

        const customerEquipment = service.getCustomerEquipment(formGroup) as any;

        expect(customerEquipment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerEquipment should not enable id FormControl', () => {
        const formGroup = service.createCustomerEquipmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerEquipment should disable id FormControl', () => {
        const formGroup = service.createCustomerEquipmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
