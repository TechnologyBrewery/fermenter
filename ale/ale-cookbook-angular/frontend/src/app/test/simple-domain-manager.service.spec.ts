import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { TestBed, inject } from '@angular/core/testing';

import { SimpleDomain } from '../shared/model/simple-domain.model';
import { Constants } from '../constants';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { GlobalErrorHandler } from '../shared/global-error-handler.service';
import { SimpleDomainManagerService } from '../generated/service/business/simple-domain-manager.service';
import { HeartbeatService } from '../generated/service/business/heartbeat.service';
import { FermenterMessage } from '../shared/model/fermenter-message.model';
import { FermenterMessages } from '../shared/model/fermenter-messages.model';

describe('Ale: Business Service Generation', () => {
  let httpTestingController: HttpTestingController;
  const constants = new Constants();
  const simpleDomainMgrUrl =
    constants.STOUT_COOKBOOK_DOMAIN_END_POINT + '/SimpleDomainManagerService';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SimpleDomainManagerService,
        GlobalErrorHandler,
        HeartbeatService
      ]
    });

    // Inject the http service and test controller for each test
    httpTestingController = TestBed.get(HttpTestingController);
  });
  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be able to support recieving primitive inputs', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const testCount = 3;

      simpleDomainManagerService
        .getSimpleDomainCount()
        .subscribe((responseCount: number) => {
          expect(responseCount).toEqual(testCount);
        });

      const req = httpTestingController.expectOne(
        simpleDomainMgrUrl + '/getSimpleDomainCount'
      );

      expect(req.request.method).toEqual('GET');

      const mockResponse = new FermenterResponse<number>();
      mockResponse.value = testCount;
      req.flush(mockResponse);
    }
  ));

  it('should be able to support providing primitive inputs', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const startPage = 3;
      const pageSize = 10;
      const testName = 'testName';

      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;

      simpleDomainManagerService
        .selectAllSimpleDomainsWithPaging(startPage, pageSize)
        .subscribe((response: Array<SimpleDomain>) => {
          expect(response.length).toEqual(1);
          expect(response[0].name).toEqual(testName);
        });

      const req = httpTestingController.expectOne(
        request =>
          request.url ===
          simpleDomainMgrUrl + '/selectAllSimpleDomainsWithPaging'
      );

      expect(req.request.method).toEqual('GET');
      expect(+req.request.params.get('startPage')).toEqual(startPage);

      const mockResponse = new FermenterResponse<Array<SimpleDomain>>();
      mockResponse.value = new Array<SimpleDomain>();
      mockResponse.value.push(testSimpleDomain);
      req.flush(mockResponse);
    }
  ));

  it('should be able to support returning a void response', inject(
    [HeartbeatService],
    (heartbeatService: HeartbeatService) => {
      const messageKey = 'heartbeat';

      heartbeatService
        .heartbeat()
        .subscribe((response: FermenterResponse<undefined>) => {
          expect(response).toBeTruthy();
          expect(response.value).toBeUndefined();
          expect(response.messages.hasErrorMessages()).toBeFalsy();
          expect(response.messages.hasMessages()).toBeTruthy();
          expect(response.messages.messages[0].key).toEqual(messageKey);
        });

      const req = httpTestingController.expectOne(
        constants.STOUT_COOKBOOK_DOMAIN_END_POINT + '/HeartbeatService/heartbeat'
      );

      expect(req.request.method).toEqual('GET');

      const mockResponse = new FermenterResponse<undefined>();
      mockResponse.messages = new FermenterMessages();
      const mockMessage = new FermenterMessage();
      mockMessage.key = messageKey;
      mockMessage.severity = 'INFO';
      mockMessage.summaryMessage = 'Server is responsive.';
      mockMessage.detailMessage = mockMessage.summaryMessage;
      mockResponse.messages.messages = [mockMessage];
      req.flush(mockResponse);
    }
  ));

  it('should be able to support providing primitive inputs', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const primitiveParam = 'Hello';

      simpleDomainManagerService
        .echoPlusWazzup(primitiveParam)
        .subscribe((response: FermenterResponse<string>) => {
          expect(response).toBeTruthy();
          expect(response.value).toEqual(primitiveParam + 'Wazzup');
        });

      const req = httpTestingController.expectOne(
        request => request.url === simpleDomainMgrUrl + '/echoPlusWazzup'
      );

      expect(req.request.method).toEqual('POST');

      const mockResponse = new FermenterResponse<string>();
      mockResponse.value = primitiveParam + 'Wazzup';
      req.flush(mockResponse);
    }
  ));

  it('should be able to support returning void', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      simpleDomainManagerService
        .returnVoid()
        .subscribe((response: FermenterResponse<undefined>) => {
          expect(response).toBeTruthy();
          expect(response.value).toBeUndefined();
        });

      const req = httpTestingController.expectOne(
        simpleDomainMgrUrl + '/returnVoid'
      );

      expect(req.request.method).toEqual('POST');

      const mockResponse = new FermenterResponse<undefined>();
      req.flush(mockResponse);
    }
  ));

  it('should be able to support providing an entity as a request parameter', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const simpleDomain = new SimpleDomain();
      const testImportantData = 'hello';
      const backendNameAddition = 'This data is really important: ';

      simpleDomainManagerService
        .someBusinessOperation(simpleDomain, testImportantData)
        .subscribe((response: FermenterResponse<SimpleDomain>) => {
          expect(response).toBeTruthy();
          expect(response.value.name).toEqual(
            backendNameAddition + testImportantData
          );
        });

      const req = httpTestingController.expectOne(
        request => request.url === simpleDomainMgrUrl + '/someBusinessOperation'
      );

      expect(req.request.method).toEqual('POST');

      const mockResponse = new FermenterResponse<SimpleDomain>();
      mockResponse.value = new SimpleDomain();
      mockResponse.value.name = backendNameAddition + testImportantData;
      req.flush(mockResponse);
    }
  ));

  it('should be able to support providing an entity as a request parameter', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const nullVariable: string = null;

      simpleDomainManagerService
        .provideNullInputFromFrontend(nullVariable)
        .subscribe((responseValue: boolean) => {
          expect(responseValue).toBeTruthy();
        });

      const req = httpTestingController.expectOne(
        request => request.url === simpleDomainMgrUrl + '/provideNullInputFromFrontend'
      );

      expect(req.request.method).toEqual('GET');
      expect(req.request.params.has('expectingThisToBeNull')).toBeFalsy();

      const mockResponse = new FermenterResponse<boolean>();
      mockResponse.value = true;
      req.flush(mockResponse);
    }
  ));

  it('should be able to support providing array of inputs', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const arrayOfSimpleDomains = new Array<SimpleDomain>();
      const numberOfSimpleDomains = Math.floor(Math.random() * 10) + 1;
      for (let i = 0; i < numberOfSimpleDomains; i++) {
        const simpleDomain = new SimpleDomain();
        arrayOfSimpleDomains.push(simpleDomain);
      }

      simpleDomainManagerService
        .countNumInputs(arrayOfSimpleDomains)
        .subscribe((response: FermenterResponse<number>) => {
          expect(response).toBeTruthy();
          expect(response.value).toEqual(numberOfSimpleDomains);
        });

      const req = httpTestingController.expectOne(
        request => request.url === simpleDomainMgrUrl + '/countNumInputs'
      );

      expect(req.request.method).toEqual('POST');

      const mockResponse = new FermenterResponse<number>();
      mockResponse.value = numberOfSimpleDomains;
      req.flush(mockResponse);
    }
  ));

  it('should be able to support returning array of results', inject(
    [SimpleDomainManagerService],
    (simpleDomainManagerService: SimpleDomainManagerService) => {
      const input = 2;
      const returnListSize = Math.floor(Math.random() * 10) + 1;

      simpleDomainManagerService
        .returnManyPrimitives(input, returnListSize)
        .subscribe((response: FermenterResponse<Array<number>>) => {
          expect(response).toBeTruthy();
          expect(response.value.length).toEqual(returnListSize);
          expect(response.value[0]).toEqual(input);
        });

      const req = httpTestingController.expectOne(
        request => request.url === simpleDomainMgrUrl + '/returnManyPrimitives'
      );

      expect(req.request.method).toEqual('POST');

      const mockResponse = new FermenterResponse<Array<number>>();
      mockResponse.value = new Array<number>();
      for (let i = 0; i < returnListSize; i++) {
        mockResponse.value.push(input);
      }
      req.flush(mockResponse);
    }
  ));
});
