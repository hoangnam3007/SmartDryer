import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProvince } from '../province.model';
import { ProvinceService } from '../service/province.service';
import { ProvinceFormGroup, ProvinceFormService } from './province-form.service';

@Component({
  standalone: true,
  selector: 'jhi-province-update',
  templateUrl: './province-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProvinceUpdateComponent implements OnInit {
  isSaving = false;
  province: IProvince | null = null;

  protected provinceService = inject(ProvinceService);
  protected provinceFormService = inject(ProvinceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProvinceFormGroup = this.provinceFormService.createProvinceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ province }) => {
      this.province = province;
      if (province) {
        this.updateForm(province);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const province = this.provinceFormService.getProvince(this.editForm);
    if (province.id !== null) {
      this.subscribeToSaveResponse(this.provinceService.update(province));
    } else {
      this.subscribeToSaveResponse(this.provinceService.create(province));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvince>>): void {
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

  protected updateForm(province: IProvince): void {
    this.province = province;
    this.provinceFormService.resetForm(this.editForm, province);
  }
}
