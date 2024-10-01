import { Component, Input, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { OrderService } from '../../order/service/order.service';
import { IOrder } from '../../order/order.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AssignSuccessComponent } from '../order-assigned/order-assigned.component';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'jhi-order-assign',
  templateUrl: './assignOrder.component.html',
  imports: [SharedModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, FormsModule],
})
export class AssignOrderComponent implements OnInit {
  staffs?: any[] = [];
  selectedStaffId?: number; // Holds the selected staff ID
  @Input() order!: IOrder; // Input decorator to receive `order` from the parent component

  protected orderService = inject(OrderService);
  protected modalService = inject(NgbModal);
  private activeModal = inject(NgbActiveModal);
  private router = inject(Router);

  ngOnInit(): void {
    // Fetch all staff members
    this.orderService.getAllStaff().subscribe({
      next: data => {
        this.staffs = data.filter((staff: any) => staff.isLead !== 1); // Only include staff who are not leads
      },
    });
  }

  back(): void {
    this.activeModal.close();
  }

  assignStaff(): void {
    if (this.selectedStaffId) {
      // Create an updated order object with the selected staff
      const updatedOrder = {
        ...this.order,
        staff: { id: this.selectedStaffId }, // Assign the selected staff
        status: 'ASSIGNED' as const, // Set status to ASSIGNED
      };

      // Call the service to update the order with the selected staff
      this.orderService.update(updatedOrder).subscribe({
        next: () => {
          this.showSuccessModal(); // Show the success modal after successful assignment
        },
      });
    }
  }

  showSuccessModal(): void {
    const modalRef = this.modalService.open(AssignSuccessComponent, { size: 'sm', backdrop: 'static' });
    modalRef.result.finally(() => {
      // First close the detail modal
      this.back();

      // Navigate to /order, but force route reloading to refresh the list
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/order']);
      });
    });
  }
}
