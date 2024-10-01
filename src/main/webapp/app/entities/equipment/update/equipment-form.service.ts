import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEquipment, NewEquipment } from '../equipment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEquipment for edit and NewEquipmentFormGroupInput for create.
 */
type EquipmentFormGroupInput = IEquipment | PartialWithRequiredKeyOf<NewEquipment>;

type EquipmentFormDefaults = Pick<NewEquipment, 'id'>;

type EquipmentFormGroupContent = {
  id: FormControl<IEquipment['id'] | NewEquipment['id']>;
  equipmentCode: FormControl<IEquipment['equipmentCode']>;
  description: FormControl<IEquipment['description']>;
};

export type EquipmentFormGroup = FormGroup<EquipmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EquipmentFormService {
  createEquipmentFormGroup(equipment: EquipmentFormGroupInput = { id: null }): EquipmentFormGroup {
    const equipmentRawValue = {
      ...this.getFormDefaults(),
      ...equipment,
    };
    return new FormGroup<EquipmentFormGroupContent>({
      id: new FormControl(
        { value: equipmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      equipmentCode: new FormControl(equipmentRawValue.equipmentCode, {
        validators: [Validators.required],
      }),
      description: new FormControl(equipmentRawValue.description),
    });
  }

  getEquipment(form: EquipmentFormGroup): IEquipment | NewEquipment {
    return form.getRawValue() as IEquipment | NewEquipment;
  }

  resetForm(form: EquipmentFormGroup, equipment: EquipmentFormGroupInput): void {
    const equipmentRawValue = { ...this.getFormDefaults(), ...equipment };
    form.reset(
      {
        ...equipmentRawValue,
        id: { value: equipmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EquipmentFormDefaults {
    return {
      id: null,
    };
  }
}
