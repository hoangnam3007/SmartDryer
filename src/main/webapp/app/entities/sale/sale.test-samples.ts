import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 8307,
  userName: 'finally',
  fullName: 'cutover metabolize',
};

export const sampleWithPartialData: ISale = {
  id: 1054,
  userName: 'whereas grumpy unlike',
  fullName: 'voluntarily',
  mobile: 'hmph helpfully underneath',
};

export const sampleWithFullData: ISale = {
  id: 10308,
  userName: 'polite weighty lest',
  fullName: 'yam brr single',
  mobile: 'psst chat',
  email: 'Letha_Boyer@hotmail.com',
  note: 'ugh',
  createDate: dayjs('2024-09-20'),
  modifiedDate: dayjs('2024-09-19'),
};

export const sampleWithNewData: NewSale = {
  userName: 'er at',
  fullName: 'gah dreamily',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
