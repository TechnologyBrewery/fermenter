import { browser, by, element } from 'protractor';

export class SimpleDomainPage {
  navigateTo() {
    return browser.get('/#/e2e-tests');
  }

  getTitleText() {
    return element(by.id('title')).getText();
  }

  deleteAllSimpleDomains(): any {
    element(by.id('deleteAllSimpleDomains')).click();
    this.waitUntilTextIsNotPending('deletedAllSimpleDomainsStatus');
  }

  runAddSimpleDomainTest() {
    element(by.id('runAddSimpleDomainTest')).click();
  }

  getAddSimpleDomainTestResult() {
    this.waitUntilTextIsNotPending('addSimpleDomainTestResult');
    return element(by.id('addSimpleDomainTestResult')).getText();
  }

  runGetSimpleDomainTest() {
    element(by.id('runGetSimpleDomainTest')).click();
  }

  getGetSimpleDomainTestResult() {
    this.waitUntilTextIsNotPending('getSimpleDomainTestResult');
    return element(by.id('getSimpleDomainTestResult')).getText();
  }

  runUpdateSimpleDomainTest() {
    element(by.id('runUpdateSimpleDomainTest')).click();
  }

  getUpdateSimpleDomainTestResult() {
    this.waitUntilTextIsNotPending('updateSimpleDomainTestResult');
    return element(by.id('updateSimpleDomainTestResult')).getText();
  }

  runDeleteSimpleDomainTest() {
    element(by.id('runDeleteSimpleDomainTest')).click();
  }

  getDeleteSimpleDomainTestResult() {
    this.waitUntilTextIsNotPending('deleteSimpleDomainTestResult');
    return element(by.id('deleteSimpleDomainTestResult')).getText();
  }

  runCountSimpleDomainTest() {
    element(by.id('runCountSimpleDomainTest')).click();
  }

  getCountSimpleDomainTestResult() {
    this.waitUntilTextIsNotPending('countSimpleDomainTestResult');
    return element(by.id('countSimpleDomainTestResult')).getText();
  }

  runFindByExampleContainsTest() {
    element(by.id('runFindByExampleContainsTest')).click();
  }

  getFindByExampleContainsTestResult() {
    this.waitUntilTextIsNotPending('findByExampleContainsTestResult');
    return element(by.id('findByExampleContainsTestResult')).getText();
  }

  runNullParamTest() {
      element(by.id('runNullParamTest')).click();
  }

  getNullParamTestResult() {
    this.waitUntilTextIsNotPending('nullParamTestResult');
    return element(by.id('nullParamTestResult')).getText();
  }

  waitUntilTextIsNotPending(elementId: string) {
    browser.wait(() => {
      return element(by.id(elementId))
        .getText()
        .then(value => {
          return value !== 'PENDING';
        });
    }, 1000);
  }

  refresh() {
    browser.refresh();
  }
}
