import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 7888,
  userName: 'management',
};

export const sampleWithPartialData: ICustomer = {
  id: 15303,
  userName: 'gah sturdy accidentally',
  createBy: 'fooey',
  email: 'Litzy25@gmail.com',
  source: 'bah',
  modifiedDate: dayjs('2024-09-19'),
};

export const sampleWithFullData: ICustomer = {
  id: 32301,
  userName: 'unlucky',
  code: 'natter promptly',
  displayName: 'incidentally hence',
  address: 'gosh astride modulo',
  createBy: 'straight duh around',
  mobile: 'or meh',
  email: 'Cristobal.Brakus59@hotmail.com',
  source: 'obnoxiously lovingly juvenile',
  note: 'outmanoeuvre tomorrow insolence',
  status: 'COMPLETED',
  createDate: dayjs('2024-09-19'),
  modifiedDate: dayjs('2024-09-19'),
};

export const sampleWithNewData: NewCustomer = {
  userName: 'snoop spouse',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
