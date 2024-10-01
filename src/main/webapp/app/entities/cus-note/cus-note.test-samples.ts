import dayjs from 'dayjs/esm';

import { ICusNote, NewCusNote } from './cus-note.model';

export const sampleWithRequiredData: ICusNote = {
  id: 24244,
  createBy: 'cheerfully not',
  content: 'insecure into honesty',
};

export const sampleWithPartialData: ICusNote = {
  id: 21065,
  createBy: 'legal considering',
  content: 'broadly bless strange',
  createDate: dayjs('2024-09-19'),
};

export const sampleWithFullData: ICusNote = {
  id: 24109,
  createBy: 'better',
  content: 'edge atop',
  createDate: dayjs('2024-09-20'),
};

export const sampleWithNewData: NewCusNote = {
  createBy: 'majestic yahoo ah',
  content: 'nearly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
