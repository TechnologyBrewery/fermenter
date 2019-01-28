// Http testing module and mocking controller
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

// Other imports
import { TestBed, inject } from '@angular/core/testing';

import { SimpleDomain } from '../shared/model/simple-domain.model';
import { Constants } from '../constants';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { GlobalErrorHandler } from '../shared/global-error-handler.service';
import { SimpleDomainMaintenanceService } from '../generated/service/simple-domain-maintenance.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { PageWrapper } from '../shared/model/page-wrapper.model';

const testUrl = '/SimpleDomain';

describe('Ale Simple Domain Maintenance Service', () => {
  let httpTestingController: HttpTestingController;
  const constants = new Constants();

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SimpleDomainMaintenanceService, GlobalErrorHandler]
    });

    // Inject the http service and test controller for each test
    httpTestingController = TestBed.get(HttpTestingController);
  });
  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be able to GET a simple domain', inject(
    [SimpleDomainMaintenanceService],
    (simpleDomainService: SimpleDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      simpleDomainService
        .get(testId)
        .subscribe((simpleDomain: SimpleDomain) => {
          expect(simpleDomain).toBeTruthy();
          expect(simpleDomain.name).toEqual(testName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        constants.stoutCookbookDomainEndPoint + testUrl + '/' + testId
      );

      // Assert that the request is a GET.
      expect(req.request.method).toEqual('GET');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<SimpleDomain>();
      mockResponse.value = testSimpleDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to PUT a simple domain', inject(
    [SimpleDomainMaintenanceService],
    (simpleDomainService: SimpleDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      simpleDomainService
        .put(testId, testSimpleDomain)
        .subscribe((response: FermenterResponse<SimpleDomain>) => {
          expect(response).toBeTruthy();
          expect(response.value.name).toEqual(testName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        constants.stoutCookbookDomainEndPoint + testUrl + '/' + testId
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('PUT');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<SimpleDomain>();
      mockResponse.value = testSimpleDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to POST a simple domain', inject(
    [SimpleDomainMaintenanceService],
    (simpleDomainService: SimpleDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      simpleDomainService
        .post(testSimpleDomain)
        .subscribe((response: FermenterResponse<SimpleDomain>) => {
          expect(response).toBeTruthy();
          expect(response.value.name).toEqual(testName);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        constants.stoutCookbookDomainEndPoint + testUrl
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('POST');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<SimpleDomain>();
      mockResponse.value = testSimpleDomain;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to DELETE a simple domain', inject(
    [SimpleDomainMaintenanceService],
    (simpleDomainService: SimpleDomainMaintenanceService) => {
      const testId = 'Test Id';

      simpleDomainService
        .delete(testId)
        .subscribe((response: FermenterResponse<{}>) => {
          expect(response).toBeTruthy();
          expect(response.hasMessages()).toBeFalsy();
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        constants.stoutCookbookDomainEndPoint + testUrl + '/' + testId
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

  it('should be able to FIND a simple domain by example', inject(
    [SimpleDomainMaintenanceService],
    (simpleDomainService: SimpleDomainMaintenanceService) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testId2 = 'Test Id 2';
      const simpleDomainProbe = new SimpleDomain();
      simpleDomainProbe.name = testName;
      const sortWrapper = new SortWrapper('id');
      const findByExampleCriteria = new FindByExampleCriteria<SimpleDomain>(
        sortWrapper,
        simpleDomainProbe
      );

      simpleDomainService
        .findByExample(findByExampleCriteria)
        .subscribe((page: PageWrapper<SimpleDomain>) => {
          expect(page).toBeTruthy();
          expect(page.totalElements).toEqual(2);
          expect(page.content[0].id).toEqual(testId);
          expect(page.content[1].id).toEqual(testId2);
        });

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        constants.stoutCookbookDomainEndPoint + testUrl + '/findByExample'
      );

      // Assert that the request is a PUT.
      expect(req.request.method).toEqual('POST');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      const testSimpleDomain2 = new SimpleDomain();
      testSimpleDomain2.name = testName;
      testSimpleDomain2.id = testId2;

      const pageWrapper = new PageWrapper<SimpleDomain>();
      pageWrapper.content = [testSimpleDomain, testSimpleDomain2];
      pageWrapper.first = true;
      pageWrapper.first = true;
      pageWrapper.size = pageWrapper.content.length;
      pageWrapper.number = 0;
      pageWrapper.numberOfElements = pageWrapper.content.length;
      pageWrapper.totalPages = 1;
      pageWrapper.totalElements = pageWrapper.content.length;

      const mockResponse = new FermenterResponse<PageWrapper<SimpleDomain>>();
      mockResponse.value = pageWrapper;
      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));
});
