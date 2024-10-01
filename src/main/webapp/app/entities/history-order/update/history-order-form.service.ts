import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IHistoryOrder, NewHistoryOrder } from '../history-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistoryOrder for edit and NewHistoryOrderFormGroupInput for create.
 */
type HistoryOrderFormGroupInput = IHistoryOrder | PartialWithRequiredKeyOf<NewHistoryOrder>;

type HistoryOrderFormDefaults = Pick<NewHistoryOrder, 'id'>;

type HistoryOrderFormGroupContent = {
  id: FormControl<IHistoryOrder['id'] | NewHistoryOrder['id']>;
  modifiedBy: FormControl<IHistoryOrder['modifiedBy']>;
  statusNew: FormControl<IHistoryOrder['statusNew']>;
  statusOld: FormControl<IHistoryOrder['statusOld']>;
  modifiedDate: FormControl<IHistoryOrder['modifiedDate']>;
  order: FormControl<IHistoryOrder['order']>;
};

export type HistoryOrderFormGroup = FormGroup<HistoryOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HistoryOrderFormService {
  createHistoryOrderFormGroup(historyOrder: HistoryOrderFormGroupInput = { id: null }): HistoryOrderFormGroup {
    const historyOrderRawValue = {
      ...this.getFormDefaults(),
      ...historyOrder,
    };
    return new FormGroup<HistoryOrderFormGroupContent>({
      id: new FormControl(
        { value: historyOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      modifiedBy: new FormControl(historyOrderRawValue.modifiedBy, {
        validators: [Validators.required],
      }),
      statusNew: new FormControl(historyOrderRawValue.statusNew),
      statusOld: new FormControl(historyOrderRawValue.statusOld),
      modifiedDate: new FormControl(historyOrderRawValue.modifiedDate),
      order: new FormControl(historyOrderRawValue.order),
    });
  }

  getHistoryOrder(form: HistoryOrderFormGroup): IHistoryOrder | NewHistoryOrder {
    return form.getRawValue() as IHistoryOrder | NewHistoryOrder;
  }

  resetForm(form: HistoryOrderFormGroup, historyOrder: HistoryOrderFormGroupInput): void {
    const historyOrderRawValue = { ...this.getFormDefaults(), ...historyOrder };
    form.reset(
      {
        ...historyOrderRawValue,
        id: { value: historyOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HistoryOrderFormDefaults {
    return {
      id: null,
    };
  }
}
