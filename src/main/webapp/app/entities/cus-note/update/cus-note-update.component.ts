import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICusNote } from '../cus-note.model';
import { CusNoteService } from '../service/cus-note.service';
import { CusNoteFormGroup, CusNoteFormService } from './cus-note-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cus-note-update',
  templateUrl: './cus-note-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CusNoteUpdateComponent implements OnInit {
  isSaving = false;
  cusNote: ICusNote | null = null;

  customersSharedCollection: ICustomer[] = [];

  protected cusNoteService = inject(CusNoteService);
  protected cusNoteFormService = inject(CusNoteFormService);
  protected customerService = inject(CustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CusNoteFormGroup = this.cusNoteFormService.createCusNoteFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cusNote }) => {
      this.cusNote = cusNote;
      if (cusNote) {
        this.updateForm(cusNote);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cusNote = this.cusNoteFormService.getCusNote(this.editForm);
    if (cusNote.id !== null) {
      this.subscribeToSaveResponse(this.cusNoteService.update(cusNote));
    } else {
      this.subscribeToSaveResponse(this.cusNoteService.create(cusNote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICusNote>>): void {
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

  protected updateForm(cusNote: ICusNote): void {
    this.cusNote = cusNote;
    this.cusNoteFormService.resetForm(this.editForm, cusNote);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      cusNote.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.cusNote?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
