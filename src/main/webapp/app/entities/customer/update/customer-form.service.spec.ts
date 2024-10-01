import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../customer.test-samples';

import { CustomerFormService } from './customer-form.service';

describe('Customer Form Service', () => {
  let service: CustomerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userName: expect.any(Object),
            code: expect.any(Object),
            displayName: expect.any(Object),
            address: expect.any(Object),
            createBy: expect.any(Object),
            mobile: expect.any(Object),
            email: expect.any(Object),
            source: expect.any(Object),
            note: expect.any(Object),
            status: expect.any(Object),
            createDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            province: expect.any(Object),
            district: expect.any(Object),
            ward: expect.any(Object),
          }),
        );
      });

      it('passing ICustomer should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userName: expect.any(Object),
            code: expect.any(Object),
            displayName: expect.any(Object),
            address: expect.any(Object),
            createBy: expect.any(Object),
            mobile: expect.any(Object),
            email: expect.any(Object),
            source: expect.any(Object),
            note: expect.any(Object),
            status: expect.any(Object),
            createDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            province: expect.any(Object),
            district: expect.any(Object),
            ward: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomer', () => {
      it('should return NewCustomer for default Customer initial value', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithNewData);

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomer for empty Customer initial value', () => {
        const formGroup = service.createCustomerFormGroup();

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject({});
      });

      it('should return ICustomer', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomer should not enable id FormControl', () => {
        const formGroup = service.createCustomerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomer should disable id FormControl', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
