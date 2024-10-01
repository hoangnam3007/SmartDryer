import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISourceOrder, NewSourceOrder } from '../source-order.model';

export type PartialUpdateSourceOrder = Partial<ISourceOrder> & Pick<ISourceOrder, 'id'>;

export type EntityResponseType = HttpResponse<ISourceOrder>;
export type EntityArrayResponseType = HttpResponse<ISourceOrder[]>;

@Injectable({ providedIn: 'root' })
export class SourceOrderService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/source-orders');

  create(sourceOrder: NewSourceOrder): Observable<EntityResponseType> {
    return this.http.post<ISourceOrder>(this.resourceUrl, sourceOrder, { observe: 'response' });
  }

  update(sourceOrder: ISourceOrder): Observable<EntityResponseType> {
    return this.http.put<ISourceOrder>(`${this.resourceUrl}/${this.getSourceOrderIdentifier(sourceOrder)}`, sourceOrder, {
      observe: 'response',
    });
  }

  partialUpdate(sourceOrder: PartialUpdateSourceOrder): Observable<EntityResponseType> {
    return this.http.patch<ISourceOrder>(`${this.resourceUrl}/${this.getSourceOrderIdentifier(sourceOrder)}`, sourceOrder, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISourceOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISourceOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSourceOrderIdentifier(sourceOrder: Pick<ISourceOrder, 'id'>): number {
    return sourceOrder.id;
  }

  compareSourceOrder(o1: Pick<ISourceOrder, 'id'> | null, o2: Pick<ISourceOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getSourceOrderIdentifier(o1) === this.getSourceOrderIdentifier(o2) : o1 === o2;
  }

  addSourceOrderToCollectionIfMissing<Type extends Pick<ISourceOrder, 'id'>>(
    sourceOrderCollection: Type[],
    ...sourceOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sourceOrders: Type[] = sourceOrdersToCheck.filter(isPresent);
    if (sourceOrders.length > 0) {
      const sourceOrderCollectionIdentifiers = sourceOrderCollection.map(sourceOrderItem => this.getSourceOrderIdentifier(sourceOrderItem));
      const sourceOrdersToAdd = sourceOrders.filter(sourceOrderItem => {
        const sourceOrderIdentifier = this.getSourceOrderIdentifier(sourceOrderItem);
        if (sourceOrderCollectionIdentifiers.includes(sourceOrderIdentifier)) {
          return false;
        }
        sourceOrderCollectionIdentifiers.push(sourceOrderIdentifier);
        return true;
      });
      return [...sourceOrdersToAdd, ...sourceOrderCollection];
    }
    return sourceOrderCollection;
  }
}
