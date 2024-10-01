import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id'>;

type OrderFormGroupContent = {
  id: FormControl<IOrder['id'] | NewOrder['id']>;
  code: FormControl<IOrder['code']>;
  createBy: FormControl<IOrder['createBy']>;
  createDate: FormControl<IOrder['createDate']>;
  finishDate: FormControl<IOrder['finishDate']>;
  status: FormControl<IOrder['status']>;
  amount: FormControl<IOrder['amount']>;
  saleNote: FormControl<IOrder['saleNote']>;
  techNote: FormControl<IOrder['techNote']>;
  note: FormControl<IOrder['note']>;
  materialSource: FormControl<IOrder['materialSource']>;
  cusName: FormControl<IOrder['cusName']>;
  cusAddress: FormControl<IOrder['cusAddress']>;
  cusMobile: FormControl<IOrder['cusMobile']>;
  imageURL: FormControl<IOrder['imageURL']>;
  appointmentDate: FormControl<IOrder['appointmentDate']>;
  activation: FormControl<IOrder['activation']>;
  assignBy: FormControl<IOrder['assignBy']>;
  customer: FormControl<IOrder['customer']>;
  sale: FormControl<IOrder['sale']>;
  staff: FormControl<IOrder['staff']>;
  sourceOrder: FormControl<IOrder['sourceOrder']>;
  province: FormControl<IOrder['province']>;
  district: FormControl<IOrder['district']>;
  ward: FormControl<IOrder['ward']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = {
      ...this.getFormDefaults(),
      ...order,
    };
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(orderRawValue.code, {
        validators: [Validators.required],
      }),
      createBy: new FormControl(orderRawValue.createBy),
      createDate: new FormControl(orderRawValue.createDate),
      finishDate: new FormControl(orderRawValue.finishDate),
      status: new FormControl(orderRawValue.status),
      amount: new FormControl(orderRawValue.amount),
      saleNote: new FormControl(orderRawValue.saleNote),
      techNote: new FormControl(orderRawValue.techNote),
      note: new FormControl(orderRawValue.note),
      materialSource: new FormControl(orderRawValue.materialSource),
      cusName: new FormControl(orderRawValue.cusName),
      cusAddress: new FormControl(orderRawValue.cusAddress),
      cusMobile: new FormControl(orderRawValue.cusMobile),
      imageURL: new FormControl(orderRawValue.imageURL),
      appointmentDate: new FormControl(orderRawValue.appointmentDate),
      activation: new FormControl(orderRawValue.activation),
      assignBy: new FormControl(orderRawValue.assignBy),
      customer: new FormControl(orderRawValue.customer),
      sale: new FormControl(orderRawValue.sale),
      staff: new FormControl(orderRawValue.staff),
      sourceOrder: new FormControl(orderRawValue.sourceOrder),
      province: new FormControl(orderRawValue.province),
      district: new FormControl(orderRawValue.district),
      ward: new FormControl(orderRawValue.ward),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return form.getRawValue() as IOrder | NewOrder;
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = { ...this.getFormDefaults(), ...order };
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    return {
      id: null,
    };
  }
}
