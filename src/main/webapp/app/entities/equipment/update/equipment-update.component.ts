import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';
import { EquipmentFormGroup, EquipmentFormService } from './equipment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-equipment-update',
  templateUrl: './equipment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EquipmentUpdateComponent implements OnInit {
  isSaving = false;
  equipment: IEquipment | null = null;

  protected equipmentService = inject(EquipmentService);
  protected equipmentFormService = inject(EquipmentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EquipmentFormGroup = this.equipmentFormService.createEquipmentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipment }) => {
      this.equipment = equipment;
      if (equipment) {
        this.updateForm(equipment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const equipment = this.equipmentFormService.getEquipment(this.editForm);
    if (equipment.id !== null) {
      this.subscribeToSaveResponse(this.equipmentService.update(equipment));
    } else {
      this.subscribeToSaveResponse(this.equipmentService.create(equipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(equipment: IEquipment): void {
    this.equipment = equipment;
    this.equipmentFormService.resetForm(this.editForm, equipment);
  }
}
