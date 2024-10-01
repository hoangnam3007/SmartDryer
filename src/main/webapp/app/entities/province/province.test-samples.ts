import { IProvince, NewProvince } from './province.model';

export const sampleWithRequiredData: IProvince = {
  id: 8752,
  name: 'whether',
};

export const sampleWithPartialData: IProvince = {
  id: 27441,
  code: 'when barring',
  name: 'nocturnal than',
};

export const sampleWithFullData: IProvince = {
  id: 16947,
  code: 'for winding',
  name: 'aw pace',
};

export const sampleWithNewData: NewProvince = {
  name: 'deliberately pattern personal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
