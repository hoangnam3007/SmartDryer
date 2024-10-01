import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IEquipment } from 'app/entities/equipment/equipment.model';
import { EquipmentService } from 'app/entities/equipment/service/equipment.service';
import { ICustomerEquipment } from '../customer-equipment.model';
import { CustomerEquipmentService } from '../service/customer-equipment.service';
import { CustomerEquipmentFormService } from './customer-equipment-form.service';

import { CustomerEquipmentUpdateComponent } from './customer-equipment-update.component';

describe('CustomerEquipment Management Update Component', () => {
  let comp: CustomerEquipmentUpdateComponent;
  let fixture: ComponentFixture<CustomerEquipmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerEquipmentFormService: CustomerEquipmentFormService;
  let customerEquipmentService: CustomerEquipmentService;
  let customerService: CustomerService;
  let equipmentService: EquipmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CustomerEquipmentUpdateComponent],
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
      .overrideTemplate(CustomerEquipmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerEquipmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerEquipmentFormService = TestBed.inject(CustomerEquipmentFormService);
    customerEquipmentService = TestBed.inject(CustomerEquipmentService);
    customerService = TestBed.inject(CustomerService);
    equipmentService = TestBed.inject(EquipmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const customerEquipment: ICustomerEquipment = { id: 456 };
      const customer: ICustomer = { id: 30933 };
      customerEquipment.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 4180 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerEquipment });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Equipment query and add missing value', () => {
      const customerEquipment: ICustomerEquipment = { id: 456 };
      const equipment: IEquipment = { id: 22310 };
      customerEquipment.equipment = equipment;

      const equipmentCollection: IEquipment[] = [{ id: 29985 }];
      jest.spyOn(equipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: equipmentCollection })));
      const additionalEquipment = [equipment];
      const expectedCollection: IEquipment[] = [...additionalEquipment, ...equipmentCollection];
      jest.spyOn(equipmentService, 'addEquipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerEquipment });
      comp.ngOnInit();

      expect(equipmentService.query).toHaveBeenCalled();
      expect(equipmentService.addEquipmentToCollectionIfMissing).toHaveBeenCalledWith(
        equipmentCollection,
        ...additionalEquipment.map(expect.objectContaining),
      );
      expect(comp.equipmentSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customerEquipment: ICustomerEquipment = { id: 456 };
      const customer: ICustomer = { id: 10222 };
      customerEquipment.customer = customer;
      const equipment: IEquipment = { id: 22315 };
      customerEquipment.equipment = equipment;

      activatedRoute.data = of({ customerEquipment });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.equipmentSharedCollection).toContain(equipment);
      expect(comp.customerEquipment).toEqual(customerEquipment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerEquipment>>();
      const customerEquipment = { id: 123 };
      jest.spyOn(customerEquipmentFormService, 'getCustomerEquipment').mockReturnValue(customerEquipment);
      jest.spyOn(customerEquipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerEquipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerEquipment }));
      saveSubject.complete();

      // THEN
      expect(customerEquipmentFormService.getCustomerEquipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerEquipmentService.update).toHaveBeenCalledWith(expect.objectContaining(customerEquipment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerEquipment>>();
      const customerEquipment = { id: 123 };
      jest.spyOn(customerEquipmentFormService, 'getCustomerEquipment').mockReturnValue({ id: null });
      jest.spyOn(customerEquipmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerEquipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerEquipment }));
      saveSubject.complete();

      // THEN
      expect(customerEquipmentFormService.getCustomerEquipment).toHaveBeenCalled();
      expect(customerEquipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerEquipment>>();
      const customerEquipment = { id: 123 };
      jest.spyOn(customerEquipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerEquipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerEquipmentService.update).toHaveBeenCalled();
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

    describe('compareEquipment', () => {
      it('Should forward to equipmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(equipmentService, 'compareEquipment');
        comp.compareEquipment(entity, entity2);
        expect(equipmentService.compareEquipment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
