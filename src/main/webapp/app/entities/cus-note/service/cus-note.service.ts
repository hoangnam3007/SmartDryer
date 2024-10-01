import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICusNote, NewCusNote } from '../cus-note.model';

export type PartialUpdateCusNote = Partial<ICusNote> & Pick<ICusNote, 'id'>;

type RestOf<T extends ICusNote | NewCusNote> = Omit<T, 'createDate'> & {
  createDate?: string | null;
};

export type RestCusNote = RestOf<ICusNote>;

export type NewRestCusNote = RestOf<NewCusNote>;

export type PartialUpdateRestCusNote = RestOf<PartialUpdateCusNote>;

export type EntityResponseType = HttpResponse<ICusNote>;
export type EntityArrayResponseType = HttpResponse<ICusNote[]>;

@Injectable({ providedIn: 'root' })
export class CusNoteService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cus-notes');

  create(cusNote: NewCusNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cusNote);
    return this.http
      .post<RestCusNote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cusNote: ICusNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cusNote);
    return this.http
      .put<RestCusNote>(`${this.resourceUrl}/${this.getCusNoteIdentifier(cusNote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cusNote: PartialUpdateCusNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cusNote);
    return this.http
      .patch<RestCusNote>(`${this.resourceUrl}/${this.getCusNoteIdentifier(cusNote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCusNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCusNote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCusNoteIdentifier(cusNote: Pick<ICusNote, 'id'>): number {
    return cusNote.id;
  }

  compareCusNote(o1: Pick<ICusNote, 'id'> | null, o2: Pick<ICusNote, 'id'> | null): boolean {
    return o1 && o2 ? this.getCusNoteIdentifier(o1) === this.getCusNoteIdentifier(o2) : o1 === o2;
  }

  addCusNoteToCollectionIfMissing<Type extends Pick<ICusNote, 'id'>>(
    cusNoteCollection: Type[],
    ...cusNotesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cusNotes: Type[] = cusNotesToCheck.filter(isPresent);
    if (cusNotes.length > 0) {
      const cusNoteCollectionIdentifiers = cusNoteCollection.map(cusNoteItem => this.getCusNoteIdentifier(cusNoteItem));
      const cusNotesToAdd = cusNotes.filter(cusNoteItem => {
        const cusNoteIdentifier = this.getCusNoteIdentifier(cusNoteItem);
        if (cusNoteCollectionIdentifiers.includes(cusNoteIdentifier)) {
          return false;
        }
        cusNoteCollectionIdentifiers.push(cusNoteIdentifier);
        return true;
      });
      return [...cusNotesToAdd, ...cusNoteCollection];
    }
    return cusNoteCollection;
  }

  protected convertDateFromClient<T extends ICusNote | NewCusNote | PartialUpdateCusNote>(cusNote: T): RestOf<T> {
    return {
      ...cusNote,
      createDate: cusNote.createDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCusNote: RestCusNote): ICusNote {
    return {
      ...restCusNote,
      createDate: restCusNote.createDate ? dayjs(restCusNote.createDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCusNote>): HttpResponse<ICusNote> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCusNote[]>): HttpResponse<ICusNote[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
