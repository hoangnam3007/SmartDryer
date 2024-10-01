import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { HistoryOrderService } from '../service/history-order.service';
import { IHistoryOrder } from '../history-order.model';
import { HistoryOrderFormGroup, HistoryOrderFormService } from './history-order-form.service';

@Component({
  standalone: true,
  selector: 'jhi-history-order-update',
  templateUrl: './history-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HistoryOrderUpdateComponent implements OnInit {
  isSaving = false;
  historyOrder: IHistoryOrder | null = null;
  orderStatusValues = Object.keys(OrderStatus);

  ordersSharedCollection: IOrder[] = [];

  protected historyOrderService = inject(HistoryOrderService);
  protected historyOrderFormService = inject(HistoryOrderFormService);
  protected orderService = inject(OrderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HistoryOrderFormGroup = this.historyOrderFormService.createHistoryOrderFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historyOrder }) => {
      this.historyOrder = historyOrder;
      if (historyOrder) {
        this.updateForm(historyOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historyOrder = this.historyOrderFormService.getHistoryOrder(this.editForm);
    if (historyOrder.id !== null) {
      this.subscribeToSaveResponse(this.historyOrderService.update(historyOrder));
    } else {
      this.subscribeToSaveResponse(this.historyOrderService.create(historyOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistoryOrder>>): void {
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

  protected updateForm(historyOrder: IHistoryOrder): void {
    this.historyOrder = historyOrder;
    this.historyOrderFormService.resetForm(this.editForm, historyOrder);

    this.ordersSharedCollection = this.orderService.addOrderToCollectionIfMissing<IOrder>(this.ordersSharedCollection, historyOrder.order);
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.historyOrder?.order)))
      .subscribe((orders: IOrder[]) => (this.ordersSharedCollection = orders));
  }
}
