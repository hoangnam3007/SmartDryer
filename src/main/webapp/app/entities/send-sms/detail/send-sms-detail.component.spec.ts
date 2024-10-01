import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SendSMSDetailComponent } from './send-sms-detail.component';

describe('SendSMS Management Detail Component', () => {
  let comp: SendSMSDetailComponent;
  let fixture: ComponentFixture<SendSMSDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SendSMSDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./send-sms-detail.component').then(m => m.SendSMSDetailComponent),
              resolve: { sendSMS: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SendSMSDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SendSMSDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sendSMS on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SendSMSDetailComponent);

      // THEN
      expect(instance.sendSMS()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
