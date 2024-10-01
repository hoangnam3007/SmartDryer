import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SendSMSResolve from './route/send-sms-routing-resolve.service';

const sendSMSRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/send-sms.component').then(m => m.SendSMSComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/send-sms-detail.component').then(m => m.SendSMSDetailComponent),
    resolve: {
      sendSMS: SendSMSResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/send-sms-update.component').then(m => m.SendSMSUpdateComponent),
    resolve: {
      sendSMS: SendSMSResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/send-sms-update.component').then(m => m.SendSMSUpdateComponent),
    resolve: {
      sendSMS: SendSMSResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sendSMSRoute;
