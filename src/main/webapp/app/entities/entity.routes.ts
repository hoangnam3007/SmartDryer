import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'smartDryerApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'cus-note',
    data: { pageTitle: 'smartDryerApp.cusNote.home.title' },
    loadChildren: () => import('./cus-note/cus-note.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'smartDryerApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'customer-equipment',
    data: { pageTitle: 'smartDryerApp.customerEquipment.home.title' },
    loadChildren: () => import('./customer-equipment/customer-equipment.routes'),
  },
  {
    path: 'district',
    data: { pageTitle: 'smartDryerApp.district.home.title' },
    loadChildren: () => import('./district/district.routes'),
  },
  {
    path: 'equipment',
    data: { pageTitle: 'smartDryerApp.equipment.home.title' },
    loadChildren: () => import('./equipment/equipment.routes'),
  },
  {
    path: 'history-order',
    data: { pageTitle: 'smartDryerApp.historyOrder.home.title' },
    loadChildren: () => import('./history-order/history-order.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'smartDryerApp.order.home.title' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'province',
    data: { pageTitle: 'smartDryerApp.province.home.title' },
    loadChildren: () => import('./province/province.routes'),
  },
  {
    path: 'sale',
    data: { pageTitle: 'smartDryerApp.sale.home.title' },
    loadChildren: () => import('./sale/sale.routes'),
  },
  {
    path: 'send-sms',
    data: { pageTitle: 'smartDryerApp.sendSMS.home.title' },
    loadChildren: () => import('./send-sms/send-sms.routes'),
  },
  {
    path: 'source-order',
    data: { pageTitle: 'smartDryerApp.sourceOrder.home.title' },
    loadChildren: () => import('./source-order/source-order.routes'),
  },
  {
    path: 'staff',
    data: { pageTitle: 'smartDryerApp.staff.home.title' },
    loadChildren: () => import('./staff/staff.routes'),
  },
  {
    path: 'ward',
    data: { pageTitle: 'smartDryerApp.ward.home.title' },
    loadChildren: () => import('./ward/ward.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
