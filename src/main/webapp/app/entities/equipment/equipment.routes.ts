import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EquipmentResolve from './route/equipment-routing-resolve.service';

const equipmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/equipment.component').then(m => m.EquipmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/equipment-detail.component').then(m => m.EquipmentDetailComponent),
    resolve: {
      equipment: EquipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/equipment-update.component').then(m => m.EquipmentUpdateComponent),
    resolve: {
      equipment: EquipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/equipment-update.component').then(m => m.EquipmentUpdateComponent),
    resolve: {
      equipment: EquipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default equipmentRoute;
