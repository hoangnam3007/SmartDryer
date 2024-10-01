export interface IProvince {
  id: number;
  code?: string | null;
  name?: string | null;
}

export class Province implements IProvince {
  constructor(
    public id: number,
    public code?: string | null,
    public name?: string | null,
  ) {}
}

export type NewProvince = Omit<IProvince, 'id'> & { id: null };
