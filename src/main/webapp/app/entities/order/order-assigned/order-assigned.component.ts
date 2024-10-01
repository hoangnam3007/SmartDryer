import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'jhi-approval-success',
  template: `
    <div class="modal-header justify-content-center">
      <h4 class="modal-title">Good job!</h4>
    </div>
    <div class="modal-body text-center">
      <div>
        <i class="fa fa-check-circle fa-3x text-success"></i>
      </div>
      <p class="mt-3">You successfully assign an Order!</p>
    </div>
    <div class="modal-footer justify-content-center">
      <button type="button" class="btn btn-success" (click)="close()">OK</button>
    </div>
  `,
  styles: [
    `
      .modal-body {
        font-size: 1.2rem;
      }
      .fa-check-circle {
        font-size: 50px;
      }
    `,
  ],
})
export class AssignSuccessComponent {
  private activeModal = inject(NgbActiveModal);

  close(): void {
    this.activeModal.close();
  }
}
