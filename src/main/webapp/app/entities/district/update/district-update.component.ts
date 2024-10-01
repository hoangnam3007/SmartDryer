import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { IDistrict } from '../district.model';
import { DistrictService } from '../service/district.service';
import { DistrictFormGroup, DistrictFormService } from './district-form.service';

@Component({
  standalone: true,
  selector: 'jhi-district-update',
  templateUrl: './district-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DistrictUpdateComponent implements OnInit {
  isSaving = false;
  district: IDistrict | null = null;

  provincesSharedCollection: IProvince[] = [];

  protected districtService = inject(DistrictService);
  protected districtFormService = inject(DistrictFormService);
  protected provinceService = inject(ProvinceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DistrictFormGroup = this.districtFormService.createDistrictFormGroup();

  compareProvince = (o1: IProvince | null, o2: IProvince | null): boolean => this.provinceService.compareProvince(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ district }) => {
      this.district = district;
      if (district) {
        this.updateForm(district);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const district = this.districtFormService.getDistrict(this.editForm);
    if (district.id !== null) {
      this.subscribeToSaveResponse(this.districtService.update(district));
    } else {
      this.subscribeToSaveResponse(this.districtService.create(district));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDistrict>>): void {
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

  protected updateForm(district: IDistrict): void {
    this.district = district;
    this.districtFormService.resetForm(this.editForm, district);

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing<IProvince>(
      this.provincesSharedCollection,
      district.province,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing<IProvince>(provinces, this.district?.province),
        ),
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }
}
