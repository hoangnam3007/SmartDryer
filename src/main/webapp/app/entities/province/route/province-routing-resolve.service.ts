import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProvince } from '../province.model';
import { ProvinceService } from '../service/province.service';

const provinceResolve = (route: ActivatedRouteSnapshot): Observable<null | IProvince> => {
  const id = route.params.id;
  if (id) {
    return inject(ProvinceService)
      .find(id)
      .pipe(
        mergeMap((province: HttpResponse<IProvince>) => {
          if (province.body) {
            return of(province.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default provinceResolve;
