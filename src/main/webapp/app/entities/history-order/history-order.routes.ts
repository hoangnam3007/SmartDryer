import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HistoryOrderResolve from './route/history-order-routing-resolve.service';

const historyOrderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/history-order.component').then(m => m.HistoryOrderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/history-order-detail.component').then(m => m.HistoryOrderDetailComponent),
    resolve: {
      historyOrder: HistoryOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/history-order-update.component').then(m => m.HistoryOrderUpdateComponent),
    resolve: {
      historyOrder: HistoryOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/history-order-update.component').then(m => m.HistoryOrderUpdateComponent),
    resolve: {
      historyOrder: HistoryOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historyOrderRoute;
