import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISendSMS } from '../send-sms.model';
import { SendSMSService } from '../service/send-sms.service';

const sendSMSResolve = (route: ActivatedRouteSnapshot): Observable<null | ISendSMS> => {
  const id = route.params.id;
  if (id) {
    return inject(SendSMSService)
      .find(id)
      .pipe(
        mergeMap((sendSMS: HttpResponse<ISendSMS>) => {
          if (sendSMS.body) {
            return of(sendSMS.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default sendSMSResolve;
