import { Component, NgZone, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Customer, ICustomer } from '../customer.model';
import { CustomerService, EntityArrayResponseType } from '../service/customer.service';
import { CustomerDeleteDialogComponent } from '../delete/customer-delete-dialog.component';
import { CustomerDetailComponent } from '../detail/customer-detail.component';
import { ApprovalSuccessComponent } from '../customer-approved/approval-success.component';
import { OrderService } from '../../order/service/order.service';
import { IOrder, NewOrder } from '../../order/order.model';
import dayjs from 'dayjs/esm';
import { OrderStatus } from '../../enumerations/order-status.model';
@Component({
  standalone: true,
  selector: 'jhi-customer',
  templateUrl: './customer.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
  ],
})
export class CustomerComponent implements OnInit {
  subscription: Subscription | null = null;
  customers?: ICustomer[];
  isLoading = false;
  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public router = inject(Router);
  protected customerService = inject(CustomerService);
  protected orderService = inject(OrderService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (_index: number, item: ICustomer): number => this.customerService.getCustomerIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  viewCustomer(item: Customer): void {
    this.customerService.find(item.id).subscribe({
      next: customer => {
        const modalRef = this.modalService.open(CustomerDetailComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.customer = customer;
      },
    });
  }

  approveCustomer(item: Customer): void {
    const updateCustomer = {
      ...item,
      status: 'APPROVED' as const,
    };
    this.customerService.update(updateCustomer).subscribe({
      next: () => {
        const newOrder: NewOrder = {
          id: null,
          code: 'ORDER_' + String(item.id),
          createBy: item.createBy,
          createDate: dayjs(),
          finishDate: null,
          status: 'NEW',
          amount: 0,
          saleNote: null,
          techNote: null,
          note: item.note,
          materialSource: null,
          cusName: item.displayName,
          cusAddress: item.address,
          cusMobile: item.mobile,
          imageURL: null,
          appointmentDate: null,
          activation: 0,
          customer: item.id ? { id: item.id } : null,
          sale: null,
          staff: null,
          province: item.province?.id ? { id: item.province.id } : null,
          district: item.district?.id ? { id: item.district.id } : null,
          ward: item.ward?.id ? { id: item.ward.id } : null,
        };

        this.orderService.create(newOrder).subscribe({
          next: () => {
            this.customers = this.customers?.filter(customer => customer.id !== item.id);
            this.showSuccessModal();
          },
        });
      },
    });
  }

  decline(item: Customer): void {
    const updateCustomer = {
      ...item,
      status: 'DECLINE' as const,
    };

    this.customerService.delete(updateCustomer.id).subscribe({
      next: () => {
        this.customers = this.customers?.filter(customer => customer.id !== item.id);
      },
    });
  }
  showSuccessModal(): void {
    this.modalService.open(ApprovalSuccessComponent, { size: 'sm', backdrop: 'static' });
    this.load();
  }
  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.customers = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: ICustomer[] | null): ICustomer[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.customerService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
