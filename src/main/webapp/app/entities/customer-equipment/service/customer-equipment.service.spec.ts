import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICustomerEquipment } from '../customer-equipment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../customer-equipment.test-samples';

import { CustomerEquipmentService } from './customer-equipment.service';

const requireRestSample: ICustomerEquipment = {
  ...sampleWithRequiredData,
};

describe('CustomerEquipment Service', () => {
  let service: CustomerEquipmentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerEquipment | ICustomerEquipment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerEquipmentService);
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

    it('should create a CustomerEquipment', () => {
      const customerEquipment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerEquipment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerEquipment', () => {
      const customerEquipment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerEquipment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerEquipment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerEquipment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerEquipment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerEquipmentToCollectionIfMissing', () => {
      it('should add a CustomerEquipment to an empty array', () => {
        const customerEquipment: ICustomerEquipment = sampleWithRequiredData;
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing([], customerEquipment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerEquipment);
      });

      it('should not add a CustomerEquipment to an array that contains it', () => {
        const customerEquipment: ICustomerEquipment = sampleWithRequiredData;
        const customerEquipmentCollection: ICustomerEquipment[] = [
          {
            ...customerEquipment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing(customerEquipmentCollection, customerEquipment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerEquipment to an array that doesn't contain it", () => {
        const customerEquipment: ICustomerEquipment = sampleWithRequiredData;
        const customerEquipmentCollection: ICustomerEquipment[] = [sampleWithPartialData];
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing(customerEquipmentCollection, customerEquipment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerEquipment);
      });

      it('should add only unique CustomerEquipment to an array', () => {
        const customerEquipmentArray: ICustomerEquipment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerEquipmentCollection: ICustomerEquipment[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing(customerEquipmentCollection, ...customerEquipmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerEquipment: ICustomerEquipment = sampleWithRequiredData;
        const customerEquipment2: ICustomerEquipment = sampleWithPartialData;
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing([], customerEquipment, customerEquipment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerEquipment);
        expect(expectedResult).toContain(customerEquipment2);
      });

      it('should accept null and undefined values', () => {
        const customerEquipment: ICustomerEquipment = sampleWithRequiredData;
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing([], null, customerEquipment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerEquipment);
      });

      it('should return initial array if no CustomerEquipment is added', () => {
        const customerEquipmentCollection: ICustomerEquipment[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerEquipmentToCollectionIfMissing(customerEquipmentCollection, undefined, null);
        expect(expectedResult).toEqual(customerEquipmentCollection);
      });
    });

    describe('compareCustomerEquipment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerEquipment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCustomerEquipment(entity1, entity2);
        const compareResult2 = service.compareCustomerEquipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCustomerEquipment(entity1, entity2);
        const compareResult2 = service.compareCustomerEquipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCustomerEquipment(entity1, entity2);
        const compareResult2 = service.compareCustomerEquipment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
