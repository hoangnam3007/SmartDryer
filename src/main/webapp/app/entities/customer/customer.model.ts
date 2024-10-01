import dayjs from 'dayjs/esm';
import { IProvince } from 'app/entities/province/province.model';
import { IDistrict } from 'app/entities/district/district.model';
import { IWard } from 'app/entities/ward/ward.model';
import { CusStatus } from 'app/entities/enumerations/cus-status.model';

export interface ICustomer {
  id: number;
  userName?: string | null;
  code?: string | null;
  displayName?: string | null;
  address?: string | null;
  createBy?: string | null;
  mobile?: string | null;
  email?: string | null;
  source?: string | null;
  note?: string | null;
  status?: keyof typeof CusStatus | null;
  createDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  province?: Pick<IProvince, 'id'> | null;
  district?: Pick<IDistrict, 'id'> | null;
  ward?: Pick<IWard, 'id'> | null;
}

export class Customer implements ICustomer {
  constructor(
    public id: number,
    public userName?: string | null,
    public code?: string | null,
    public displayName?: string | null,
    public address?: string | null,
    public createBy?: string | null,
    public mobile?: string | null,
    public email?: string | null,
    public source?: string | null,
    public note?: string | null,
    public status?: keyof typeof CusStatus | null,
    public createDate?: dayjs.Dayjs | null,
    public modifiedDate?: dayjs.Dayjs | null,
    public province?: Pick<IProvince, 'id'> | null,
    public district?: Pick<IDistrict, 'id'> | null,
    public ward?: Pick<IWard, 'id'> | null,
  ) {}
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
