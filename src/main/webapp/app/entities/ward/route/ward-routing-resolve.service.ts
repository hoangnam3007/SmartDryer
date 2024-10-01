import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWard } from '../ward.model';
import { WardService } from '../service/ward.service';

const wardResolve = (route: ActivatedRouteSnapshot): Observable<null | IWard> => {
  const id = route.params.id;
  if (id) {
    return inject(WardService)
      .find(id)
      .pipe(
        mergeMap((ward: HttpResponse<IWard>) => {
          if (ward.body) {
            return of(ward.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default wardResolve;
