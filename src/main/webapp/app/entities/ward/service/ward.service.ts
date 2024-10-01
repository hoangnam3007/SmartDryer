import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWard, NewWard } from '../ward.model';

export type PartialUpdateWard = Partial<IWard> & Pick<IWard, 'id'>;

export type EntityResponseType = HttpResponse<IWard>;
export type EntityArrayResponseType = HttpResponse<IWard[]>;

@Injectable({ providedIn: 'root' })
export class WardService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wards');

  create(ward: NewWard): Observable<EntityResponseType> {
    return this.http.post<IWard>(this.resourceUrl, ward, { observe: 'response' });
  }

  update(ward: IWard): Observable<EntityResponseType> {
    return this.http.put<IWard>(`${this.resourceUrl}/${this.getWardIdentifier(ward)}`, ward, { observe: 'response' });
  }

  partialUpdate(ward: PartialUpdateWard): Observable<EntityResponseType> {
    return this.http.patch<IWard>(`${this.resourceUrl}/${this.getWardIdentifier(ward)}`, ward, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWardIdentifier(ward: Pick<IWard, 'id'>): number {
    return ward.id;
  }

  compareWard(o1: Pick<IWard, 'id'> | null, o2: Pick<IWard, 'id'> | null): boolean {
    return o1 && o2 ? this.getWardIdentifier(o1) === this.getWardIdentifier(o2) : o1 === o2;
  }

  addWardToCollectionIfMissing<Type extends Pick<IWard, 'id'>>(
    wardCollection: Type[],
    ...wardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const wards: Type[] = wardsToCheck.filter(isPresent);
    if (wards.length > 0) {
      const wardCollectionIdentifiers = wardCollection.map(wardItem => this.getWardIdentifier(wardItem));
      const wardsToAdd = wards.filter(wardItem => {
        const wardIdentifier = this.getWardIdentifier(wardItem);
        if (wardCollectionIdentifiers.includes(wardIdentifier)) {
          return false;
        }
        wardCollectionIdentifiers.push(wardIdentifier);
        return true;
      });
      return [...wardsToAdd, ...wardCollection];
    }
    return wardCollection;
  }
}
