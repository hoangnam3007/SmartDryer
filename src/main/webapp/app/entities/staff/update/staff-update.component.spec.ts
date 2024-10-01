import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { StaffService } from '../service/staff.service';
import { IStaff } from '../staff.model';
import { StaffFormService } from './staff-form.service';

import { StaffUpdateComponent } from './staff-update.component';

describe('Staff Management Update Component', () => {
  let comp: StaffUpdateComponent;
  let fixture: ComponentFixture<StaffUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let staffFormService: StaffFormService;
  let staffService: StaffService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StaffUpdateComponent],
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
      .overrideTemplate(StaffUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StaffUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    staffFormService = TestBed.inject(StaffFormService);
    staffService = TestBed.inject(StaffService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Staff query and add missing value', () => {
      const staff: IStaff = { id: 456 };
      const staffLead: IStaff = { id: 29981 };
      staff.staffLead = staffLead;

      const staffCollection: IStaff[] = [{ id: 14524 }];
      jest.spyOn(staffService, 'query').mockReturnValue(of(new HttpResponse({ body: staffCollection })));
      const additionalStaff = [staffLead];
      const expectedCollection: IStaff[] = [...additionalStaff, ...staffCollection];
      jest.spyOn(staffService, 'addStaffToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      expect(staffService.query).toHaveBeenCalled();
      expect(staffService.addStaffToCollectionIfMissing).toHaveBeenCalledWith(
        staffCollection,
        ...additionalStaff.map(expect.objectContaining),
      );
      expect(comp.staffSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const staff: IStaff = { id: 456 };
      const staffLead: IStaff = { id: 11955 };
      staff.staffLead = staffLead;

      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      expect(comp.staffSharedCollection).toContain(staffLead);
      expect(comp.staff).toEqual(staff);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStaff>>();
      const staff = { id: 123 };
      jest.spyOn(staffFormService, 'getStaff').mockReturnValue(staff);
      jest.spyOn(staffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staff }));
      saveSubject.complete();

      // THEN
      expect(staffFormService.getStaff).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(staffService.update).toHaveBeenCalledWith(expect.objectContaining(staff));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStaff>>();
      const staff = { id: 123 };
      jest.spyOn(staffFormService, 'getStaff').mockReturnValue({ id: null });
      jest.spyOn(staffService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staff: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staff }));
      saveSubject.complete();

      // THEN
      expect(staffFormService.getStaff).toHaveBeenCalled();
      expect(staffService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStaff>>();
      const staff = { id: 123 };
      jest.spyOn(staffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(staffService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStaff', () => {
      it('Should forward to staffService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(staffService, 'compareStaff');
        comp.compareStaff(entity, entity2);
        expect(staffService.compareStaff).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
