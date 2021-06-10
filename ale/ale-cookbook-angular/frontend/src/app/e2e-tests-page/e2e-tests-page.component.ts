import { Component, OnInit } from '@angular/core';
import { SimpleDomainManagerService } from '../generated/service/business/simple-domain-manager.service';
import { SimpleDomainMaintenanceService } from '../generated/service/maintenance/simple-domain-maintenance.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SimpleDomain } from '../shared/model/simple-domain.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { e2eTestStatus } from './e2e-test-status.enum';

@Component({
  selector: 'app-e2e-tests-page',
  templateUrl: './e2e-tests-page.component.html',
  styleUrls: ['./e2e-tests-page.component.css']
})
export class E2eTestsPageComponent implements OnInit {
  public e2eTestStatus = e2eTestStatus;
  addSimpleDomainTestResult = e2eTestStatus.TEST_PENDING;
  getSimpleDomainTestResult = e2eTestStatus.TEST_PENDING;
  updateSimpleDomainTestResult = e2eTestStatus.TEST_PENDING;
  deleteSimpleDomainTestResult = e2eTestStatus.TEST_PENDING;
  countSimpleDomainTestResult = e2eTestStatus.TEST_PENDING;
  findByExampleContainsTestResult = e2eTestStatus.TEST_PENDING;
  nullParamTestResult = e2eTestStatus.TEST_PENDING;
  listParamTestResult = e2eTestStatus.TEST_PENDING;
  getPagedResponseTestResult = e2eTestStatus.TEST_PENDING;
  deletedAllSimpleDomainsStatus = e2eTestStatus.TEST_PENDING;

  deleteAll = this.simpleDomainManagerService.deleteAllSimpleDomains();

  constructor(
    private simpleDomainService: SimpleDomainMaintenanceService,
    private simpleDomainManagerService: SimpleDomainManagerService
  ) { }

  ngOnInit() { }

  runAddSimpleDomainTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();
    this.simpleDomainService.post(testSimpleDomain).subscribe(response => {
      if (response.value.name === testSimpleDomain.name) {
        this.deleteAll.subscribe(() => {
          this.addSimpleDomainTestResult = e2eTestStatus.TEST_PASSED;
        });
      }
    });
  }

  runGetSimpleDomainTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();
    this.simpleDomainService.post(testSimpleDomain).subscribe(postResponse => {
      this.simpleDomainService
        .get(postResponse.value.id)
        .subscribe(getResponse => {
          if (getResponse.name === testSimpleDomain.name) {
            this.deleteAll.subscribe(() => {
              this.getSimpleDomainTestResult = e2eTestStatus.TEST_PASSED;
            });
          }
        });
    });
  }

  runUpdateSimpleDomainTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();
    const updatedName = 'updatedName';
    this.simpleDomainService.post(testSimpleDomain).subscribe(postResponse => {
      postResponse.value.name = updatedName;

      this.simpleDomainService
        .put(postResponse.value.id, postResponse.value)
        .subscribe(putResponse => {
          if (putResponse.value.name === updatedName) {
            this.deleteAll.subscribe(() => {
              this.updateSimpleDomainTestResult = e2eTestStatus.TEST_PASSED;
            });
          }
        });
    });
  }

  runDeleteSimpleDomainTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();

    this.simpleDomainService.post(testSimpleDomain).subscribe(postResponse => {
      this.simpleDomainService
        .delete(postResponse.value.id)
        .subscribe(putResponse => {
          this.deleteAll.subscribe(() => {
            this.deleteSimpleDomainTestResult = e2eTestStatus.TEST_PASSED;
          });
        });
    });
  }

  runCountSimpleDomainTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();

    this.simpleDomainService.post(testSimpleDomain).subscribe(postResponse => {

      this.simpleDomainManagerService
        .getSimpleDomainCount()
        .subscribe(countResponse => {
          if (countResponse === 1) {
            this.deleteAll.subscribe(() => {
              this.countSimpleDomainTestResult = e2eTestStatus.TEST_PASSED;
            });
          } else {
            this.countSimpleDomainTestResult = e2eTestStatus.TEST_FAILED;
          }
        });
    });
  }

  runFindByExampleContainsTest() {
    const testSimpleDomain = new SimpleDomain();
    const name = 'runFindByExampleContainsTest';
    testSimpleDomain.name = name + 'uniqueENDING';

    const findProbe = new SimpleDomain();
    findProbe.name = name;
    findProbe.simpleDomainChilds = undefined;
    findProbe.simpleDomainEagerChilds = undefined;

    const findCriteria = new FindByExampleCriteria<SimpleDomain>(
      new SortWrapper('name'),
      findProbe,
      true,
      0,
      10000
    );

    const findByExampleRestCall = this.simpleDomainService.findByExample(findCriteria);
    const postRestCallToSetupTestData = this.simpleDomainService.post(testSimpleDomain);

    postRestCallToSetupTestData.subscribe(postResponse => {
      findByExampleRestCall.subscribe(findResponse => {
        const findByExampleContainsFoundTestSimpleDomain =
          findResponse.content.length === 1 &&
          findResponse.content[0].name.indexOf(name) >= 0;

        if (findByExampleContainsFoundTestSimpleDomain) {
          this.deleteAll.subscribe(() => {
            this.findByExampleContainsTestResult = e2eTestStatus.TEST_PASSED;
          });
        } else {
          this.findByExampleContainsTestResult = e2eTestStatus.TEST_FAILED;
          console.error('Failed to find ' + name + ' found (' + findResponse.numberOfElements + ') simple domains');
        }
      });
    });
  }

  runNullParamTest() {
    this.simpleDomainManagerService
      .provideNullInputFromFrontend(null)
      .subscribe(inputWasNull => {
        if (inputWasNull) {
          this.deleteAll.subscribe(() => {
            this.nullParamTestResult = e2eTestStatus.TEST_PASSED;
          });
        } else {
          this.nullParamTestResult = e2eTestStatus.TEST_FAILED;
        }
      });
  }

  runListParamTest() {
    const array = ['val1', 'val2'];
    this.simpleDomainManagerService
      .listAsParamFromFrontend(array)
      .subscribe(inputWasList => {
        this.deleteAll.subscribe(() => {
          this.listParamTestResult = inputWasList ? e2eTestStatus.TEST_PASSED : e2eTestStatus.TEST_FAILED;
        });
      });
  }

  runGetPagedResponseTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();
    const pageSizeRequested = 100;
    this.simpleDomainService.post(testSimpleDomain).subscribe(postResponse => {

      this.simpleDomainManagerService
        .getPagedSimpleDomains(0, pageSizeRequested)
        .subscribe(pagedResponse => {
          const testFailed = pagedResponse.content[0].name !== testSimpleDomain.name ||
            !pagedResponse.first || pagedResponse.itemsPerPage !== pageSizeRequested ||
            pagedResponse.numberOfElements !== 1 || pagedResponse.totalResults !== 1;
          if (testFailed) {
            this.getPagedResponseTestResult = e2eTestStatus.TEST_FAILED;
          } else {
            this.deleteAll.subscribe(() => {
              this.getPagedResponseTestResult = e2eTestStatus.TEST_PASSED;
            });
          }
        });
    });
  }

  deleteAllSimpleDomains() {
    this.deleteAll.subscribe(() => {
      this.deletedAllSimpleDomainsStatus = e2eTestStatus.TEST_DONE;
    });
  }

  getRandomString() {
    return Math.random()
      .toString(36)
      .substring(2, 15);
  }
}
