import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'cdc0b4f9-0a94-4ec5-80b1-f025d37d6fb7',
};

export const sampleWithPartialData: IAuthority = {
  name: '5c6b0665-907b-482f-add8-77caaaec9405',
};

export const sampleWithFullData: IAuthority = {
  name: 'b9120fe2-38be-4b6d-adee-f8e54cc9f0ff',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
