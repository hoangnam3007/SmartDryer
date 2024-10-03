import { Component, Input, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOrder, Order } from '../order.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'jhi-order-detail',
  templateUrl: './order-detail.component.html',
  imports: [SharedModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrderDetailComponent {
  @Input() order: IOrder | null = null;
  protected modalService = inject(NgbModal);
  private activeModal = inject(NgbActiveModal);
  private router = inject(Router);

  back(): void {
    this.activeModal.close();
  }
}
