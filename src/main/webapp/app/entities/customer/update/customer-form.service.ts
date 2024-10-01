import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  userName: FormControl<ICustomer['userName']>;
  code: FormControl<ICustomer['code']>;
  displayName: FormControl<ICustomer['displayName']>;
  address: FormControl<ICustomer['address']>;
  createBy: FormControl<ICustomer['createBy']>;
  mobile: FormControl<ICustomer['mobile']>;
  email: FormControl<ICustomer['email']>;
  source: FormControl<ICustomer['source']>;
  note: FormControl<ICustomer['note']>;
  status: FormControl<ICustomer['status']>;
  createDate: FormControl<ICustomer['createDate']>;
  modifiedDate: FormControl<ICustomer['modifiedDate']>;
  province: FormControl<ICustomer['province']>;
  district: FormControl<ICustomer['district']>;
  ward: FormControl<ICustomer['ward']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userName: new FormControl(customerRawValue.userName, {
        validators: [Validators.required],
      }),
      code: new FormControl(customerRawValue.code),
      displayName: new FormControl(customerRawValue.displayName),
      address: new FormControl(customerRawValue.address),
      createBy: new FormControl(customerRawValue.createBy),
      mobile: new FormControl(customerRawValue.mobile),
      email: new FormControl(customerRawValue.email),
      source: new FormControl(customerRawValue.source),
      note: new FormControl(customerRawValue.note),
      status: new FormControl(customerRawValue.status),
      createDate: new FormControl(customerRawValue.createDate),
      modifiedDate: new FormControl(customerRawValue.modifiedDate),
      province: new FormControl(customerRawValue.province),
      district: new FormControl(customerRawValue.district),
      ward: new FormControl(customerRawValue.ward),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
    };
  }
}
