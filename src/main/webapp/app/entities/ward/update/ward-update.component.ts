import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { IWard } from '../ward.model';
import { WardService } from '../service/ward.service';
import { WardFormGroup, WardFormService } from './ward-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ward-update',
  templateUrl: './ward-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WardUpdateComponent implements OnInit {
  isSaving = false;
  ward: IWard | null = null;

  districtsSharedCollection: IDistrict[] = [];

  protected wardService = inject(WardService);
  protected wardFormService = inject(WardFormService);
  protected districtService = inject(DistrictService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WardFormGroup = this.wardFormService.createWardFormGroup();

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ward }) => {
      this.ward = ward;
      if (ward) {
        this.updateForm(ward);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ward = this.wardFormService.getWard(this.editForm);
    if (ward.id !== null) {
      this.subscribeToSaveResponse(this.wardService.update(ward));
    } else {
      this.subscribeToSaveResponse(this.wardService.create(ward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWard>>): void {
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

  protected updateForm(ward: IWard): void {
    this.ward = ward;
    this.wardFormService.resetForm(this.editForm, ward);

    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      ward.district,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) => this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.ward?.district)),
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
  }
}
