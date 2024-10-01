import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICusNote, NewCusNote } from '../cus-note.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICusNote for edit and NewCusNoteFormGroupInput for create.
 */
type CusNoteFormGroupInput = ICusNote | PartialWithRequiredKeyOf<NewCusNote>;

type CusNoteFormDefaults = Pick<NewCusNote, 'id'>;

type CusNoteFormGroupContent = {
  id: FormControl<ICusNote['id'] | NewCusNote['id']>;
  createBy: FormControl<ICusNote['createBy']>;
  content: FormControl<ICusNote['content']>;
  createDate: FormControl<ICusNote['createDate']>;
  customer: FormControl<ICusNote['customer']>;
};

export type CusNoteFormGroup = FormGroup<CusNoteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CusNoteFormService {
  createCusNoteFormGroup(cusNote: CusNoteFormGroupInput = { id: null }): CusNoteFormGroup {
    const cusNoteRawValue = {
      ...this.getFormDefaults(),
      ...cusNote,
    };
    return new FormGroup<CusNoteFormGroupContent>({
      id: new FormControl(
        { value: cusNoteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createBy: new FormControl(cusNoteRawValue.createBy, {
        validators: [Validators.required],
      }),
      content: new FormControl(cusNoteRawValue.content, {
        validators: [Validators.required],
      }),
      createDate: new FormControl(cusNoteRawValue.createDate),
      customer: new FormControl(cusNoteRawValue.customer),
    });
  }

  getCusNote(form: CusNoteFormGroup): ICusNote | NewCusNote {
    return form.getRawValue() as ICusNote | NewCusNote;
  }

  resetForm(form: CusNoteFormGroup, cusNote: CusNoteFormGroupInput): void {
    const cusNoteRawValue = { ...this.getFormDefaults(), ...cusNote };
    form.reset(
      {
        ...cusNoteRawValue,
        id: { value: cusNoteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CusNoteFormDefaults {
    return {
      id: null,
    };
  }
}
