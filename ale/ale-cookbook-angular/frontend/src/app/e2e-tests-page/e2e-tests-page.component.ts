import { Component, OnInit } from '@angular/core';
import { SimpleDomainManagerService } from '../generated/service/business/simple-domain-manager.service';
import { SimpleDomainMaintenanceService } from '../generated/service/maintenance/simple-domain-maintenance.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SimpleDomain } from '../shared/model/simple-domain.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { FermenterResponse } from '../shared/model/fermenter-response.model';

@Component({
  selector: 'app-e2e-tests-page',
  templateUrl: './e2e-tests-page.component.html',
  styleUrls: ['./e2e-tests-page.component.css']
})
export class E2eTestsPageComponent implements OnInit {
  addSimpleDomainTestResult = 'PENDING';
  getSimpleDomainTestResult = 'PENDING';
  updateSimpleDomainTestResult = 'PENDING';
  deleteSimpleDomainTestResult = 'PENDING';
  countSimpleDomainTestResult = 'PENDING';
  findByExampleContainsTestResult = 'PENDING';
  nullParamTestResult = 'PENDING';
  deletedAllSimpleDomainsStatus = 'PENDING';
  TEST_PASSED = 'PASSED';
  TEST_FAILED = 'FAILED';

  constructor(
    private simpleDomainService: SimpleDomainMaintenanceService,
    private simpleDomainManagerService: SimpleDomainManagerService
  ) {}

  ngOnInit() {}

  runAddSimpleDomainTest() {
    const testSimpleDomain = new SimpleDomain();
    testSimpleDomain.name = this.getRandomString();
    this.simpleDomainService.post(testSimpleDomain).subscribe(response => {
      if (response.value.name === testSimpleDomain.name) {
        this.addSimpleDomainTestResult = this.TEST_PASSED;
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
            this.getSimpleDomainTestResult = this.TEST_PASSED;
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
            this.updateSimpleDomainTestResult = this.TEST_PASSED;
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
          this.deleteSimpleDomainTestResult = this.TEST_PASSED;
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
            this.countSimpleDomainTestResult = this.TEST_PASSED;
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

    const findByExampleRestCall = this.simpleDomainService.findByExample(
      findCriteria
    );
    const postRestCallToSetupTestData = this.simpleDomainService.post(
      testSimpleDomain
    );

    postRestCallToSetupTestData.subscribe(postResponse => {
      findByExampleRestCall.subscribe(findResponse => {
        const findByExampleContainsFoundTestSimpleDomain =
          findResponse.content.length === 1 &&
          findResponse.content[0].name.indexOf(name) >= 0;

        if (findByExampleContainsFoundTestSimpleDomain) {
          this.findByExampleContainsTestResult = this.TEST_PASSED;
        } else {
          this.findByExampleContainsTestResult = this.TEST_FAILED;
          console.error(
            'Failed to find ' +
              name +
              ' found (' +
              findResponse.numberOfElements +
              ') simple domains'
          );
        }
        // delete test data once done
        this.simpleDomainService.delete(postResponse.value.id).subscribe();
      });
    });
  }

  runNullParamTest() {
    this.simpleDomainManagerService
      .provideNullInputFromFrontend(null)
      .subscribe(inputWasNull => {
        if (inputWasNull) {
          this.nullParamTestResult = this.TEST_PASSED;
        } else {
          this.nullParamTestResult = this.TEST_FAILED;
        }
      });
  }

  deleteAllSimpleDomains() {
    this.simpleDomainManagerService
      .deleteAllSimpleDomains()
      .subscribe(response => {
        this.deletedAllSimpleDomainsStatus = 'DONE';
      });
  }

  getRandomString() {
    return Math.random()
      .toString(36)
      .substring(2, 15);
  }
}
