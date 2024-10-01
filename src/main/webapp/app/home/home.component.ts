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
@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule, ItemCountComponent, NgbPaginationModule], // Removed SharedModule
})
export default class HomeComponent implements OnInit, OnDestroy {
  isSubmit = false;
  account: any = null;

  customerSignal = signal<Customer[] | null>(null);
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
  // Arrays for provinces, districts, and wards
  provinces: any[] = [];
  districts: any[] = [];
  wards: any[] = [];
  filterDistricts: any[] = [];
  filterWards: any[] = [];
  totalItems = signal(0);
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  sortState = sortStateSignal({});

  private readonly destroy$ = new Subject<void>();
  private accountService = inject(AccountService);
  private homeService = inject(HomeService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private sortService = inject(SortService);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    // Fetch the location data (provinces, districts, wards)
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

  // Update the address when province, district, or ward changes
  updateAddress(): void {
    const province = this.provinces.find(p => p.id === this.selectedProvinceId)?.name ?? '';
    const district = this.filterDistricts.find(d => d.id === this.selectedDistrictId)?.name ?? '';
    const ward = this.filterWards.find(w => w.id === this.selectedWardId)?.name ?? '';
    // Only join non-empty parts
    this.address = [ward, district, province].filter(Boolean).join(', ');
  }
  // Filter districts when a province is selected
  onProvinceChange(event: Event): void {
    const selectedProvinceId = Number((event.target as HTMLSelectElement).value);
    this.filterDistricts = this.districts.filter(district => district.province?.id === selectedProvinceId);
    this.filterWards = []; // Reset wards
    this.updateAddress(); // Update the address after change
  }

  // Filter wards when a district is selected
  onDistrictChange(event: Event): void {
    const selectedDistrictId = Number((event.target as HTMLSelectElement).value);
    this.filterWards = this.wards.filter(ward => ward.district?.id === selectedDistrictId);
    this.updateAddress(); // Update the address after change
  }

  onWardChange(): void {
    this.updateAddress(); // Update the address after ward change
  }

  submit(): void {
    this.updateAddress();
    const customer: ICustomer = {
      id: 0,
      userName: this.name, // Map name to userName or displayName
      displayName: this.name,
      email: this.email,
      mobile: this.phone,
      address: this.address,
      note: this.message,
      status: 'NEW', // Assuming this maps to the CusStatus enum
      createDate: dayjs(), // Get the current date
      province: this.selectedProvinceId ? { id: this.selectedProvinceId } : null, // Ensure correct referencing of IDs
      district: this.selectedDistrictId ? { id: this.selectedDistrictId } : null,
      ward: this.selectedWardId ? { id: this.selectedWardId } : null,
    };
    // alert('Customer Data JSON:\n' + JSON.stringify(customer, null, 2));
    // Call the service to create the new customer
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
    // Clear all form fields and reset the address
    this.name = '';
    this.email = '';
    this.phone = '';
    this.message = '';
    this.address = '';
    this.selectedProvinceId = null;
    this.selectedDistrictId = null;
    this.selectedWardId = null;
    // Reset filtered lists for districts and wards
    this.filterDistricts = [];
    this.filterWards = [];
  }

  // Simple login method
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

  private onSuccess(customers: Customer[] | null, headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get('X-Total-Count')));
    this.customerSignal.set(customers);
  }
}
