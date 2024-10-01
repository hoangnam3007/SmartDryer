import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoryOrder } from '../history-order.model';
import { HistoryOrderService } from '../service/history-order.service';

const historyOrderResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistoryOrder> => {
  const id = route.params.id;
  if (id) {
    return inject(HistoryOrderService)
      .find(id)
      .pipe(
        mergeMap((historyOrder: HttpResponse<IHistoryOrder>) => {
          if (historyOrder.body) {
            return of(historyOrder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default historyOrderResolve;
