export interface IEquipment {
  id: number;
  equipmentCode?: string | null;
  description?: string | null;
}

export type NewEquipment = Omit<IEquipment, 'id'> & { id: null };
