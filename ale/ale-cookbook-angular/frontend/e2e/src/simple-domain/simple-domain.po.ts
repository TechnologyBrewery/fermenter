import { browser, by, element } from 'protractor';

export class SimpleDomainPage {
  navigateTo() {
    return browser.get('/#/SimpleDomain');
  }

  getSuccessToast() {
    element(by.className('toast-success')).isDisplayed();
    return element(by.className('toast-success'));
  }

  getTitleText() {
    return element(by.id('title')).getText();
  }

  getListItems() {
    browser.wait(result => {
      return element.all(by.tagName('mat-list-item')).isPresent();
    }, 3000);
    return element.all(by.tagName('mat-list-item'));
  }

  addSimpleDomain(name: string): any {
    element(by.id('simpleDomainName')).sendKeys(name);
    element(by.id('addSimpleDomain')).click();
    this.getSuccessToast();
    return this.getListItems()
      .last()
      .getAttribute('id');
  }

  showListById(simpleDomainId: any): any {
    element(by.id('filterListById')).sendKeys(simpleDomainId);
    element(by.id('filterList')).click();
  }

  deleteSimpleDomainInList() {
    this.getListItems()
      .first()
      .all(by.id('deleteSimpleDomain'))
      .click();
    return this.getSuccessToast().getText();
  }

  listOfSimpleDomainNames() {
    return this.getListItems()
      .all(by.tagName('span'))
      .map(function(elm) {
        return elm.getText();
      });
  }

  deleteAllSimpleDomains(): any {
    element(by.id('deleteAllSimpleDomains')).click();
  }

  editSimpleDomain(newName: string): any {
    const firstItemInList = this.getListItems().first();
    firstItemInList.all(by.id('editSimpleDomain')).click();
    const editField = firstItemInList.all(by.id('editField'));
    editField.clear();
    editField.sendKeys(newName);
    firstItemInList.all(by.id('saveEdits')).click();
    return this.getSuccessToast().getText();
  }

  getAllSimpleDomains(): any {
    element(by.id('getAllSimpleDomains')).click();
  }

  getCountByBusinessService(): any {
    element(by.id('getSimpleDomainCountButton')).click();
    return element(by.id('simpleDomainCountByBusinessService')).getText();
  }

  runSimplifiedTests() {
    element(by.id('runSimplifiedTests')).click();
  }

  getFindByExampleContainsTestResult() {
    return element(by.id('findByExampleContainsTestResults')).getText();
  }

  refresh() {
    browser.refresh();
  }
}
