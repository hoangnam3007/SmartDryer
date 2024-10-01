import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { HistoryOrderService } from '../service/history-order.service';
import { IHistoryOrder } from '../history-order.model';
import { HistoryOrderFormService } from './history-order-form.service';

import { HistoryOrderUpdateComponent } from './history-order-update.component';

describe('HistoryOrder Management Update Component', () => {
  let comp: HistoryOrderUpdateComponent;
  let fixture: ComponentFixture<HistoryOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historyOrderFormService: HistoryOrderFormService;
  let historyOrderService: HistoryOrderService;
  let orderService: OrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HistoryOrderUpdateComponent],
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
      .overrideTemplate(HistoryOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoryOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historyOrderFormService = TestBed.inject(HistoryOrderFormService);
    historyOrderService = TestBed.inject(HistoryOrderService);
    orderService = TestBed.inject(OrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Order query and add missing value', () => {
      const historyOrder: IHistoryOrder = { id: 456 };
      const order: IOrder = { id: 14202 };
      historyOrder.order = order;

      const orderCollection: IOrder[] = [{ id: 18708 }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historyOrder });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(expect.objectContaining),
      );
      expect(comp.ordersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const historyOrder: IHistoryOrder = { id: 456 };
      const order: IOrder = { id: 7649 };
      historyOrder.order = order;

      activatedRoute.data = of({ historyOrder });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection).toContain(order);
      expect(comp.historyOrder).toEqual(historyOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoryOrder>>();
      const historyOrder = { id: 123 };
      jest.spyOn(historyOrderFormService, 'getHistoryOrder').mockReturnValue(historyOrder);
      jest.spyOn(historyOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historyOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historyOrder }));
      saveSubject.complete();

      // THEN
      expect(historyOrderFormService.getHistoryOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historyOrderService.update).toHaveBeenCalledWith(expect.objectContaining(historyOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoryOrder>>();
      const historyOrder = { id: 123 };
      jest.spyOn(historyOrderFormService, 'getHistoryOrder').mockReturnValue({ id: null });
      jest.spyOn(historyOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historyOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historyOrder }));
      saveSubject.complete();

      // THEN
      expect(historyOrderFormService.getHistoryOrder).toHaveBeenCalled();
      expect(historyOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoryOrder>>();
      const historyOrder = { id: 123 };
      jest.spyOn(historyOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historyOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historyOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrder', () => {
      it('Should forward to orderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
