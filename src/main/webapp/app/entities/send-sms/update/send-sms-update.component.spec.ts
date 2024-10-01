import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SendSMSService } from '../service/send-sms.service';
import { ISendSMS } from '../send-sms.model';
import { SendSMSFormService } from './send-sms-form.service';

import { SendSMSUpdateComponent } from './send-sms-update.component';

describe('SendSMS Management Update Component', () => {
  let comp: SendSMSUpdateComponent;
  let fixture: ComponentFixture<SendSMSUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sendSMSFormService: SendSMSFormService;
  let sendSMSService: SendSMSService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SendSMSUpdateComponent],
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
      .overrideTemplate(SendSMSUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SendSMSUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sendSMSFormService = TestBed.inject(SendSMSFormService);
    sendSMSService = TestBed.inject(SendSMSService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sendSMS: ISendSMS = { id: 456 };

      activatedRoute.data = of({ sendSMS });
      comp.ngOnInit();

      expect(comp.sendSMS).toEqual(sendSMS);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISendSMS>>();
      const sendSMS = { id: 123 };
      jest.spyOn(sendSMSFormService, 'getSendSMS').mockReturnValue(sendSMS);
      jest.spyOn(sendSMSService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sendSMS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sendSMS }));
      saveSubject.complete();

      // THEN
      expect(sendSMSFormService.getSendSMS).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sendSMSService.update).toHaveBeenCalledWith(expect.objectContaining(sendSMS));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISendSMS>>();
      const sendSMS = { id: 123 };
      jest.spyOn(sendSMSFormService, 'getSendSMS').mockReturnValue({ id: null });
      jest.spyOn(sendSMSService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sendSMS: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sendSMS }));
      saveSubject.complete();

      // THEN
      expect(sendSMSFormService.getSendSMS).toHaveBeenCalled();
      expect(sendSMSService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISendSMS>>();
      const sendSMS = { id: 123 };
      jest.spyOn(sendSMSService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sendSMS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sendSMSService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
