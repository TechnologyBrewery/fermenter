import { SimpleDomainPage } from './simple-domain.po';

describe('Simple Domain e2e Test Page', () => {
  const RESULT_PASSED = 'PASSED';
  let page: SimpleDomainPage;

  beforeEach(() => {
    page = new SimpleDomainPage();
    page.navigateTo();
    page.refresh();
    page.deleteAllSimpleDomains();
  });

  it('should display a welcome message', () => {
    expect(page.getTitleText()).toEqual('Simplified UI E2E test setup');
  });

  it('should be able to ADD a Simple Domain', () => {
    page.runAddSimpleDomainTest();
    expect(page.getAddSimpleDomainTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to GET a Simple Domain', () => {
    page.runGetSimpleDomainTest();
    expect(page.getGetSimpleDomainTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to UPDATE a Simple Domain', () => {
    page.runUpdateSimpleDomainTest();
    expect(page.getUpdateSimpleDomainTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to DELETE a Simple Domain', () => {
    page.runDeleteSimpleDomainTest();
    expect(page.getDeleteSimpleDomainTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to verify Simple Domain Count via a Business Service', () => {
    page.runCountSimpleDomainTest();
    expect(page.getCountSimpleDomainTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to find by example using contains', () => {
    page.runFindByExampleContainsTest();
    expect(page.getFindByExampleContainsTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to pass a null parameter to the backend', () => {
    page.runNullParamTest();
    expect(page.getNullParamTestResult()).toEqual(RESULT_PASSED);
  });

  it('should be able to send lists as parameters to the backend', () => {
    page.runListParamTest();
    expect(page.getListParamTestResult()).toEqual(RESULT_PASSED)
  });

  afterEach(() => {
    page.deleteAllSimpleDomains();
  });
});
