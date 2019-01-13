import { Component, OnInit } from '@angular/core';
import { SimpleDomain } from '../shared/model/simple-domain.model';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { SimpleDomainMaintenanceService } from '../generated/service/maintenance/simple-domain-maintenance.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { PageWrapper } from '../shared/model/page-wrapper.model';
import { MatSnackBar } from '@angular/material';
import { SimpleDomainManagerService } from '../generated/service/business/simple-domain-manager.service';

@Component({
  selector: 'app-simple-domain',
  templateUrl: './simple-domain.component.html',
  styleUrls: ['./simple-domain.component.css']
})
export class SimpleDomainComponent implements OnInit {
  public simpleDomainAdding: SimpleDomain;
  public simpleDomainEditing: SimpleDomain;
  public simpleDomains = new Array<SimpleDomain>();
  public filterListById: string;
  public simpleDomainCountByBusinessService = -1;
  private findAllCriteria = new FindByExampleCriteria<SimpleDomain>(
    new SortWrapper('name'),
    null,
    0,
    10000
  );

  constructor(
    private simpleDomainService: SimpleDomainMaintenanceService,
    private simpleDomainManagerService: SimpleDomainManagerService,
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
        if (response.hasErrorMessages()) {
          this.snackBar.open('PROBLEMS DELETING ALL SIMPLE DOMAINS', null, {
            duration: 50000,
            panelClass: ['toast-failure']
          });
        } else {
          this.snackBar.open('Sucessfully deleted all simple domains.', null, {
            duration: 3000,
            panelClass: ['toast-success']
          });
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
        if (!response.hasMessages()) {
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
        if (response.hasMessages()) {
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
}
