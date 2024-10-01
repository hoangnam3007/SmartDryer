import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ISale } from 'app/entities/sale/sale.model';
import { SaleService } from 'app/entities/sale/service/sale.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';
import { ISourceOrder } from 'app/entities/source-order/source-order.model';
import { SourceOrderService } from 'app/entities/source-order/service/source-order.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { IWard } from 'app/entities/ward/ward.model';
import { WardService } from 'app/entities/ward/service/ward.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { OrderService } from '../service/order.service';
import { IOrder } from '../order.model';
import { OrderFormGroup, OrderFormService } from './order-form.service';

@Component({
  standalone: true,
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  order: IOrder | null = null;
  orderStatusValues = Object.keys(OrderStatus);

  customersSharedCollection: ICustomer[] = [];
  salesSharedCollection: ISale[] = [];
  staffSharedCollection: IStaff[] = [];
  sourceOrdersSharedCollection: ISourceOrder[] = [];
  provincesSharedCollection: IProvince[] = [];
  districtsSharedCollection: IDistrict[] = [];
  wardsSharedCollection: IWard[] = [];

  protected orderService = inject(OrderService);
  protected orderFormService = inject(OrderFormService);
  protected customerService = inject(CustomerService);
  protected saleService = inject(SaleService);
  protected staffService = inject(StaffService);
  protected sourceOrderService = inject(SourceOrderService);
  protected provinceService = inject(ProvinceService);
  protected districtService = inject(DistrictService);
  protected wardService = inject(WardService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareSale = (o1: ISale | null, o2: ISale | null): boolean => this.saleService.compareSale(o1, o2);

  compareStaff = (o1: IStaff | null, o2: IStaff | null): boolean => this.staffService.compareStaff(o1, o2);

  compareSourceOrder = (o1: ISourceOrder | null, o2: ISourceOrder | null): boolean => this.sourceOrderService.compareSourceOrder(o1, o2);

  compareProvince = (o1: IProvince | null, o2: IProvince | null): boolean => this.provinceService.compareProvince(o1, o2);

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  compareWard = (o1: IWard | null, o2: IWard | null): boolean => this.wardService.compareWard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
      if (order) {
        this.updateForm(order);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id !== null) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      order.customer,
    );
    this.salesSharedCollection = this.saleService.addSaleToCollectionIfMissing<ISale>(this.salesSharedCollection, order.sale);
    this.staffSharedCollection = this.staffService.addStaffToCollectionIfMissing<IStaff>(this.staffSharedCollection, order.staff);
    this.sourceOrdersSharedCollection = this.sourceOrderService.addSourceOrderToCollectionIfMissing<ISourceOrder>(
      this.sourceOrdersSharedCollection,
      order.sourceOrder,
    );
    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing<IProvince>(
      this.provincesSharedCollection,
      order.province,
    );
    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      order.district,
    );
    this.wardsSharedCollection = this.wardService.addWardToCollectionIfMissing<IWard>(this.wardsSharedCollection, order.ward);
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.order?.customer)),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.saleService
      .query()
      .pipe(map((res: HttpResponse<ISale[]>) => res.body ?? []))
      .pipe(map((sales: ISale[]) => this.saleService.addSaleToCollectionIfMissing<ISale>(sales, this.order?.sale)))
      .subscribe((sales: ISale[]) => (this.salesSharedCollection = sales));

    this.staffService
      .query()
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing<IStaff>(staff, this.order?.staff)))
      .subscribe((staff: IStaff[]) => (this.staffSharedCollection = staff));

    this.sourceOrderService
      .query()
      .pipe(map((res: HttpResponse<ISourceOrder[]>) => res.body ?? []))
      .pipe(
        map((sourceOrders: ISourceOrder[]) =>
          this.sourceOrderService.addSourceOrderToCollectionIfMissing<ISourceOrder>(sourceOrders, this.order?.sourceOrder),
        ),
      )
      .subscribe((sourceOrders: ISourceOrder[]) => (this.sourceOrdersSharedCollection = sourceOrders));

    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) => this.provinceService.addProvinceToCollectionIfMissing<IProvince>(provinces, this.order?.province)),
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));

    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) => this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.order?.district)),
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));

    this.wardService
      .query()
      .pipe(map((res: HttpResponse<IWard[]>) => res.body ?? []))
      .pipe(map((wards: IWard[]) => this.wardService.addWardToCollectionIfMissing<IWard>(wards, this.order?.ward)))
      .subscribe((wards: IWard[]) => (this.wardsSharedCollection = wards));
  }
}
