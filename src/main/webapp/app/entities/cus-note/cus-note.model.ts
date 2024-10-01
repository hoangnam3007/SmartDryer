import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface ICusNote {
  id: number;
  createBy?: string | null;
  content?: string | null;
  createDate?: dayjs.Dayjs | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewCusNote = Omit<ICusNote, 'id'> & { id: null };
