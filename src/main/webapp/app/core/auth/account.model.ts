import { IStaff } from '../../entities/staff/staff.model';
import { ISale } from '../../entities/sale/sale.model';
export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string | null,
    public langKey: string,
    public lastName: string | null,
    public login: string,
    public imageUrl: string | null,
    public staff?: Pick<IStaff, 'id'> | null,
    public sale?: Pick<ISale, 'id'> | null,
  ) {}
}
