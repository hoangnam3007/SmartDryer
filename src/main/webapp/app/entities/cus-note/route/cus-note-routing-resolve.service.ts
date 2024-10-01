import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICusNote } from '../cus-note.model';
import { CusNoteService } from '../service/cus-note.service';

const cusNoteResolve = (route: ActivatedRouteSnapshot): Observable<null | ICusNote> => {
  const id = route.params.id;
  if (id) {
    return inject(CusNoteService)
      .find(id)
      .pipe(
        mergeMap((cusNote: HttpResponse<ICusNote>) => {
          if (cusNote.body) {
            return of(cusNote.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cusNoteResolve;
