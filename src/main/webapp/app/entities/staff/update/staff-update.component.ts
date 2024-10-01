import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStaff } from '../staff.model';
import { StaffService } from '../service/staff.service';
import { StaffFormGroup, StaffFormService } from './staff-form.service';

@Component({
  standalone: true,
  selector: 'jhi-staff-update',
  templateUrl: './staff-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StaffUpdateComponent implements OnInit {
  isSaving = false;
  staff: IStaff | null = null;

  staffSharedCollection: IStaff[] = [];

  protected staffService = inject(StaffService);
  protected staffFormService = inject(StaffFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StaffFormGroup = this.staffFormService.createStaffFormGroup();

  compareStaff = (o1: IStaff | null, o2: IStaff | null): boolean => this.staffService.compareStaff(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staff }) => {
      this.staff = staff;
      if (staff) {
        this.updateForm(staff);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const staff = this.staffFormService.getStaff(this.editForm);
    if (staff.id !== null) {
      this.subscribeToSaveResponse(this.staffService.update(staff));
    } else {
      this.subscribeToSaveResponse(this.staffService.create(staff));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaff>>): void {
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

  protected updateForm(staff: IStaff): void {
    this.staff = staff;
    this.staffFormService.resetForm(this.editForm, staff);

    this.staffSharedCollection = this.staffService.addStaffToCollectionIfMissing<IStaff>(this.staffSharedCollection, staff.staffLead);
  }

  protected loadRelationshipsOptions(): void {
    this.staffService
      .query()
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing<IStaff>(staff, this.staff?.staffLead)))
      .subscribe((staff: IStaff[]) => (this.staffSharedCollection = staff));
  }
}
