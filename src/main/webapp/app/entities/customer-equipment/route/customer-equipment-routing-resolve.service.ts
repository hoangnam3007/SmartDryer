import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerEquipment } from '../customer-equipment.model';
import { CustomerEquipmentService } from '../service/customer-equipment.service';

const customerEquipmentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomerEquipment> => {
  const id = route.params.id;
  if (id) {
    return inject(CustomerEquipmentService)
      .find(id)
      .pipe(
        mergeMap((customerEquipment: HttpResponse<ICustomerEquipment>) => {
          if (customerEquipment.body) {
            return of(customerEquipment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default customerEquipmentResolve;
