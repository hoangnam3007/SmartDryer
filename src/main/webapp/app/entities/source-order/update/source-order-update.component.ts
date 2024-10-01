import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISourceOrder } from '../source-order.model';
import { SourceOrderService } from '../service/source-order.service';
import { SourceOrderFormGroup, SourceOrderFormService } from './source-order-form.service';

@Component({
  standalone: true,
  selector: 'jhi-source-order-update',
  templateUrl: './source-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SourceOrderUpdateComponent implements OnInit {
  isSaving = false;
  sourceOrder: ISourceOrder | null = null;

  protected sourceOrderService = inject(SourceOrderService);
  protected sourceOrderFormService = inject(SourceOrderFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SourceOrderFormGroup = this.sourceOrderFormService.createSourceOrderFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sourceOrder }) => {
      this.sourceOrder = sourceOrder;
      if (sourceOrder) {
        this.updateForm(sourceOrder);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sourceOrder = this.sourceOrderFormService.getSourceOrder(this.editForm);
    if (sourceOrder.id !== null) {
      this.subscribeToSaveResponse(this.sourceOrderService.update(sourceOrder));
    } else {
      this.subscribeToSaveResponse(this.sourceOrderService.create(sourceOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISourceOrder>>): void {
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

  protected updateForm(sourceOrder: ISourceOrder): void {
    this.sourceOrder = sourceOrder;
    this.sourceOrderFormService.resetForm(this.editForm, sourceOrder);
  }
}
