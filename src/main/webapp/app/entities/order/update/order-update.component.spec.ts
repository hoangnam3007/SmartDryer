import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

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
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';
import { OrderFormService } from './order-form.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Order Management Update Component', () => {
  let comp: OrderUpdateComponent;
  let fixture: ComponentFixture<OrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let customerService: CustomerService;
  let saleService: SaleService;
  let staffService: StaffService;
  let sourceOrderService: SourceOrderService;
  let provinceService: ProvinceService;
  let districtService: DistrictService;
  let wardService: WardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrderUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    customerService = TestBed.inject(CustomerService);
    saleService = TestBed.inject(SaleService);
    staffService = TestBed.inject(StaffService);
    sourceOrderService = TestBed.inject(SourceOrderService);
    provinceService = TestBed.inject(ProvinceService);
    districtService = TestBed.inject(DistrictService);
    wardService = TestBed.inject(WardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const customer: ICustomer = { id: 31480 };
      order.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 74 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sale query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const sale: ISale = { id: 809 };
      order.sale = sale;

      const saleCollection: ISale[] = [{ id: 19534 }];
      jest.spyOn(saleService, 'query').mockReturnValue(of(new HttpResponse({ body: saleCollection })));
      const additionalSales = [sale];
      const expectedCollection: ISale[] = [...additionalSales, ...saleCollection];
      jest.spyOn(saleService, 'addSaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(saleService.query).toHaveBeenCalled();
      expect(saleService.addSaleToCollectionIfMissing).toHaveBeenCalledWith(
        saleCollection,
        ...additionalSales.map(expect.objectContaining),
      );
      expect(comp.salesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Staff query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const staff: IStaff = { id: 23067 };
      order.staff = staff;

      const staffCollection: IStaff[] = [{ id: 13984 }];
      jest.spyOn(staffService, 'query').mockReturnValue(of(new HttpResponse({ body: staffCollection })));
      const additionalStaff = [staff];
      const expectedCollection: IStaff[] = [...additionalStaff, ...staffCollection];
      jest.spyOn(staffService, 'addStaffToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(staffService.query).toHaveBeenCalled();
      expect(staffService.addStaffToCollectionIfMissing).toHaveBeenCalledWith(
        staffCollection,
        ...additionalStaff.map(expect.objectContaining),
      );
      expect(comp.staffSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SourceOrder query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const sourceOrder: ISourceOrder = { id: 3033 };
      order.sourceOrder = sourceOrder;

      const sourceOrderCollection: ISourceOrder[] = [{ id: 27191 }];
      jest.spyOn(sourceOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: sourceOrderCollection })));
      const additionalSourceOrders = [sourceOrder];
      const expectedCollection: ISourceOrder[] = [...additionalSourceOrders, ...sourceOrderCollection];
      jest.spyOn(sourceOrderService, 'addSourceOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(sourceOrderService.query).toHaveBeenCalled();
      expect(sourceOrderService.addSourceOrderToCollectionIfMissing).toHaveBeenCalledWith(
        sourceOrderCollection,
        ...additionalSourceOrders.map(expect.objectContaining),
      );
      expect(comp.sourceOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Province query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const province: IProvince = { id: 11380 };
      order.province = province;

      const provinceCollection: IProvince[] = [{ id: 31741 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [province];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(
        provinceCollection,
        ...additionalProvinces.map(expect.objectContaining),
      );
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call District query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const district: IDistrict = { id: 26382 };
      order.district = district;

      const districtCollection: IDistrict[] = [{ id: 10333 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining),
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ward query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const ward: IWard = { id: 16649 };
      order.ward = ward;

      const wardCollection: IWard[] = [{ id: 3644 }];
      jest.spyOn(wardService, 'query').mockReturnValue(of(new HttpResponse({ body: wardCollection })));
      const additionalWards = [ward];
      const expectedCollection: IWard[] = [...additionalWards, ...wardCollection];
      jest.spyOn(wardService, 'addWardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(wardService.query).toHaveBeenCalled();
      expect(wardService.addWardToCollectionIfMissing).toHaveBeenCalledWith(
        wardCollection,
        ...additionalWards.map(expect.objectContaining),
      );
      expect(comp.wardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const order: IOrder = { id: 456 };
      const customer: ICustomer = { id: 18049 };
      order.customer = customer;
      const sale: ISale = { id: 28589 };
      order.sale = sale;
      const staff: IStaff = { id: 8097 };
      order.staff = staff;
      const sourceOrder: ISourceOrder = { id: 21846 };
      order.sourceOrder = sourceOrder;
      const province: IProvince = { id: 29209 };
      order.province = province;
      const district: IDistrict = { id: 27538 };
      order.district = district;
      const ward: IWard = { id: 3767 };
      order.ward = ward;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.salesSharedCollection).toContain(sale);
      expect(comp.staffSharedCollection).toContain(staff);
      expect(comp.sourceOrdersSharedCollection).toContain(sourceOrder);
      expect(comp.provincesSharedCollection).toContain(province);
      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.wardsSharedCollection).toContain(ward);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      jest.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSale', () => {
      it('Should forward to saleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(saleService, 'compareSale');
        comp.compareSale(entity, entity2);
        expect(saleService.compareSale).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStaff', () => {
      it('Should forward to staffService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(staffService, 'compareStaff');
        comp.compareStaff(entity, entity2);
        expect(staffService.compareStaff).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSourceOrder', () => {
      it('Should forward to sourceOrderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sourceOrderService, 'compareSourceOrder');
        comp.compareSourceOrder(entity, entity2);
        expect(sourceOrderService.compareSourceOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProvince', () => {
      it('Should forward to provinceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(provinceService, 'compareProvince');
        comp.compareProvince(entity, entity2);
        expect(provinceService.compareProvince).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareWard', () => {
      it('Should forward to wardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(wardService, 'compareWard');
        comp.compareWard(entity, entity2);
        expect(wardService.compareWard).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
