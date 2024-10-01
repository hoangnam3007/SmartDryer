export interface ISourceOrder {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewSourceOrder = Omit<ISourceOrder, 'id'> & { id: null };
