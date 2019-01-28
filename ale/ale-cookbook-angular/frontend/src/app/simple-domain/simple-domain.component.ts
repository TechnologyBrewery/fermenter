import { Component, OnInit } from '@angular/core';
import { SimpleDomain } from '../shared/model/simple-domain.model';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { SimpleDomainMaintenanceService } from '../generated/service/simple-domain-maintenance.service';
import { FindByExampleCriteria } from '../shared/model/find-by-example-criteria.model';
import { SortWrapper } from '../shared/model/sort-wrapper.model';
import { PageWrapper } from '../shared/model/page-wrapper.model';
import { MatSnackBar } from '@angular/material';

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
  private findAllCriteria = new FindByExampleCriteria<SimpleDomain>(
    new SortWrapper('name'),
    null,
    0,
    10000
  );

  constructor(
    private simpleDomainService: SimpleDomainMaintenanceService,
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
    this.simpleDomainService
      .findByExample(this.findAllCriteria)
      .subscribe((response: PageWrapper<SimpleDomain>) => {
        for (const simpleDomain of response.content) {
          this.simpleDomainService.delete(simpleDomain.id).subscribe();
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
}
