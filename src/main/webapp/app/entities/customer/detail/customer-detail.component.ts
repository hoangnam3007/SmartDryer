import { Component, Input, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { Customer, ICustomer } from '../customer.model';
import { ApprovalSuccessComponent } from '../customer-approved/approval-success.component';
import { OrderService } from '../../order/service/order.service';
import { CustomerService } from '../service/customer.service';
import { IOrder, NewOrder } from '../../order/order.model';
import dayjs from 'dayjs/esm';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  imports: [SharedModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomerDetailComponent implements OnInit {
  @Input() customer: Customer | null = null;
  protected customerService = inject(CustomerService);
  protected orderService = inject(OrderService);
  protected modalService = inject(NgbModal);
  private activeModal = inject(NgbActiveModal);
  private router = inject(Router);

  ngOnInit(): void {
    if (this.customer && typeof this.customer.createDate === 'string') {
      // Convert to Dayjs if it's a string
      this.customer.createDate = dayjs(this.customer.createDate);
    }
  }

  back(): void {
    this.activeModal.close();
  }

  approve(item: Customer): void {
    const updateCustomer = {
      ...item,
      status: 'APPROVED' as const,
    };
    this.customerService.update(updateCustomer).subscribe({
      next: () => {
        const newOrder: NewOrder = {
          id: null,
          code: 'ORDER_' + String(item.id),
          createBy: item.createBy,
          createDate: item.createDate ? dayjs(item.createDate) : dayjs(),
          finishDate: null,
          status: 'NEW',
          amount: 0,
          saleNote: null,
          techNote: null,
          note: item.note,
          materialSource: null,
          cusName: item.displayName,
          cusAddress: item.address,
          cusMobile: item.mobile,
          imageURL: null,
          appointmentDate: null,
          activation: 0,
          customer: item.id ? { id: item.id } : null,
          sale: null,
          staff: null,
          province: item.province?.id ? { id: item.province.id } : null,
          district: item.district?.id ? { id: item.district.id } : null,
          ward: item.ward?.id ? { id: item.ward.id } : null,
        };

        this.orderService.create(newOrder).subscribe({
          next: () => {
            this.showSuccessModal();
          },
        });
      },
    });
  }

  showSuccessModal(): void {
    const modalRef = this.modalService.open(ApprovalSuccessComponent, { size: 'sm', backdrop: 'static' });
    modalRef.result.finally(() => {
      // First close the detail modal
      this.back();

      // Navigate to /customer, but force route reloading
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/customer']); // Navigate back and force the list to reload
      });
    });
  }
}
