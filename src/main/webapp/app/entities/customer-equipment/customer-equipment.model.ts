import { ICustomer } from 'app/entities/customer/customer.model';
import { IEquipment } from 'app/entities/equipment/equipment.model';

export interface ICustomerEquipment {
  id: number;
  quantily?: number | null;
  customer?: Pick<ICustomer, 'id'> | null;
  equipment?: Pick<IEquipment, 'id'> | null;
}

export type NewCustomerEquipment = Omit<ICustomerEquipment, 'id'> & { id: null };
