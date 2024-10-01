import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerEquipment, NewCustomerEquipment } from '../customer-equipment.model';

export type PartialUpdateCustomerEquipment = Partial<ICustomerEquipment> & Pick<ICustomerEquipment, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerEquipment>;
export type EntityArrayResponseType = HttpResponse<ICustomerEquipment[]>;

@Injectable({ providedIn: 'root' })
export class CustomerEquipmentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-equipments');

  create(customerEquipment: NewCustomerEquipment): Observable<EntityResponseType> {
    return this.http.post<ICustomerEquipment>(this.resourceUrl, customerEquipment, { observe: 'response' });
  }

  update(customerEquipment: ICustomerEquipment): Observable<EntityResponseType> {
    return this.http.put<ICustomerEquipment>(
      `${this.resourceUrl}/${this.getCustomerEquipmentIdentifier(customerEquipment)}`,
      customerEquipment,
      { observe: 'response' },
    );
  }

  partialUpdate(customerEquipment: PartialUpdateCustomerEquipment): Observable<EntityResponseType> {
    return this.http.patch<ICustomerEquipment>(
      `${this.resourceUrl}/${this.getCustomerEquipmentIdentifier(customerEquipment)}`,
      customerEquipment,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerEquipment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerEquipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerEquipmentIdentifier(customerEquipment: Pick<ICustomerEquipment, 'id'>): number {
    return customerEquipment.id;
  }

  compareCustomerEquipment(o1: Pick<ICustomerEquipment, 'id'> | null, o2: Pick<ICustomerEquipment, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerEquipmentIdentifier(o1) === this.getCustomerEquipmentIdentifier(o2) : o1 === o2;
  }

  addCustomerEquipmentToCollectionIfMissing<Type extends Pick<ICustomerEquipment, 'id'>>(
    customerEquipmentCollection: Type[],
    ...customerEquipmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerEquipments: Type[] = customerEquipmentsToCheck.filter(isPresent);
    if (customerEquipments.length > 0) {
      const customerEquipmentCollectionIdentifiers = customerEquipmentCollection.map(customerEquipmentItem =>
        this.getCustomerEquipmentIdentifier(customerEquipmentItem),
      );
      const customerEquipmentsToAdd = customerEquipments.filter(customerEquipmentItem => {
        const customerEquipmentIdentifier = this.getCustomerEquipmentIdentifier(customerEquipmentItem);
        if (customerEquipmentCollectionIdentifiers.includes(customerEquipmentIdentifier)) {
          return false;
        }
        customerEquipmentCollectionIdentifiers.push(customerEquipmentIdentifier);
        return true;
      });
      return [...customerEquipmentsToAdd, ...customerEquipmentCollection];
    }
    return customerEquipmentCollection;
  }
}
