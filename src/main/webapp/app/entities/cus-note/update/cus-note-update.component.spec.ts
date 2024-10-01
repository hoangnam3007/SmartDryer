import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { CusNoteService } from '../service/cus-note.service';
import { ICusNote } from '../cus-note.model';
import { CusNoteFormService } from './cus-note-form.service';

import { CusNoteUpdateComponent } from './cus-note-update.component';

describe('CusNote Management Update Component', () => {
  let comp: CusNoteUpdateComponent;
  let fixture: ComponentFixture<CusNoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cusNoteFormService: CusNoteFormService;
  let cusNoteService: CusNoteService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CusNoteUpdateComponent],
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
      .overrideTemplate(CusNoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CusNoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cusNoteFormService = TestBed.inject(CusNoteFormService);
    cusNoteService = TestBed.inject(CusNoteService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const cusNote: ICusNote = { id: 456 };
      const customer: ICustomer = { id: 26950 };
      cusNote.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 1773 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cusNote });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cusNote: ICusNote = { id: 456 };
      const customer: ICustomer = { id: 16995 };
      cusNote.customer = customer;

      activatedRoute.data = of({ cusNote });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.cusNote).toEqual(cusNote);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICusNote>>();
      const cusNote = { id: 123 };
      jest.spyOn(cusNoteFormService, 'getCusNote').mockReturnValue(cusNote);
      jest.spyOn(cusNoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cusNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cusNote }));
      saveSubject.complete();

      // THEN
      expect(cusNoteFormService.getCusNote).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cusNoteService.update).toHaveBeenCalledWith(expect.objectContaining(cusNote));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICusNote>>();
      const cusNote = { id: 123 };
      jest.spyOn(cusNoteFormService, 'getCusNote').mockReturnValue({ id: null });
      jest.spyOn(cusNoteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cusNote: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cusNote }));
      saveSubject.complete();

      // THEN
      expect(cusNoteFormService.getCusNote).toHaveBeenCalled();
      expect(cusNoteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICusNote>>();
      const cusNote = { id: 123 };
      jest.spyOn(cusNoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cusNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cusNoteService.update).toHaveBeenCalled();
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
  });
});
