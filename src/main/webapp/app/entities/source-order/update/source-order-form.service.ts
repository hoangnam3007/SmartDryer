import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISourceOrder, NewSourceOrder } from '../source-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISourceOrder for edit and NewSourceOrderFormGroupInput for create.
 */
type SourceOrderFormGroupInput = ISourceOrder | PartialWithRequiredKeyOf<NewSourceOrder>;

type SourceOrderFormDefaults = Pick<NewSourceOrder, 'id'>;

type SourceOrderFormGroupContent = {
  id: FormControl<ISourceOrder['id'] | NewSourceOrder['id']>;
  name: FormControl<ISourceOrder['name']>;
  description: FormControl<ISourceOrder['description']>;
};

export type SourceOrderFormGroup = FormGroup<SourceOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SourceOrderFormService {
  createSourceOrderFormGroup(sourceOrder: SourceOrderFormGroupInput = { id: null }): SourceOrderFormGroup {
    const sourceOrderRawValue = {
      ...this.getFormDefaults(),
      ...sourceOrder,
    };
    return new FormGroup<SourceOrderFormGroupContent>({
      id: new FormControl(
        { value: sourceOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(sourceOrderRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(sourceOrderRawValue.description),
    });
  }

  getSourceOrder(form: SourceOrderFormGroup): ISourceOrder | NewSourceOrder {
    return form.getRawValue() as ISourceOrder | NewSourceOrder;
  }

  resetForm(form: SourceOrderFormGroup, sourceOrder: SourceOrderFormGroupInput): void {
    const sourceOrderRawValue = { ...this.getFormDefaults(), ...sourceOrder };
    form.reset(
      {
        ...sourceOrderRawValue,
        id: { value: sourceOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SourceOrderFormDefaults {
    return {
      id: null,
    };
  }
}
