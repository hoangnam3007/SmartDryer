import { IProvince } from 'app/entities/province/province.model';

export interface IDistrict {
  id: number;
  code?: string | null;
  name?: string | null;
  province?: Pick<IProvince, 'id'> | null;
}

export class District implements IDistrict {
  constructor(
    public id: number,
    public code?: string | null,
    public name?: string | null,
    public province?: Pick<IProvince, 'id'> | null,
  ) {}
}

export type NewDistrict = Omit<IDistrict, 'id'> & { id: null };
