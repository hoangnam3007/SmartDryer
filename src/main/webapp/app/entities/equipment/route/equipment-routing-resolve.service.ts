import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';

const equipmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IEquipment> => {
  const id = route.params.id;
  if (id) {
    return inject(EquipmentService)
      .find(id)
      .pipe(
        mergeMap((equipment: HttpResponse<IEquipment>) => {
          if (equipment.body) {
            return of(equipment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default equipmentResolve;
