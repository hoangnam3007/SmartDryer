import dayjs from 'dayjs/esm';

export interface ISendSMS {
  id: number;
  mobile?: string | null;
  content?: string | null;
  status?: number | null;
  createDate?: dayjs.Dayjs | null;
  sendedDate?: dayjs.Dayjs | null;
  type?: number | null;
}

export type NewSendSMS = Omit<ISendSMS, 'id'> & { id: null };
