import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISale, NewSale } from '../sale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISale for edit and NewSaleFormGroupInput for create.
 */
type SaleFormGroupInput = ISale | PartialWithRequiredKeyOf<NewSale>;

type SaleFormDefaults = Pick<NewSale, 'id'>;

type SaleFormGroupContent = {
  id: FormControl<ISale['id'] | NewSale['id']>;
  userName: FormControl<ISale['userName']>;
  fullName: FormControl<ISale['fullName']>;
  mobile: FormControl<ISale['mobile']>;
  email: FormControl<ISale['email']>;
  note: FormControl<ISale['note']>;
  createDate: FormControl<ISale['createDate']>;
  modifiedDate: FormControl<ISale['modifiedDate']>;
};

export type SaleFormGroup = FormGroup<SaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SaleFormService {
  createSaleFormGroup(sale: SaleFormGroupInput = { id: null }): SaleFormGroup {
    const saleRawValue = {
      ...this.getFormDefaults(),
      ...sale,
    };
    return new FormGroup<SaleFormGroupContent>({
      id: new FormControl(
        { value: saleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userName: new FormControl(saleRawValue.userName, {
        validators: [Validators.required],
      }),
      fullName: new FormControl(saleRawValue.fullName, {
        validators: [Validators.required],
      }),
      mobile: new FormControl(saleRawValue.mobile),
      email: new FormControl(saleRawValue.email),
      note: new FormControl(saleRawValue.note),
      createDate: new FormControl(saleRawValue.createDate),
      modifiedDate: new FormControl(saleRawValue.modifiedDate),
    });
  }

  getSale(form: SaleFormGroup): ISale | NewSale {
    return form.getRawValue() as ISale | NewSale;
  }

  resetForm(form: SaleFormGroup, sale: SaleFormGroupInput): void {
    const saleRawValue = { ...this.getFormDefaults(), ...sale };
    form.reset(
      {
        ...saleRawValue,
        id: { value: saleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SaleFormDefaults {
    return {
      id: null,
    };
  }
}
