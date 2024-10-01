import dayjs, { Dayjs } from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ISale } from 'app/entities/sale/sale.model';
import { IStaff } from 'app/entities/staff/staff.model';
import { ISourceOrder } from 'app/entities/source-order/source-order.model';
import { IProvince } from 'app/entities/province/province.model';
import { IDistrict } from 'app/entities/district/district.model';
import { IWard } from 'app/entities/ward/ward.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IOrder {
  id: number;
  code?: string | null;
  createBy?: string | null;
  createDate?: dayjs.Dayjs | null;
  finishDate?: dayjs.Dayjs | null;
  status?: keyof typeof OrderStatus | null;
  amount?: number | null;
  saleNote?: string | null;
  techNote?: string | null;
  note?: string | null;
  materialSource?: number | null;
  cusName?: string | null;
  cusAddress?: string | null;
  cusMobile?: string | null;
  imageURL?: string | null;
  appointmentDate?: dayjs.Dayjs | null;
  activation?: number | null;
  assignBy?: string | null;
  customer?: Pick<ICustomer, 'id'> | null;
  sale?: Pick<ISale, 'id'> | null;
  staff?: Pick<IStaff, 'id'> | null;
  sourceOrder?: Pick<ISourceOrder, 'id'> | null;
  province?: Pick<IProvince, 'id'> | null;
  district?: Pick<IDistrict, 'id'> | null;
  ward?: Pick<IWard, 'id'> | null;
}

export class Order implements IOrder {
  constructor(
    public id: number,
    public code?: string | null,
    public createBy?: string | null,
    public createDate?: Dayjs | null,
    public finishDate?: Dayjs | null,
    public status?: keyof typeof OrderStatus | null,
    public amount?: number | null,
    public saleNote?: string | null,
    public techNote?: string | null,
    public note?: string | null,
    public materialSource?: number | null,
    public cusName?: string | null,
    public cusAddress?: string | null,
    public cusMobile?: string | null,
    public imageURL?: string | null,
    public appointmentDate?: Dayjs | null,
    public activation?: number | null,
    public assignBy?: string | null,
    public customer?: Pick<ICustomer, 'id'> | null,
    public sale?: Pick<ISale, 'id'> | null,
    public staff?: Pick<IStaff, 'id'> | null,
    public sourceOrder?: Pick<ISourceOrder, 'id'> | null,
    public province?: Pick<IProvince, 'id'> | null,
    public district?: Pick<IDistrict, 'id'> | null,
    public ward?: Pick<IWard, 'id'> | null,
  ) {}
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
