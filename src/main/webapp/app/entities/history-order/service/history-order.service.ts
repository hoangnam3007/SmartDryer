import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistoryOrder, NewHistoryOrder } from '../history-order.model';

export type PartialUpdateHistoryOrder = Partial<IHistoryOrder> & Pick<IHistoryOrder, 'id'>;

type RestOf<T extends IHistoryOrder | NewHistoryOrder> = Omit<T, 'modifiedDate'> & {
  modifiedDate?: string | null;
};

export type RestHistoryOrder = RestOf<IHistoryOrder>;

export type NewRestHistoryOrder = RestOf<NewHistoryOrder>;

export type PartialUpdateRestHistoryOrder = RestOf<PartialUpdateHistoryOrder>;

export type EntityResponseType = HttpResponse<IHistoryOrder>;
export type EntityArrayResponseType = HttpResponse<IHistoryOrder[]>;

@Injectable({ providedIn: 'root' })
export class HistoryOrderService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/history-orders');

  create(historyOrder: NewHistoryOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historyOrder);
    return this.http
      .post<RestHistoryOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historyOrder: IHistoryOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historyOrder);
    return this.http
      .put<RestHistoryOrder>(`${this.resourceUrl}/${this.getHistoryOrderIdentifier(historyOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historyOrder: PartialUpdateHistoryOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historyOrder);
    return this.http
      .patch<RestHistoryOrder>(`${this.resourceUrl}/${this.getHistoryOrderIdentifier(historyOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistoryOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoryOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHistoryOrderIdentifier(historyOrder: Pick<IHistoryOrder, 'id'>): number {
    return historyOrder.id;
  }

  compareHistoryOrder(o1: Pick<IHistoryOrder, 'id'> | null, o2: Pick<IHistoryOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoryOrderIdentifier(o1) === this.getHistoryOrderIdentifier(o2) : o1 === o2;
  }

  addHistoryOrderToCollectionIfMissing<Type extends Pick<IHistoryOrder, 'id'>>(
    historyOrderCollection: Type[],
    ...historyOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historyOrders: Type[] = historyOrdersToCheck.filter(isPresent);
    if (historyOrders.length > 0) {
      const historyOrderCollectionIdentifiers = historyOrderCollection.map(historyOrderItem =>
        this.getHistoryOrderIdentifier(historyOrderItem),
      );
      const historyOrdersToAdd = historyOrders.filter(historyOrderItem => {
        const historyOrderIdentifier = this.getHistoryOrderIdentifier(historyOrderItem);
        if (historyOrderCollectionIdentifiers.includes(historyOrderIdentifier)) {
          return false;
        }
        historyOrderCollectionIdentifiers.push(historyOrderIdentifier);
        return true;
      });
      return [...historyOrdersToAdd, ...historyOrderCollection];
    }
    return historyOrderCollection;
  }

  protected convertDateFromClient<T extends IHistoryOrder | NewHistoryOrder | PartialUpdateHistoryOrder>(historyOrder: T): RestOf<T> {
    return {
      ...historyOrder,
      modifiedDate: historyOrder.modifiedDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHistoryOrder: RestHistoryOrder): IHistoryOrder {
    return {
      ...restHistoryOrder,
      modifiedDate: restHistoryOrder.modifiedDate ? dayjs(restHistoryOrder.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistoryOrder>): HttpResponse<IHistoryOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistoryOrder[]>): HttpResponse<IHistoryOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
