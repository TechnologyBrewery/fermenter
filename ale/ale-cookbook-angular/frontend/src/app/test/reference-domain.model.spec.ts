import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { LocalDomain } from '../shared/model/local-domain.model';
import { ValidationReferencedObject } from '../shared/model/validation-referenced-object.model';
import { reference } from '@angular/core/src/render3';


describe('Ale - support generation of multiple domains… ' +
'should create a local domain entity from the second domain model (reference-domain)', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule]
    }).compileComponents();
  }));

  it('should create the reference domain entity', () => {
    const localDomain: LocalDomain = new LocalDomain();
    expect(localDomain).toBeTruthy();
  });

  it(`should be able to serialize from json for a valid name property in the second domain`, () => {
    const testName = 'my test name';
    const localDomainJson: any = new Object();
    // this structure should match the generated structure
    // verifies that we are generating the correct stuff
    // and verifies that we can set and retrieve the content
    localDomainJson.name = testName;
    const localDomain: LocalDomain = new LocalDomain(localDomainJson);
    expect(localDomain.name).toEqual(testName);
  });

  it(`should be able to serialize from json for a valid uuid property in the second domain`, () => {
    const testUUID = 'd3d062f8-b4fb-4ca6-a6e9-36db76a6f47a';
    const localDomainJson: any = new Object();
    // this structure should match the generated structure
    // verifies that we are generating the correct stuff
    // and verifies that we can set and retrieve the content
    localDomainJson.id = testUUID;
    const localDomain: LocalDomain = new LocalDomain(localDomainJson);
    expect(localDomain.id).toEqual(testUUID);
  });

  it(`should be able to serialize from json for a valid reference in the second domain`, () => {
    const referenceName = 'Transient Reference Object';
    const externalTransientReference: any = new Object();
    externalTransientReference.name = referenceName;

    const localDomainTestName = 'Local Domain Name';
    const localDomainJson: any = new Object();
    localDomainJson.name = localDomainTestName;
    localDomainJson.externalTransientReference = externalTransientReference;

    const localDomain: LocalDomain = new LocalDomain(localDomainJson);

    expect(localDomain.externalTransientReference.name).toEqual(referenceName);
  });

});
