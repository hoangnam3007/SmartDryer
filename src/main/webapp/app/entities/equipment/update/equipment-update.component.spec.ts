import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EquipmentService } from '../service/equipment.service';
import { IEquipment } from '../equipment.model';
import { EquipmentFormService } from './equipment-form.service';

import { EquipmentUpdateComponent } from './equipment-update.component';

describe('Equipment Management Update Component', () => {
  let comp: EquipmentUpdateComponent;
  let fixture: ComponentFixture<EquipmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let equipmentFormService: EquipmentFormService;
  let equipmentService: EquipmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EquipmentUpdateComponent],
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
      .overrideTemplate(EquipmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EquipmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    equipmentFormService = TestBed.inject(EquipmentFormService);
    equipmentService = TestBed.inject(EquipmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const equipment: IEquipment = { id: 456 };

      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      expect(comp.equipment).toEqual(equipment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquipment>>();
      const equipment = { id: 123 };
      jest.spyOn(equipmentFormService, 'getEquipment').mockReturnValue(equipment);
      jest.spyOn(equipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equipment }));
      saveSubject.complete();

      // THEN
      expect(equipmentFormService.getEquipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(equipmentService.update).toHaveBeenCalledWith(expect.objectContaining(equipment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquipment>>();
      const equipment = { id: 123 };
      jest.spyOn(equipmentFormService, 'getEquipment').mockReturnValue({ id: null });
      jest.spyOn(equipmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equipment }));
      saveSubject.complete();

      // THEN
      expect(equipmentFormService.getEquipment).toHaveBeenCalled();
      expect(equipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquipment>>();
      const equipment = { id: 123 };
      jest.spyOn(equipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(equipmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
