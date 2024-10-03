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
import { AssignOrderComponent } from '../Assign/assignOrder.component';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IOrder, Order } from '../order.model';
import { IStaff } from '../../staff/staff.model';
import { EntityArrayResponseType, OrderService } from '../service/order.service';
import { OrderDeleteDialogComponent } from '../delete/order-delete-dialog.component';
import { OrderDetailComponent } from '../detail/order-detail.component';
@Component({
  standalone: true,
  selector: 'jhi-order',
  templateUrl: './order.component.html',
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
export class OrderComponent implements OnInit {
  subscription: Subscription | null = null;
  orders?: IOrder[];
  isLoading = false;
  provinces: any[] = [];
  districts: any[] = [];
  wards: any[] = [];
  filterDistricts: any[] = [];
  filterWards: any[] = [];
  selectedProvinceId: number | null = null;
  selectedDistrictId: number | null = null;
  selectedWardId: number | null = null;
  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public router = inject(Router);
  protected orderService = inject(OrderService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  trackId = (_index: number, item: IOrder): number => this.orderService.getOrderIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
    this.orderService.getLocations().subscribe({
      next: data => {
        this.provinces = data.provinces.map((province: { id: number; name: string }) => ({
          id: province.id,
          name: province.name,
        }));
        this.districts = data.districts; // Ensure the correct structure for districts
        this.wards = data.wards; // Ensure the correct structure for wards
      },
    });
  }
  onProvinceChange(event: Event): void {
    const selectedProvinceId = Number((event.target as HTMLSelectElement).value);
    this.filterDistricts = this.districts.filter(district => district.province?.id === selectedProvinceId);
    this.filterWards = []; // Reset wards
    this.load();
  }

  // Filter wards when a district is selected
  onDistrictChange(event: Event): void {
    const selectedDistrictId = Number((event.target as HTMLSelectElement).value);
    this.filterWards = this.wards.filter(ward => ward.district?.id === selectedDistrictId);
    this.load();
  }

  // Update the address when ward changes (optional)
  onWardChange(event: Event): void {
    const selectedWardId = Number((event.target as HTMLSelectElement).value);
    this.load();
  }

  delete(order: IOrder): void {
    const modalRef = this.modalService.open(OrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.order = order;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
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

  assignOrder(order: IOrder): void {
    this.orderService.getAllStaff().subscribe({
      next: staff => {
        const modalRef = this.modalService.open(AssignOrderComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.order = order; // Pass the order to the modal
      },
    });
  }

  viewOrder(order: IOrder): void {
    this.orderService.find(order.id).subscribe({
      next: () => {
        const modalRef = this.modalService.open(OrderDetailComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.order = order;
      },
    });
  }
  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.orders = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IOrder[] | null): IOrder[] {
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

    if (this.selectedProvinceId) {
      queryObject.province = this.selectedProvinceId;
    }
    if (this.selectedDistrictId) {
      queryObject.district = this.selectedDistrictId;
    }
    if (this.selectedWardId) {
      queryObject.ward = this.selectedWardId;
    }
    return this.orderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
