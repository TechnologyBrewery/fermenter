// Http testing module and mocking controller
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

// Other imports
import { LocalDomain } from '../shared/model/local-domain.model';
import { LocalDomainMaintenanceService } from '../generated/service/maintenance/local-domain-maintenance.service';
import { TestBed, inject } from '@angular/core/testing';
import { Constants } from '../constants';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { GlobalErrorHandler } from '../shared/global-error-handler.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { PageWrapper } from '../shared/model/page-wrapper.model';


describe('Ale Reference Domain Maintenance Service', () => {
  let httpTestingController: HttpTestingController;
  const constants = new Constants();
  const localDomainMaintUrl =
    constants.STOUT_COOKBOOK_REFERENCING_DOMAIN_END_POINT + '/LocalDomain';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LocalDomainMaintenanceService, GlobalErrorHandler]
    });

   // Inject the http service and test controller for each test
   httpTestingController = TestBed.get(HttpTestingController);
  });
  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be able to GET a reference domain object', inject(
    [LocalDomainMaintenanceService],
    (localDomainService: LocalDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testLocalDomain = new LocalDomain();
      testLocalDomain.name = testName;
      testLocalDomain.id = testId;

      localDomainService
        .get(testId)
        .subscribe(localDomain => {
          expect(localDomain).toBeTruthy();
          expect(localDomain.name).toEqual(testName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        localDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a GET.
      expect(req.request.method).toEqual('GET');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<LocalDomain>();
      mockResponse.value = testLocalDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to GET a reference domain object with an external transient reference', inject(
    [LocalDomainMaintenanceService],
    (localDomainService: LocalDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const referenceName = 'Reference Name';
      const externalTransientReference: any = new Object();
      externalTransientReference.name = referenceName;

      localDomainService
        .get(testId)
        .subscribe((localDomain: LocalDomain) => {
          expect(localDomain).toBeTruthy();
          expect(localDomain.name).toEqual(testName);
          expect(localDomain.externalTransientReference.name).toEqual(referenceName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        localDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a GET.
      expect(req.request.method).toEqual('GET');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<LocalDomain>();
      const testLocalDomain = new LocalDomain();
      testLocalDomain.name = testName;
      testLocalDomain.id = testId;
      testLocalDomain.externalTransientReference = externalTransientReference;

      mockResponse.value = testLocalDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to PUT a reference domain object', inject(
    [LocalDomainMaintenanceService],
    (localDomainService: LocalDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const referenceName = 'Reference Name';
      const externalTransientReference: any = new Object();
      externalTransientReference.name = referenceName;
      const testLocalDomain = new LocalDomain();
      testLocalDomain.name = testName;
      testLocalDomain.id = testId;
      testLocalDomain.externalTransientReference = externalTransientReference;


      localDomainService
        .put(testId, testLocalDomain)
        .subscribe((response: FermenterResponse<LocalDomain>) => {
          expect(response).toBeTruthy();
          expect(response.value.name).toEqual(testName);
          expect(response.value.externalTransientReference.name).toEqual(referenceName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        localDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('PUT');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<LocalDomain>();
      mockResponse.value = testLocalDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to POST a reference domain object', inject(
    [LocalDomainMaintenanceService],
    (localDomainService: LocalDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const referenceName = 'Reference Name';
      const externalTransientReference: any = new Object();
      externalTransientReference.name = referenceName;
      const testLocalDomain = new LocalDomain();
      testLocalDomain.name = testName;
      testLocalDomain.id = testId;
      testLocalDomain.externalTransientReference = externalTransientReference;

      localDomainService
        .post(testLocalDomain)
        .subscribe((response: FermenterResponse<LocalDomain>) => {
          expect(response).toBeTruthy();
          expect(response.value.name).toEqual(testName);
          expect(response.value.externalTransientReference.name).toEqual(referenceName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        localDomainMaintUrl
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('POST');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<LocalDomain>();
      mockResponse.value = testLocalDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to DELETE a reference domain object', inject(
    [LocalDomainMaintenanceService],
    (localDomainService: LocalDomainMaintenanceService) => {
      const testId = 'Test Id';

      localDomainService
        .delete(testId)
        .subscribe((response: FermenterResponse<{}>) => {
          expect(response).toBeTruthy();
          expect(response.messages.hasMessages()).toBeFalsy();
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        localDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('DELETE');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<{}>();
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to FIND a reference domain object by example', inject(
    [LocalDomainMaintenanceService],
    (localDomainService: LocalDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testId2 = 'Test Id 2';
      const localDomainProbe = new LocalDomain();
      localDomainProbe.name = testName;
      const sortWrapper = new SortWrapper('id');
      const findByExampleCriteria = new FindByExampleCriteria<LocalDomain>(
        sortWrapper,
        localDomainProbe
      );

      localDomainService
        .findByExample(findByExampleCriteria)
        .subscribe((page: PageWrapper<LocalDomain>) => {
          expect(page).toBeTruthy();
          expect(page.totalResults).toEqual(2);
          expect(page.content[0].id).toEqual(testId);
          expect(page.content[1].id).toEqual(testId2);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        localDomainMaintUrl + '/findByExample'
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('POST');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const testLocalDomain = new LocalDomain();
      testLocalDomain.name = testName;
      testLocalDomain.id = testId;

      const testLocalDomain2 = new LocalDomain();
      testLocalDomain2.name = testName;
      testLocalDomain2.id = testId2;

      const pageWrapper = new PageWrapper<LocalDomain>();
      pageWrapper.content = [testLocalDomain, testLocalDomain2];
      pageWrapper.first = true;
      pageWrapper.last = true;
      pageWrapper.itemsPerPage = constants.DEFAULT_PAGE_SIZE;
      pageWrapper.startPage = 0;
      pageWrapper.numberOfElements = pageWrapper.content.length;
      pageWrapper.totalPages = 1;
      pageWrapper.totalResults = pageWrapper.content.length;

      const mockResponse = new FermenterResponse<PageWrapper<LocalDomain>>();
      mockResponse.value = pageWrapper;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

});
