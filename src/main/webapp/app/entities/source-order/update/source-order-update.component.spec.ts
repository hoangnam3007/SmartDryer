import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SourceOrderService } from '../service/source-order.service';
import { ISourceOrder } from '../source-order.model';
import { SourceOrderFormService } from './source-order-form.service';

import { SourceOrderUpdateComponent } from './source-order-update.component';

describe('SourceOrder Management Update Component', () => {
  let comp: SourceOrderUpdateComponent;
  let fixture: ComponentFixture<SourceOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sourceOrderFormService: SourceOrderFormService;
  let sourceOrderService: SourceOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SourceOrderUpdateComponent],
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
      .overrideTemplate(SourceOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SourceOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sourceOrderFormService = TestBed.inject(SourceOrderFormService);
    sourceOrderService = TestBed.inject(SourceOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sourceOrder: ISourceOrder = { id: 456 };

      activatedRoute.data = of({ sourceOrder });
      comp.ngOnInit();

      expect(comp.sourceOrder).toEqual(sourceOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISourceOrder>>();
      const sourceOrder = { id: 123 };
      jest.spyOn(sourceOrderFormService, 'getSourceOrder').mockReturnValue(sourceOrder);
      jest.spyOn(sourceOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sourceOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sourceOrder }));
      saveSubject.complete();

      // THEN
      expect(sourceOrderFormService.getSourceOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sourceOrderService.update).toHaveBeenCalledWith(expect.objectContaining(sourceOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISourceOrder>>();
      const sourceOrder = { id: 123 };
      jest.spyOn(sourceOrderFormService, 'getSourceOrder').mockReturnValue({ id: null });
      jest.spyOn(sourceOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sourceOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sourceOrder }));
      saveSubject.complete();

      // THEN
      expect(sourceOrderFormService.getSourceOrder).toHaveBeenCalled();
      expect(sourceOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISourceOrder>>();
      const sourceOrder = { id: 123 };
      jest.spyOn(sourceOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sourceOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sourceOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
