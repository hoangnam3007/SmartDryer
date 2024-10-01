import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISourceOrder } from '../source-order.model';
import { SourceOrderService } from '../service/source-order.service';

const sourceOrderResolve = (route: ActivatedRouteSnapshot): Observable<null | ISourceOrder> => {
  const id = route.params.id;
  if (id) {
    return inject(SourceOrderService)
      .find(id)
      .pipe(
        mergeMap((sourceOrder: HttpResponse<ISourceOrder>) => {
          if (sourceOrder.body) {
            return of(sourceOrder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default sourceOrderResolve;
