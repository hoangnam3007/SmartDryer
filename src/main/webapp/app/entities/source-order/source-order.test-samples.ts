import { ISourceOrder, NewSourceOrder } from './source-order.model';

export const sampleWithRequiredData: ISourceOrder = {
  id: 25367,
  name: 'erode torn',
};

export const sampleWithPartialData: ISourceOrder = {
  id: 5488,
  name: 'commonly hence',
  description: 'inasmuch',
};

export const sampleWithFullData: ISourceOrder = {
  id: 16362,
  name: 'resist lawyer impress',
  description: 'furthermore',
};

export const sampleWithNewData: NewSourceOrder = {
  name: 'loosely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
