import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IEquipment } from 'app/entities/equipment/equipment.model';
import { EquipmentService } from 'app/entities/equipment/service/equipment.service';
import { CustomerEquipmentService } from '../service/customer-equipment.service';
import { ICustomerEquipment } from '../customer-equipment.model';
import { CustomerEquipmentFormGroup, CustomerEquipmentFormService } from './customer-equipment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customer-equipment-update',
  templateUrl: './customer-equipment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomerEquipmentUpdateComponent implements OnInit {
  isSaving = false;
  customerEquipment: ICustomerEquipment | null = null;

  customersSharedCollection: ICustomer[] = [];
  equipmentSharedCollection: IEquipment[] = [];

  protected customerEquipmentService = inject(CustomerEquipmentService);
  protected customerEquipmentFormService = inject(CustomerEquipmentFormService);
  protected customerService = inject(CustomerService);
  protected equipmentService = inject(EquipmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerEquipmentFormGroup = this.customerEquipmentFormService.createCustomerEquipmentFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareEquipment = (o1: IEquipment | null, o2: IEquipment | null): boolean => this.equipmentService.compareEquipment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerEquipment }) => {
      this.customerEquipment = customerEquipment;
      if (customerEquipment) {
        this.updateForm(customerEquipment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerEquipment = this.customerEquipmentFormService.getCustomerEquipment(this.editForm);
    if (customerEquipment.id !== null) {
      this.subscribeToSaveResponse(this.customerEquipmentService.update(customerEquipment));
    } else {
      this.subscribeToSaveResponse(this.customerEquipmentService.create(customerEquipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerEquipment>>): void {
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

  protected updateForm(customerEquipment: ICustomerEquipment): void {
    this.customerEquipment = customerEquipment;
    this.customerEquipmentFormService.resetForm(this.editForm, customerEquipment);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      customerEquipment.customer,
    );
    this.equipmentSharedCollection = this.equipmentService.addEquipmentToCollectionIfMissing<IEquipment>(
      this.equipmentSharedCollection,
      customerEquipment.equipment,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.customerEquipment?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.equipmentService
      .query()
      .pipe(map((res: HttpResponse<IEquipment[]>) => res.body ?? []))
      .pipe(
        map((equipment: IEquipment[]) =>
          this.equipmentService.addEquipmentToCollectionIfMissing<IEquipment>(equipment, this.customerEquipment?.equipment),
        ),
      )
      .subscribe((equipment: IEquipment[]) => (this.equipmentSharedCollection = equipment));
  }
}
