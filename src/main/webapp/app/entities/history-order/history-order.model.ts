import dayjs from 'dayjs/esm';
import { IOrder } from 'app/entities/order/order.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IHistoryOrder {
  id: number;
  modifiedBy?: string | null;
  statusNew?: keyof typeof OrderStatus | null;
  statusOld?: keyof typeof OrderStatus | null;
  modifiedDate?: dayjs.Dayjs | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewHistoryOrder = Omit<IHistoryOrder, 'id'> & { id: null };
