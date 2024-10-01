import dayjs from 'dayjs/esm';

import { ISendSMS, NewSendSMS } from './send-sms.model';

export const sampleWithRequiredData: ISendSMS = {
  id: 23667,
  mobile: 'unto photosynthesise',
};

export const sampleWithPartialData: ISendSMS = {
  id: 15864,
  mobile: 'sore grandiose neon',
  content: 'similar',
  status: 2689,
  createDate: dayjs('2024-09-20'),
  sendedDate: dayjs('2024-09-19'),
};

export const sampleWithFullData: ISendSMS = {
  id: 1728,
  mobile: 'alongside',
  content: 'highball',
  status: 16005,
  createDate: dayjs('2024-09-19'),
  sendedDate: dayjs('2024-09-20'),
  type: 8755,
};

export const sampleWithNewData: NewSendSMS = {
  mobile: 'cavernous phew critique',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
