import { Component, OnInit } from '@angular/core';
import { SimpleDomain } from '../shared/model/simple-domain.model';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { SimpleDomainMaintenanceService } from '../generated/service/maintenance/simple-domain-maintenance.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { PageWrapper } from '../shared/model/page-wrapper.model';
import { MatSnackBar } from '@angular/material';
import { SimpleDomainManagerService } from '../generated/service/business/simple-domain-manager.service';
import { ValidationExample } from '../shared/model/validation-example.model';
import { ValidationExampleMaintenanceService } from '../generated/service/maintenance/validation-example-maintenance.service';

@Component({
  selector: 'app-simple-domain',
  templateUrl: './simple-domain.component.html',
  styleUrls: ['./simple-domain.component.css']
})
export class SimpleDomainComponent implements OnInit {
  findByExampleContainsTestResult = 'PENDING';
  nullParamTestResult = 'PENDING';
  public simpleDomainAdding: SimpleDomain;
  public simpleDomainEditing: SimpleDomain;
  public simpleDomains = new Array<SimpleDomain>();
  public filterListById: string;
  public simpleDomainCountByBusinessService = -1;
  private findAllCriteria = new FindByExampleCriteria<SimpleDomain>(
    new SortWrapper('name'),
    null,
    false,
    0,
    10000
  );

  constructor(
    private simpleDomainService: SimpleDomainMaintenanceService,
    private simpleDomainManagerService: SimpleDomainManagerService,
    private validationMaintService: ValidationExampleMaintenanceService,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.simpleDomainAdding = new SimpleDomain();
  }

  onSubmit() {
    console.log('submit fired');
    this.simpleDomainService
      .post(this.simpleDomainAdding)
      .subscribe((response: FermenterResponse<SimpleDomain>) => {
        this.snackBar.open('Sucessfully added', null, {
          duration: 3000,
          panelClass: ['toast-success']
        });
        this.simpleDomains.push(response.value);
        this.simpleDomainAdding = new SimpleDomain();
      });
  }

  deleteAllSimpleDomains() {
    this.simpleDomains = new Array<SimpleDomain>();
    this.simpleDomainManagerService
      .deleteAllSimpleDomains()
      .subscribe((response: FermenterResponse<undefined>) => {
        if (response.messages.hasErrorMessages()) {
          this.toast('PROBLEMS DELETING ALL SIMPLE DOMAINS', false);
        } else {
          this.toast('Sucessfully deleted all simple domains.', true);
        }
      });
  }

  filterList() {
    this.simpleDomains = new Array<SimpleDomain>();
    this.simpleDomainService
      .get(this.filterListById)
      .subscribe((simpleDomain: SimpleDomain) => {
        if (simpleDomain) {
          this.simpleDomains.push(simpleDomain);
        }
      });
  }

  deleteSimpleDomain(simpleDomain: SimpleDomain) {
    this.simpleDomainService
      .delete(simpleDomain.id)
      .subscribe((response: FermenterResponse<SimpleDomain>) => {
        if (!response.messages.hasMessages()) {
          this.toast('Sucessfully deleted ' + simpleDomain.name, true);
        }
      });
  }

  getAllSimpleDomains() {
    this.simpleDomains = new Array<SimpleDomain>();
    this.simpleDomainService
      .findByExample(this.findAllCriteria)
      .subscribe((response: PageWrapper<SimpleDomain>) => {
        this.simpleDomains = response.content;
      });
  }

  editSimpleDomain(simpleDomain: SimpleDomain) {
    this.simpleDomainEditing = simpleDomain;
  }

  onEditSubmit() {
    this.simpleDomainService
      .put(this.simpleDomainEditing.id, this.simpleDomainEditing)
      .subscribe((response: FermenterResponse<SimpleDomain>) => {
        if (response.messages.hasMessages()) {
          this.toast(
            'Failed to update ' + this.simpleDomainEditing.name,
            false
          );
        } else {
          this.toast(
            'Successfully updated ' + this.simpleDomainEditing.name,
            true
          );
          this.simpleDomainEditing = null;
        }
      });
  }

  toast(message: string, success: boolean) {
    const toastClass = success ? 'toast-success' : 'toast-fail';
    this.snackBar.open(message, null, {
      duration: 3000,
      panelClass: [toastClass]
    });
  }

  editingSimpleDomain(simpleDomain: SimpleDomain): boolean {
    return (
      this.simpleDomainEditing &&
      simpleDomain.id === this.simpleDomainEditing.id
    );
  }

  getSimpleDomainCountByBusinessService() {
    this.simpleDomainCountByBusinessService = -1;
    this.simpleDomainManagerService
      .getSimpleDomainCount()
      .subscribe((response: number) => {
        this.simpleDomainCountByBusinessService = response;
      });
  }

  testGlobalErrorHandler() {
    const validation = new ValidationExample();
    validation.requiredField = 'testss';
    validation.regexZipcodeExample = 'this should fail';

    // make two calls to show how the popup can show multiple errors
    this.validationMaintService.post(validation).subscribe();

    this.simpleDomainManagerService
      .createAndPropagateErrorMessages(3)
      .subscribe(data => {
        // if there is a fermenter error or other error it should by default, be caught and handled by the global error handler.
        alert('YOU SHOULD NEVER SEE THIS ALERT');
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
          this.findByExampleContainsTestResult = 'PASSED';
        } else {
          this.findByExampleContainsTestResult = 'FAILED';
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
    this.simpleDomainManagerService.provideNullInputFromFrontend(null).subscribe(inputWasNull => {
      if(inputWasNull) {
        this.nullParamTestResult = 'PASSED';
      } else {
        this.nullParamTestResult = 'FAILED';
      }
    })
  }
}
