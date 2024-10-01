import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISendSMS } from '../send-sms.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../send-sms.test-samples';

import { RestSendSMS, SendSMSService } from './send-sms.service';

const requireRestSample: RestSendSMS = {
  ...sampleWithRequiredData,
  createDate: sampleWithRequiredData.createDate?.format(DATE_FORMAT),
  sendedDate: sampleWithRequiredData.sendedDate?.format(DATE_FORMAT),
};

describe('SendSMS Service', () => {
  let service: SendSMSService;
  let httpMock: HttpTestingController;
  let expectedResult: ISendSMS | ISendSMS[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SendSMSService);
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

    it('should create a SendSMS', () => {
      const sendSMS = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sendSMS).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SendSMS', () => {
      const sendSMS = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sendSMS).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SendSMS', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SendSMS', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SendSMS', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSendSMSToCollectionIfMissing', () => {
      it('should add a SendSMS to an empty array', () => {
        const sendSMS: ISendSMS = sampleWithRequiredData;
        expectedResult = service.addSendSMSToCollectionIfMissing([], sendSMS);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sendSMS);
      });

      it('should not add a SendSMS to an array that contains it', () => {
        const sendSMS: ISendSMS = sampleWithRequiredData;
        const sendSMSCollection: ISendSMS[] = [
          {
            ...sendSMS,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSendSMSToCollectionIfMissing(sendSMSCollection, sendSMS);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SendSMS to an array that doesn't contain it", () => {
        const sendSMS: ISendSMS = sampleWithRequiredData;
        const sendSMSCollection: ISendSMS[] = [sampleWithPartialData];
        expectedResult = service.addSendSMSToCollectionIfMissing(sendSMSCollection, sendSMS);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sendSMS);
      });

      it('should add only unique SendSMS to an array', () => {
        const sendSMSArray: ISendSMS[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sendSMSCollection: ISendSMS[] = [sampleWithRequiredData];
        expectedResult = service.addSendSMSToCollectionIfMissing(sendSMSCollection, ...sendSMSArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sendSMS: ISendSMS = sampleWithRequiredData;
        const sendSMS2: ISendSMS = sampleWithPartialData;
        expectedResult = service.addSendSMSToCollectionIfMissing([], sendSMS, sendSMS2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sendSMS);
        expect(expectedResult).toContain(sendSMS2);
      });

      it('should accept null and undefined values', () => {
        const sendSMS: ISendSMS = sampleWithRequiredData;
        expectedResult = service.addSendSMSToCollectionIfMissing([], null, sendSMS, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sendSMS);
      });

      it('should return initial array if no SendSMS is added', () => {
        const sendSMSCollection: ISendSMS[] = [sampleWithRequiredData];
        expectedResult = service.addSendSMSToCollectionIfMissing(sendSMSCollection, undefined, null);
        expect(expectedResult).toEqual(sendSMSCollection);
      });
    });

    describe('compareSendSMS', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSendSMS(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSendSMS(entity1, entity2);
        const compareResult2 = service.compareSendSMS(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSendSMS(entity1, entity2);
        const compareResult2 = service.compareSendSMS(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSendSMS(entity1, entity2);
        const compareResult2 = service.compareSendSMS(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
