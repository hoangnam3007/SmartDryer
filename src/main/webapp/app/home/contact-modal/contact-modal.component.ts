import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm'; // Import dayjs
import { OrderService } from '../../entities/order/service/order.service';
import { IOrder } from '../../entities/order/order.model';
import SharedModule from 'app/shared/shared.module'; // Assuming you are using SharedModule for styling or utilities

@Component({
  standalone: true,
  selector: 'jhi-contact',
  templateUrl: './contact-modal.component.html',
  imports: [SharedModule, ReactiveFormsModule], // Include ReactiveFormsModule here
})
export class ContactModalComponent implements OnInit {
  isSaving = false;
  order!: IOrder; // The order object passed to this component

  // Form to edit the order
  editForm = new FormGroup({
    cusAddress: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    techNote: new FormControl('', Validators.maxLength(500)),
    appointmentDate: new FormControl('', [Validators.required]),
  });

  // Injecting the necessary services
  private activeModal = inject(NgbActiveModal);
  private orderService = inject(OrderService);

  ngOnInit(): void {
    this.updateForm(this.order); // Initialize form with existing data
  }

  // Populate the form with order details
  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      cusAddress: order.cusAddress,
      techNote: order.techNote,
      appointmentDate: order.appointmentDate ? dayjs(order.appointmentDate).format('YYYY-MM-DD') : '',
    });
  }

  // Update the order details
  update(): void {
    this.isSaving = true;
    const updatedOrder = this.createFromForm();
    this.orderService.update(updatedOrder).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  // Close the modal without saving
  cancel(): void {
    this.activeModal.close(); // Dismiss the modal without saving
  }

  // Create the order object from the form values
  private createFromForm(): IOrder {
    const appointmentDateValue = this.editForm.get('appointmentDate')!.value;

    // Convert appointmentDate from string to Dayjs
    const appointmentDate = appointmentDateValue
      ? dayjs(appointmentDateValue).startOf('day') // Convert Date string to Dayjs
      : null;

    return {
      ...this.order,
      status: 'HOLD' as const,
      cusAddress: this.editForm.get('cusAddress')!.value,
      techNote: this.editForm.get('techNote')!.value,
      appointmentDate, // Set the converted Dayjs object
    };
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.activeModal.close('save'); // Close the modal and indicate success
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
