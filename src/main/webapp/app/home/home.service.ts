import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer, ICustomer } from '../entities/customer/customer.model';
import { createRequestOption } from 'app/core/request/request-util';
import { Pagination } from 'app/core/request/request.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IOrder } from '../entities/order/order.model';
@Injectable({
  providedIn: 'root',
})
export class HomeService {
  private apiUrl = '/api/locations';
  private applicationConfigService = inject(ApplicationConfigService);
  private customerUrl = this.applicationConfigService.getEndpointFor('api/customers');
  private orderUrl = this.applicationConfigService.getEndpointFor('api/orders/staff');
  constructor(private http: HttpClient) {}

  getLocations(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  createCustomer(customer: ICustomer): Observable<ICustomer> {
    return this.http.post<ICustomer>(this.customerUrl, customer);
  }

  getAllCustomer(req?: Pagination): Observable<HttpResponse<ICustomer[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustomer[]>(this.customerUrl, { params: options, observe: 'response' });
  }

  hasOrder(staffId: number): Observable<boolean> {
    const url = `${this.orderUrl}/${staffId}/has-orders`;
    return this.http.get<boolean>(url);
  }

  getOrderForStaff(staffId: number, req?: Pagination): Observable<HttpResponse<IOrder[]>> {
    const orderUrl = `${this.orderUrl}/${staffId}/orders`; // Add slash before staffId
    const options = createRequestOption(req);
    return this.http.get<IOrder[]>(orderUrl, { params: options, observe: 'response' });
  }
}
