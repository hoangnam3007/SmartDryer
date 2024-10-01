import { IDistrict } from 'app/entities/district/district.model';

export interface IWard {
  id: number;
  code?: string | null;
  name?: string | null;
  district?: Pick<IDistrict, 'id'> | null;
}

export class Ward implements IWard {
  constructor(
    public id: number,
    public code?: string | null,
    public name?: string | null,
    public district?: Pick<IDistrict, 'id'> | null,
  ) {}
}

export type NewWard = Omit<IWard, 'id'> & { id: null };
