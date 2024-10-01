import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 21636,
  name: 'truly hedge where',
};

export const sampleWithPartialData: IDistrict = {
  id: 11374,
  code: 'until',
  name: 'whose octopus',
};

export const sampleWithFullData: IDistrict = {
  id: 12629,
  code: 'marry across',
  name: 'boohoo',
};

export const sampleWithNewData: NewDistrict = {
  name: 'postulate whether consequently',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
