import dayjs from 'dayjs/esm';

import { IStaff, NewStaff } from './staff.model';

export const sampleWithRequiredData: IStaff = {
  id: 9955,
  userName: 'job',
  fullName: 'traipse',
};

export const sampleWithPartialData: IStaff = {
  id: 23000,
  userName: 'remote amnesty',
  fullName: 'sway concerning astride',
  mobile: 'implant',
  note: 'furthermore offensively annually',
  createDate: dayjs('2024-09-19'),
  modifiedDate: dayjs('2024-09-19'),
};

export const sampleWithFullData: IStaff = {
  id: 18735,
  userName: 'yuck',
  fullName: 'oof abnormally',
  mobile: 'swoon',
  email: 'Larue_Keeling@hotmail.com',
  note: 'yowza aggressive like',
  createDate: dayjs('2024-09-19'),
  modifiedDate: dayjs('2024-09-20'),
  isLead: 2787,
  imageURL: 'grimy qua examiner',
};

export const sampleWithNewData: NewStaff = {
  userName: 'terrible shrill',
  fullName: 'magnificent',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
