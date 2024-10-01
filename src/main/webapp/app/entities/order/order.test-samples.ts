import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 31274,
  code: 'questionably toy',
};

export const sampleWithPartialData: IOrder = {
  id: 26600,
  code: 'fervently nestling huzzah',
  createDate: dayjs('2024-09-20'),
  finishDate: dayjs('2024-09-19'),
  status: 'DELAY',
  amount: 20551,
  saleNote: 'fortnight circa oh',
  techNote: 'sugary brightly',
  materialSource: 21830,
  cusName: 'outrage eek',
  cusAddress: 'step-aunt odometer excepting',
  cusMobile: 'through',
  imageURL: 'amidst split',
  assignBy: 'as',
};

export const sampleWithFullData: IOrder = {
  id: 10786,
  code: 'next since long',
  createBy: 'unaccountably',
  createDate: dayjs('2024-09-19'),
  finishDate: dayjs('2024-09-20'),
  status: 'ASSIGNED',
  amount: 3250,
  saleNote: 'pro incidentally champion',
  techNote: 'prime',
  note: 'despoil',
  materialSource: 13442,
  cusName: 'minus instead',
  cusAddress: 'ripple',
  cusMobile: 'strictly awkwardly',
  imageURL: 'ack aw in',
  appointmentDate: dayjs('2024-09-20'),
  activation: 10585,
  assignBy: 'under',
};

export const sampleWithNewData: NewOrder = {
  code: 'rightfully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
