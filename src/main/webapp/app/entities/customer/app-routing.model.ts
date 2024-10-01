import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from '../customer/list/customer.component';

const routes: Routes = [
  { path: 'customer', component: CustomerComponent },
  // Add other routes as needed
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' }), // This enables reloading on the same URL
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
