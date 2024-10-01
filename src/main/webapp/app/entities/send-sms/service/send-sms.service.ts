import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISendSMS, NewSendSMS } from '../send-sms.model';

export type PartialUpdateSendSMS = Partial<ISendSMS> & Pick<ISendSMS, 'id'>;

type RestOf<T extends ISendSMS | NewSendSMS> = Omit<T, 'createDate' | 'sendedDate'> & {
  createDate?: string | null;
  sendedDate?: string | null;
};

export type RestSendSMS = RestOf<ISendSMS>;

export type NewRestSendSMS = RestOf<NewSendSMS>;

export type PartialUpdateRestSendSMS = RestOf<PartialUpdateSendSMS>;

export type EntityResponseType = HttpResponse<ISendSMS>;
export type EntityArrayResponseType = HttpResponse<ISendSMS[]>;

@Injectable({ providedIn: 'root' })
export class SendSMSService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/send-sms');

  create(sendSMS: NewSendSMS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sendSMS);
    return this.http
      .post<RestSendSMS>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sendSMS: ISendSMS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sendSMS);
    return this.http
      .put<RestSendSMS>(`${this.resourceUrl}/${this.getSendSMSIdentifier(sendSMS)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sendSMS: PartialUpdateSendSMS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sendSMS);
    return this.http
      .patch<RestSendSMS>(`${this.resourceUrl}/${this.getSendSMSIdentifier(sendSMS)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSendSMS>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSendSMS[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSendSMSIdentifier(sendSMS: Pick<ISendSMS, 'id'>): number {
    return sendSMS.id;
  }

  compareSendSMS(o1: Pick<ISendSMS, 'id'> | null, o2: Pick<ISendSMS, 'id'> | null): boolean {
    return o1 && o2 ? this.getSendSMSIdentifier(o1) === this.getSendSMSIdentifier(o2) : o1 === o2;
  }

  addSendSMSToCollectionIfMissing<Type extends Pick<ISendSMS, 'id'>>(
    sendSMSCollection: Type[],
    ...sendSMSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sendSMS: Type[] = sendSMSToCheck.filter(isPresent);
    if (sendSMS.length > 0) {
      const sendSMSCollectionIdentifiers = sendSMSCollection.map(sendSMSItem => this.getSendSMSIdentifier(sendSMSItem));
      const sendSMSToAdd = sendSMS.filter(sendSMSItem => {
        const sendSMSIdentifier = this.getSendSMSIdentifier(sendSMSItem);
        if (sendSMSCollectionIdentifiers.includes(sendSMSIdentifier)) {
          return false;
        }
        sendSMSCollectionIdentifiers.push(sendSMSIdentifier);
        return true;
      });
      return [...sendSMSToAdd, ...sendSMSCollection];
    }
    return sendSMSCollection;
  }

  protected convertDateFromClient<T extends ISendSMS | NewSendSMS | PartialUpdateSendSMS>(sendSMS: T): RestOf<T> {
    return {
      ...sendSMS,
      createDate: sendSMS.createDate?.format(DATE_FORMAT) ?? null,
      sendedDate: sendSMS.sendedDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSendSMS: RestSendSMS): ISendSMS {
    return {
      ...restSendSMS,
      createDate: restSendSMS.createDate ? dayjs(restSendSMS.createDate) : undefined,
      sendedDate: restSendSMS.sendedDate ? dayjs(restSendSMS.sendedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSendSMS>): HttpResponse<ISendSMS> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSendSMS[]>): HttpResponse<ISendSMS[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
