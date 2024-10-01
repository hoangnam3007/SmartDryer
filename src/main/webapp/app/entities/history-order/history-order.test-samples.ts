import dayjs from 'dayjs/esm';

import { IHistoryOrder, NewHistoryOrder } from './history-order.model';

export const sampleWithRequiredData: IHistoryOrder = {
  id: 29354,
  modifiedBy: 'about next sepal',
};

export const sampleWithPartialData: IHistoryOrder = {
  id: 4663,
  modifiedBy: 'commonly where gladly',
  statusNew: 'CANCEL',
};

export const sampleWithFullData: IHistoryOrder = {
  id: 9056,
  modifiedBy: 'aha verbalize',
  statusNew: 'DELAY',
  statusOld: 'HOLD',
  modifiedDate: dayjs('2024-09-19'),
};

export const sampleWithNewData: NewHistoryOrder = {
  modifiedBy: 'yippee that',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
