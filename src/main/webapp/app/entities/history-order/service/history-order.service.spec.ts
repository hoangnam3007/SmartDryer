import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHistoryOrder } from '../history-order.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../history-order.test-samples';

import { HistoryOrderService, RestHistoryOrder } from './history-order.service';

const requireRestSample: RestHistoryOrder = {
  ...sampleWithRequiredData,
  modifiedDate: sampleWithRequiredData.modifiedDate?.format(DATE_FORMAT),
};

describe('HistoryOrder Service', () => {
  let service: HistoryOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistoryOrder | IHistoryOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HistoryOrderService);
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

    it('should create a HistoryOrder', () => {
      const historyOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(historyOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HistoryOrder', () => {
      const historyOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(historyOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HistoryOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HistoryOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HistoryOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHistoryOrderToCollectionIfMissing', () => {
      it('should add a HistoryOrder to an empty array', () => {
        const historyOrder: IHistoryOrder = sampleWithRequiredData;
        expectedResult = service.addHistoryOrderToCollectionIfMissing([], historyOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historyOrder);
      });

      it('should not add a HistoryOrder to an array that contains it', () => {
        const historyOrder: IHistoryOrder = sampleWithRequiredData;
        const historyOrderCollection: IHistoryOrder[] = [
          {
            ...historyOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoryOrderToCollectionIfMissing(historyOrderCollection, historyOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HistoryOrder to an array that doesn't contain it", () => {
        const historyOrder: IHistoryOrder = sampleWithRequiredData;
        const historyOrderCollection: IHistoryOrder[] = [sampleWithPartialData];
        expectedResult = service.addHistoryOrderToCollectionIfMissing(historyOrderCollection, historyOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historyOrder);
      });

      it('should add only unique HistoryOrder to an array', () => {
        const historyOrderArray: IHistoryOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historyOrderCollection: IHistoryOrder[] = [sampleWithRequiredData];
        expectedResult = service.addHistoryOrderToCollectionIfMissing(historyOrderCollection, ...historyOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historyOrder: IHistoryOrder = sampleWithRequiredData;
        const historyOrder2: IHistoryOrder = sampleWithPartialData;
        expectedResult = service.addHistoryOrderToCollectionIfMissing([], historyOrder, historyOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historyOrder);
        expect(expectedResult).toContain(historyOrder2);
      });

      it('should accept null and undefined values', () => {
        const historyOrder: IHistoryOrder = sampleWithRequiredData;
        expectedResult = service.addHistoryOrderToCollectionIfMissing([], null, historyOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historyOrder);
      });

      it('should return initial array if no HistoryOrder is added', () => {
        const historyOrderCollection: IHistoryOrder[] = [sampleWithRequiredData];
        expectedResult = service.addHistoryOrderToCollectionIfMissing(historyOrderCollection, undefined, null);
        expect(expectedResult).toEqual(historyOrderCollection);
      });
    });

    describe('compareHistoryOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistoryOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHistoryOrder(entity1, entity2);
        const compareResult2 = service.compareHistoryOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHistoryOrder(entity1, entity2);
        const compareResult2 = service.compareHistoryOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHistoryOrder(entity1, entity2);
        const compareResult2 = service.compareHistoryOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
