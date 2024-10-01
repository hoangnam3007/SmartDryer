import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProvinceResolve from './route/province-routing-resolve.service';

const provinceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/province.component').then(m => m.ProvinceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/province-detail.component').then(m => m.ProvinceDetailComponent),
    resolve: {
      province: ProvinceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/province-update.component').then(m => m.ProvinceUpdateComponent),
    resolve: {
      province: ProvinceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/province-update.component').then(m => m.ProvinceUpdateComponent),
    resolve: {
      province: ProvinceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default provinceRoute;
