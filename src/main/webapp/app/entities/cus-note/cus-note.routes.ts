import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CusNoteResolve from './route/cus-note-routing-resolve.service';

const cusNoteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cus-note.component').then(m => m.CusNoteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cus-note-detail.component').then(m => m.CusNoteDetailComponent),
    resolve: {
      cusNote: CusNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cus-note-update.component').then(m => m.CusNoteUpdateComponent),
    resolve: {
      cusNote: CusNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cus-note-update.component').then(m => m.CusNoteUpdateComponent),
    resolve: {
      cusNote: CusNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cusNoteRoute;
