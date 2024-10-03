import { Component, OnInit, inject } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';
import { OrderService } from '../../entities/order/service/order.service';
import { IOrder } from '../../entities/order/order.model';
import { SourceOrderService } from '../../entities/source-order/service/source-order.service';
import { ISourceOrder } from '../../entities/source-order/source-order.model'; // Import the correct interface
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  selector: 'jhi-plan',
  templateUrl: './plan-modal.component.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class PlanModalComponent implements OnInit {
  isSaving = false;
  order!: IOrder; // Ensure the `order` is set when passed to the component
  sources: ISourceOrder[] = []; // Correct typing for sources

  // Form to handle the order update
  editForm = new FormGroup({
    amount: new FormControl<number | null>(null, [Validators.required]),
    techNote: new FormControl('', [Validators.maxLength(500)]),
    sourceOrder: new FormControl<number | null>(null, [Validators.required]), // This expects a number (sourceOrder id)
    finishDate: new FormControl('', [Validators.required]),
  });

  private activeModal = inject(NgbActiveModal);
  private orderService = inject(OrderService);
  private sourceOrderService = inject(SourceOrderService);

  ngOnInit(): void {
    this.updateForm(this.order); // Populate the form with order details
    // Fetch source orders
    this.sourceOrderService.query().subscribe({
      next: res => {
        this.sources = res.body ?? []; // Ensure `sources` is populated from the response body
      },
    });
  }

  // Close the modal without saving
  cancel(): void {
    this.activeModal.dismiss(); // Dismiss the modal without saving
  }

  // Populate the form with order details
  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      amount: order.amount,
      techNote: order.techNote,
      sourceOrder: order.sourceOrder ? order.sourceOrder.id : null, // Assign only the id of the sourceOrder
      finishDate: order.finishDate ? dayjs(order.finishDate).format('YYYY-MM-DD') : '', // Fixed to use finishDate
    });
  }

  // Handle saving logic here
  save(): void {
    this.isSaving = true;
    const updatedOrder = this.createFromForm(); // Create the updated order object
    this.orderService.update(updatedOrder).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  private createFromForm(): IOrder {
    const finishDateValue = this.editForm.get('finishDate')!.value;
    const sourceOrderValue = this.editForm.get('sourceOrder')!.value; // Get the source order ID from the form

    return {
      ...this.order,
      amount: this.editForm.get('amount')!.value,
      techNote: this.editForm.get('techNote')!.value,
      sourceOrder: sourceOrderValue ? { id: sourceOrderValue } : null, // Wrap the ID in an object with 'id' property
      finishDate: finishDateValue ? dayjs(finishDateValue).startOf('day') : null,
    };
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.activeModal.close('save');
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
