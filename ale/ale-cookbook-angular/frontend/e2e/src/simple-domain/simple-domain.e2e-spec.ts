import { SimpleDomainPage } from './simple-domain.po';

describe('Simple Domain e2e Test Page', () => {
  let page: SimpleDomainPage;

  beforeEach(() => {
    page = new SimpleDomainPage();
    page.navigateTo();
    page.refresh();
    page.deleteAllSimpleDomains();
  });

  it('should display a welcome message', () => {
    expect(page.getTitleText()).toEqual('List of Simple Domains');
  });

  it('should be able to ADD a Simple Domain', () => {
    const testNameToAdd = 'test name to add';
    page.addSimpleDomain(testNameToAdd);
    expect(page.listOfSimpleDomainNames()).toContain(testNameToAdd);
  });

  it('should be able to GET a Simple Domain', () => {
    const testNameToGet = 'test name to get';
    const id = page.addSimpleDomain(testNameToGet);
    page.showListById(id);
    const listOfSimpleDomainNames = page.listOfSimpleDomainNames();
    expect(listOfSimpleDomainNames).toContain(testNameToGet);
  });

  it('should be able to UPDATE a Simple Domain', () => {
    const firstName = 'test to edit first name';
    const secondName = 'test to edit second name';
    const firstNameId = page.addSimpleDomain(firstName);
    expect(page.editSimpleDomain(secondName)).toContain('Success');
    page.showListById(firstNameId);
    const listOfSimpleDomainNames = page.listOfSimpleDomainNames();
    expect(listOfSimpleDomainNames).toContain(secondName);
  });

  it('should be able to DELETE a Simple Domain', () => {
    const testNameToDelete = 'test name to delete';
    const testNameToKeep = 'test name to keep';
    page.addSimpleDomain(testNameToKeep);
    const id = page.addSimpleDomain(testNameToDelete);
    page.showListById(id);
    expect(page.listOfSimpleDomainNames()).toContain(testNameToDelete);
    const nameDeleted = page.deleteSimpleDomainInList();
    expect(nameDeleted).toContain(testNameToDelete);
    page.getAllSimpleDomains();
    const simpleDomains = page.listOfSimpleDomainNames();
    expect(simpleDomains).toContain(testNameToKeep);
    expect(simpleDomains).not.toContain(testNameToDelete);
  });

  afterEach(() => {
    page.deleteAllSimpleDomains();
  });
});
