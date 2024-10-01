import { ICustomerEquipment, NewCustomerEquipment } from './customer-equipment.model';

export const sampleWithRequiredData: ICustomerEquipment = {
  id: 31262,
};

export const sampleWithPartialData: ICustomerEquipment = {
  id: 13225,
  quantily: 21248,
};

export const sampleWithFullData: ICustomerEquipment = {
  id: 13629,
  quantily: 27699,
};

export const sampleWithNewData: NewCustomerEquipment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
