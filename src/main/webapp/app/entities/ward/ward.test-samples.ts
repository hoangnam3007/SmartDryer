import { IWard, NewWard } from './ward.model';

export const sampleWithRequiredData: IWard = {
  id: 9525,
  name: 'gosh',
};

export const sampleWithPartialData: IWard = {
  id: 23568,
  name: 'actually adventurously',
};

export const sampleWithFullData: IWard = {
  id: 12363,
  code: 'wilted upward',
  name: 'godparent forenenst',
};

export const sampleWithNewData: NewWard = {
  name: 'when questioningly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
