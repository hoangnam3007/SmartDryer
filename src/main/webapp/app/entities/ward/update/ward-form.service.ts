import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWard, NewWard } from '../ward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWard for edit and NewWardFormGroupInput for create.
 */
type WardFormGroupInput = IWard | PartialWithRequiredKeyOf<NewWard>;

type WardFormDefaults = Pick<NewWard, 'id'>;

type WardFormGroupContent = {
  id: FormControl<IWard['id'] | NewWard['id']>;
  code: FormControl<IWard['code']>;
  name: FormControl<IWard['name']>;
  district: FormControl<IWard['district']>;
};

export type WardFormGroup = FormGroup<WardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WardFormService {
  createWardFormGroup(ward: WardFormGroupInput = { id: null }): WardFormGroup {
    const wardRawValue = {
      ...this.getFormDefaults(),
      ...ward,
    };
    return new FormGroup<WardFormGroupContent>({
      id: new FormControl(
        { value: wardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(wardRawValue.code),
      name: new FormControl(wardRawValue.name, {
        validators: [Validators.required],
      }),
      district: new FormControl(wardRawValue.district),
    });
  }

  getWard(form: WardFormGroup): IWard | NewWard {
    return form.getRawValue() as IWard | NewWard;
  }

  resetForm(form: WardFormGroup, ward: WardFormGroupInput): void {
    const wardRawValue = { ...this.getFormDefaults(), ...ward };
    form.reset(
      {
        ...wardRawValue,
        id: { value: wardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WardFormDefaults {
    return {
      id: null,
    };
  }
}
