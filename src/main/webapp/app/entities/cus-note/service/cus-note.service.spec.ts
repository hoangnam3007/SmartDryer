import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICusNote } from '../cus-note.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cus-note.test-samples';

import { CusNoteService, RestCusNote } from './cus-note.service';

const requireRestSample: RestCusNote = {
  ...sampleWithRequiredData,
  createDate: sampleWithRequiredData.createDate?.format(DATE_FORMAT),
};

describe('CusNote Service', () => {
  let service: CusNoteService;
  let httpMock: HttpTestingController;
  let expectedResult: ICusNote | ICusNote[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CusNoteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CusNote', () => {
      const cusNote = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cusNote).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CusNote', () => {
      const cusNote = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cusNote).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CusNote', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CusNote', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CusNote', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCusNoteToCollectionIfMissing', () => {
      it('should add a CusNote to an empty array', () => {
        const cusNote: ICusNote = sampleWithRequiredData;
        expectedResult = service.addCusNoteToCollectionIfMissing([], cusNote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cusNote);
      });

      it('should not add a CusNote to an array that contains it', () => {
        const cusNote: ICusNote = sampleWithRequiredData;
        const cusNoteCollection: ICusNote[] = [
          {
            ...cusNote,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCusNoteToCollectionIfMissing(cusNoteCollection, cusNote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CusNote to an array that doesn't contain it", () => {
        const cusNote: ICusNote = sampleWithRequiredData;
        const cusNoteCollection: ICusNote[] = [sampleWithPartialData];
        expectedResult = service.addCusNoteToCollectionIfMissing(cusNoteCollection, cusNote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cusNote);
      });

      it('should add only unique CusNote to an array', () => {
        const cusNoteArray: ICusNote[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cusNoteCollection: ICusNote[] = [sampleWithRequiredData];
        expectedResult = service.addCusNoteToCollectionIfMissing(cusNoteCollection, ...cusNoteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cusNote: ICusNote = sampleWithRequiredData;
        const cusNote2: ICusNote = sampleWithPartialData;
        expectedResult = service.addCusNoteToCollectionIfMissing([], cusNote, cusNote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cusNote);
        expect(expectedResult).toContain(cusNote2);
      });

      it('should accept null and undefined values', () => {
        const cusNote: ICusNote = sampleWithRequiredData;
        expectedResult = service.addCusNoteToCollectionIfMissing([], null, cusNote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cusNote);
      });

      it('should return initial array if no CusNote is added', () => {
        const cusNoteCollection: ICusNote[] = [sampleWithRequiredData];
        expectedResult = service.addCusNoteToCollectionIfMissing(cusNoteCollection, undefined, null);
        expect(expectedResult).toEqual(cusNoteCollection);
      });
    });

    describe('compareCusNote', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCusNote(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCusNote(entity1, entity2);
        const compareResult2 = service.compareCusNote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCusNote(entity1, entity2);
        const compareResult2 = service.compareCusNote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCusNote(entity1, entity2);
        const compareResult2 = service.compareCusNote(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
