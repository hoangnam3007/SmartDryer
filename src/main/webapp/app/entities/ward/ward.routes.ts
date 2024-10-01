import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WardResolve from './route/ward-routing-resolve.service';

const wardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ward.component').then(m => m.WardComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ward-detail.component').then(m => m.WardDetailComponent),
    resolve: {
      ward: WardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ward-update.component').then(m => m.WardUpdateComponent),
    resolve: {
      ward: WardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ward-update.component').then(m => m.WardUpdateComponent),
    resolve: {
      ward: WardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default wardRoute;
