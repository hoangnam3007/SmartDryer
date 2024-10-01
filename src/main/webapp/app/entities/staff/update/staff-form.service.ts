import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStaff, NewStaff } from '../staff.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStaff for edit and NewStaffFormGroupInput for create.
 */
type StaffFormGroupInput = IStaff | PartialWithRequiredKeyOf<NewStaff>;

type StaffFormDefaults = Pick<NewStaff, 'id'>;

type StaffFormGroupContent = {
  id: FormControl<IStaff['id'] | NewStaff['id']>;
  userName: FormControl<IStaff['userName']>;
  fullName: FormControl<IStaff['fullName']>;
  mobile: FormControl<IStaff['mobile']>;
  email: FormControl<IStaff['email']>;
  note: FormControl<IStaff['note']>;
  createDate: FormControl<IStaff['createDate']>;
  modifiedDate: FormControl<IStaff['modifiedDate']>;
  isLead: FormControl<IStaff['isLead']>;
  imageURL: FormControl<IStaff['imageURL']>;
  staffLead: FormControl<IStaff['staffLead']>;
};

export type StaffFormGroup = FormGroup<StaffFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StaffFormService {
  createStaffFormGroup(staff: StaffFormGroupInput = { id: null }): StaffFormGroup {
    const staffRawValue = {
      ...this.getFormDefaults(),
      ...staff,
    };
    return new FormGroup<StaffFormGroupContent>({
      id: new FormControl(
        { value: staffRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userName: new FormControl(staffRawValue.userName, {
        validators: [Validators.required],
      }),
      fullName: new FormControl(staffRawValue.fullName, {
        validators: [Validators.required],
      }),
      mobile: new FormControl(staffRawValue.mobile),
      email: new FormControl(staffRawValue.email),
      note: new FormControl(staffRawValue.note),
      createDate: new FormControl(staffRawValue.createDate),
      modifiedDate: new FormControl(staffRawValue.modifiedDate),
      isLead: new FormControl(staffRawValue.isLead),
      imageURL: new FormControl(staffRawValue.imageURL),
      staffLead: new FormControl(staffRawValue.staffLead),
    });
  }

  getStaff(form: StaffFormGroup): IStaff | NewStaff {
    return form.getRawValue() as IStaff | NewStaff;
  }

  resetForm(form: StaffFormGroup, staff: StaffFormGroupInput): void {
    const staffRawValue = { ...this.getFormDefaults(), ...staff };
    form.reset(
      {
        ...staffRawValue,
        id: { value: staffRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StaffFormDefaults {
    return {
      id: null,
    };
  }
}
