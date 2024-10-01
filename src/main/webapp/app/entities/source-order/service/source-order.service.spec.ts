import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISourceOrder } from '../source-order.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../source-order.test-samples';

import { SourceOrderService } from './source-order.service';

const requireRestSample: ISourceOrder = {
  ...sampleWithRequiredData,
};

describe('SourceOrder Service', () => {
  let service: SourceOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: ISourceOrder | ISourceOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SourceOrderService);
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

    it('should create a SourceOrder', () => {
      const sourceOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sourceOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SourceOrder', () => {
      const sourceOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sourceOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SourceOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SourceOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SourceOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSourceOrderToCollectionIfMissing', () => {
      it('should add a SourceOrder to an empty array', () => {
        const sourceOrder: ISourceOrder = sampleWithRequiredData;
        expectedResult = service.addSourceOrderToCollectionIfMissing([], sourceOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sourceOrder);
      });

      it('should not add a SourceOrder to an array that contains it', () => {
        const sourceOrder: ISourceOrder = sampleWithRequiredData;
        const sourceOrderCollection: ISourceOrder[] = [
          {
            ...sourceOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSourceOrderToCollectionIfMissing(sourceOrderCollection, sourceOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SourceOrder to an array that doesn't contain it", () => {
        const sourceOrder: ISourceOrder = sampleWithRequiredData;
        const sourceOrderCollection: ISourceOrder[] = [sampleWithPartialData];
        expectedResult = service.addSourceOrderToCollectionIfMissing(sourceOrderCollection, sourceOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sourceOrder);
      });

      it('should add only unique SourceOrder to an array', () => {
        const sourceOrderArray: ISourceOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sourceOrderCollection: ISourceOrder[] = [sampleWithRequiredData];
        expectedResult = service.addSourceOrderToCollectionIfMissing(sourceOrderCollection, ...sourceOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sourceOrder: ISourceOrder = sampleWithRequiredData;
        const sourceOrder2: ISourceOrder = sampleWithPartialData;
        expectedResult = service.addSourceOrderToCollectionIfMissing([], sourceOrder, sourceOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sourceOrder);
        expect(expectedResult).toContain(sourceOrder2);
      });

      it('should accept null and undefined values', () => {
        const sourceOrder: ISourceOrder = sampleWithRequiredData;
        expectedResult = service.addSourceOrderToCollectionIfMissing([], null, sourceOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sourceOrder);
      });

      it('should return initial array if no SourceOrder is added', () => {
        const sourceOrderCollection: ISourceOrder[] = [sampleWithRequiredData];
        expectedResult = service.addSourceOrderToCollectionIfMissing(sourceOrderCollection, undefined, null);
        expect(expectedResult).toEqual(sourceOrderCollection);
      });
    });

    describe('compareSourceOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSourceOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSourceOrder(entity1, entity2);
        const compareResult2 = service.compareSourceOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSourceOrder(entity1, entity2);
        const compareResult2 = service.compareSourceOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSourceOrder(entity1, entity2);
        const compareResult2 = service.compareSourceOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
