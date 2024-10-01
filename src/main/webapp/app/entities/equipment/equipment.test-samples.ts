import { IEquipment, NewEquipment } from './equipment.model';

export const sampleWithRequiredData: IEquipment = {
  id: 13234,
  equipmentCode: 'cease ick',
};

export const sampleWithPartialData: IEquipment = {
  id: 15928,
  equipmentCode: 'fooey',
  description: 'mmm',
};

export const sampleWithFullData: IEquipment = {
  id: 19680,
  equipmentCode: 'although',
  description: 'greedily breakdown save',
};

export const sampleWithNewData: NewEquipment = {
  equipmentCode: 'genuine concerning',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
