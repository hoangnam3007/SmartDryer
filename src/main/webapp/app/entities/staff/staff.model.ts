import dayjs from 'dayjs/esm';

export interface IStaff {
  id: number;
  userName?: string | null;
  fullName?: string | null;
  mobile?: string | null;
  email?: string | null;
  note?: string | null;
  createDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  isLead?: number | null;
  imageURL?: string | null;
  staffLead?: Pick<IStaff, 'id'> | null;
}

export type NewStaff = Omit<IStaff, 'id'> & { id: null };
