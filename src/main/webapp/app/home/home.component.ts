import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AccountService } from '../core/auth/account.service';
import { HomeService } from '../home/home.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // Import CommonModule for ngIf
import { RouterModule } from '@angular/router'; // Correct import for RouterModule
import dayjs from 'dayjs/esm';
import { Customer, ICustomer } from '../entities/customer/customer.model';
import { CusStatus } from '../entities/enumerations/cus-status.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SortByDirective, SortDirective, SortService, SortState, sortStateSignal } from 'app/shared/sort';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ItemCountComponent } from 'app/shared/pagination';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Order } from '../entities/order/order.model';
import SharedModule from 'app/shared/shared.module';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderService } from '../entities/order/service/order.service';
import { ContactModalComponent } from './contact-modal/contact-modal.component';
import { PlanModalComponent } from './plan-modal/plan-modal.component';
@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule, ItemCountComponent, NgbPaginationModule, SharedModule], // Removed SharedModule
})
export default class HomeComponent implements OnInit, OnDestroy {
  isSubmit = false;
  account: any = null;

  customerSignal = signal<Customer[] | null>(null);
  orderSignal = signal<Order[] | null>(null);
  submitSuccess = false;
  name = '';
  email = '';
  phone = '';
  message = '';
  address = '';
  selectedProvinceId: number | null = null;
  selectedDistrictId: number | null = null;
  selectedWardId: number | null = null;
  status: keyof typeof CusStatus = CusStatus.NEW;
  provinces: any[] = [];
  districts: any[] = [];
  wards: any[] = [];
  filterDistricts: any[] = [];
  filterWards: any[] = [];
  totalItems = signal(0);
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  sortState = sortStateSignal({});
  hasOrders = false;

  protected modalService = inject(NgbModal);
  private readonly destroy$ = new Subject<void>();
  private accountService = inject(AccountService);
  private homeService = inject(HomeService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private sortService = inject(SortService);
  private orderService = inject(OrderService);
  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        if (this.account?.staff?.id) {
          this.checkHasOrder();
          this.getOrderForStaff();
        }
      });

    this.homeService.getLocations().subscribe({
      next: data => {
        this.provinces = data.provinces.map((province: { id: number; name: string }) => ({
          id: province.id,
          name: province.name,
        }));
        this.districts = data.districts;
        this.wards = data.wards;
      },
    });
    this.loadAll();
  }

  updateAddress(): void {
    const province = this.provinces.find(p => p.id === this.selectedProvinceId)?.name ?? '';
    const district = this.filterDistricts.find(d => d.id === this.selectedDistrictId)?.name ?? '';
    const ward = this.filterWards.find(w => w.id === this.selectedWardId)?.name ?? '';
    this.address = [ward, district, province].filter(Boolean).join(', ');
  }

  onProvinceChange(event: Event): void {
    const selectedProvinceId = Number((event.target as HTMLSelectElement).value);
    this.filterDistricts = this.districts.filter(district => district.province?.id === selectedProvinceId);
    this.filterWards = [];
    this.updateAddress();
  }

  onDistrictChange(event: Event): void {
    const selectedDistrictId = Number((event.target as HTMLSelectElement).value);
    this.filterWards = this.wards.filter(ward => ward.district?.id === selectedDistrictId);
    this.updateAddress();
  }

  onWardChange(): void {
    this.updateAddress();
  }

  submit(): void {
    this.updateAddress();
    const customer: ICustomer = {
      id: 0,
      userName: this.name,
      displayName: this.name,
      email: this.email,
      mobile: this.phone,
      address: this.address,
      note: this.message,
      status: 'NEW',
      createDate: dayjs(),
      province: this.selectedProvinceId ? { id: this.selectedProvinceId } : null,
      district: this.selectedDistrictId ? { id: this.selectedDistrictId } : null,
      ward: this.selectedWardId ? { id: this.selectedWardId } : null,
    };

    this.homeService.createCustomer(customer).subscribe({
      next: response => {
        this.submitSuccess = true;
        this.resetForm();
      },
      error: err => {
        this.submitSuccess = false;
      },
    });
  }

  resetForm(): void {
    this.name = '';
    this.email = '';
    this.phone = '';
    this.message = '';
    this.address = '';
    this.selectedProvinceId = null;
    this.selectedDistrictId = null;
    this.selectedWardId = null;
    this.filterDistricts = [];
    this.filterWards = [];
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  trackIdentity(_index: number, item: Customer): number {
    return item.id;
  }

  get Customers(): Customer[] | null {
    return this.customerSignal();
  }

  get orders(): Order[] | null {
    return this.orderSignal();
  }

  loadAll(): void {
    this.homeService
      .getAllCustomer({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sortService.buildSortParam(this.sortState(), 'id'),
      })
      .subscribe({
        next: (res: HttpResponse<Customer[]>) => {
          this.onSuccess(res.body, res.headers);
        },
      });
  }

  transition(sortState?: SortState): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: this.sortService.buildSortParam(sortState ?? this.sortState()),
      },
    });
  }

  checkHasOrder(): void {
    this.homeService.hasOrder(this.account.staff.id).subscribe(result => {
      this.hasOrders = result;
    });
  }

  getOrderForStaff(): void {
    this.homeService
      .getOrderForStaff(this.account.staff.id, {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sortService.buildSortParam(this.sortState(), 'id'),
      })
      .subscribe({
        next: (res: HttpResponse<Order[]>) => {
          // alert('Order data: ' + JSON.stringify(res.body, null, 2));
          this.orderForStaff(res.body, res.headers);
        },
      });
  }

  contactModal(order: Order): void {
    this.orderService.find(order.id).subscribe({
      next: () => {
        const modalRef = this.modalService.open(ContactModalComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.order = order;
        // Listen to the modal close event and reload the orders
        modalRef.result.then(result => {
          if (result === 'save') {
            this.getOrderForStaff();
          }
        });
      },
    });
  }

  planModal(order: Order): void {
    this.orderService.find(order.id).subscribe({
      next: () => {
        const modalRef = this.modalService.open(PlanModalComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.order = order;
        // Listen to the modal close event and reload the orders
        modalRef.result.then(result => {
          if (result === 'save') {
            this.getOrderForStaff();
          }
        });
      },
    });
  }

  private onSuccess(customers: Customer[] | null, headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get('X-Total-Count')));
    this.customerSignal.set(customers);
  }

  private orderForStaff(order: Order[] | null, headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get('X-Total-Count')));
    this.orderSignal.set(order);
    // alert('Order data: ' + JSON.stringify(order, null, 2));
  }
}
