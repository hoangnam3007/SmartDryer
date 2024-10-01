import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomerEquipment, NewCustomerEquipment } from '../customer-equipment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerEquipment for edit and NewCustomerEquipmentFormGroupInput for create.
 */
type CustomerEquipmentFormGroupInput = ICustomerEquipment | PartialWithRequiredKeyOf<NewCustomerEquipment>;

type CustomerEquipmentFormDefaults = Pick<NewCustomerEquipment, 'id'>;

type CustomerEquipmentFormGroupContent = {
  id: FormControl<ICustomerEquipment['id'] | NewCustomerEquipment['id']>;
  quantily: FormControl<ICustomerEquipment['quantily']>;
  customer: FormControl<ICustomerEquipment['customer']>;
  equipment: FormControl<ICustomerEquipment['equipment']>;
};

export type CustomerEquipmentFormGroup = FormGroup<CustomerEquipmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerEquipmentFormService {
  createCustomerEquipmentFormGroup(customerEquipment: CustomerEquipmentFormGroupInput = { id: null }): CustomerEquipmentFormGroup {
    const customerEquipmentRawValue = {
      ...this.getFormDefaults(),
      ...customerEquipment,
    };
    return new FormGroup<CustomerEquipmentFormGroupContent>({
      id: new FormControl(
        { value: customerEquipmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantily: new FormControl(customerEquipmentRawValue.quantily),
      customer: new FormControl(customerEquipmentRawValue.customer),
      equipment: new FormControl(customerEquipmentRawValue.equipment),
    });
  }

  getCustomerEquipment(form: CustomerEquipmentFormGroup): ICustomerEquipment | NewCustomerEquipment {
    return form.getRawValue() as ICustomerEquipment | NewCustomerEquipment;
  }

  resetForm(form: CustomerEquipmentFormGroup, customerEquipment: CustomerEquipmentFormGroupInput): void {
    const customerEquipmentRawValue = { ...this.getFormDefaults(), ...customerEquipment };
    form.reset(
      {
        ...customerEquipmentRawValue,
        id: { value: customerEquipmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerEquipmentFormDefaults {
    return {
      id: null,
    };
  }
}
