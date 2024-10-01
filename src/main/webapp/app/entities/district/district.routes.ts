import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DistrictResolve from './route/district-routing-resolve.service';

const districtRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/district.component').then(m => m.DistrictComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/district-detail.component').then(m => m.DistrictDetailComponent),
    resolve: {
      district: DistrictResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/district-update.component').then(m => m.DistrictUpdateComponent),
    resolve: {
      district: DistrictResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/district-update.component').then(m => m.DistrictUpdateComponent),
    resolve: {
      district: DistrictResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default districtRoute;
