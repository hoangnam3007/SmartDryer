import dayjs from 'dayjs/esm';

export interface ISale {
  id: number;
  userName?: string | null;
  fullName?: string | null;
  mobile?: string | null;
  email?: string | null;
  note?: string | null;
  createDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
