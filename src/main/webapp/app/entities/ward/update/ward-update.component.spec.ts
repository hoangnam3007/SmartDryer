import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { WardService } from '../service/ward.service';
import { IWard } from '../ward.model';
import { WardFormService } from './ward-form.service';

import { WardUpdateComponent } from './ward-update.component';

describe('Ward Management Update Component', () => {
  let comp: WardUpdateComponent;
  let fixture: ComponentFixture<WardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wardFormService: WardFormService;
  let wardService: WardService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WardUpdateComponent],
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
      .overrideTemplate(WardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wardFormService = TestBed.inject(WardFormService);
    wardService = TestBed.inject(WardService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call District query and add missing value', () => {
      const ward: IWard = { id: 456 };
      const district: IDistrict = { id: 27348 };
      ward.district = district;

      const districtCollection: IDistrict[] = [{ id: 30268 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ward });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining),
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ward: IWard = { id: 456 };
      const district: IDistrict = { id: 11205 };
      ward.district = district;

      activatedRoute.data = of({ ward });
      comp.ngOnInit();

      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.ward).toEqual(ward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWard>>();
      const ward = { id: 123 };
      jest.spyOn(wardFormService, 'getWard').mockReturnValue(ward);
      jest.spyOn(wardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ward }));
      saveSubject.complete();

      // THEN
      expect(wardFormService.getWard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(wardService.update).toHaveBeenCalledWith(expect.objectContaining(ward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWard>>();
      const ward = { id: 123 };
      jest.spyOn(wardFormService, 'getWard').mockReturnValue({ id: null });
      jest.spyOn(wardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ward }));
      saveSubject.complete();

      // THEN
      expect(wardFormService.getWard).toHaveBeenCalled();
      expect(wardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWard>>();
      const ward = { id: 123 };
      jest.spyOn(wardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
