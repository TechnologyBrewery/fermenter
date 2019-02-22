import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SimpleDomain } from '../shared/model/simple-domain.model';
import { ValidationReferenceExample } from '../shared/model/validation-reference-example.model';

describe('Ale Simple Domain Model Generation Validation', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule]
    }).compileComponents();
  }));

  it('should create the simple domain entity', () => {
    const simpleDomain: SimpleDomain = new SimpleDomain();
    expect(simpleDomain).toBeTruthy();
  });

  it(`should be able to serialize from json for a valid name property`, () => {
    const testName = 'my test name';
    const simpleDomainJson: any = new Object();
    // this structure should match the generated structure
    // verifies that we are generating the correct stuff
    // and verifies that we can set and retrieve the content
    simpleDomainJson.name = testName;
    const simpleDomain: SimpleDomain = new SimpleDomain(simpleDomainJson);
    expect(simpleDomain.name).toEqual(testName);
  });

  it(`should be able to serialize from json for a valid uuid property`, () => {
    const testUUID = 'd3d062f8-b4fb-4ca6-a6e9-36db76a6f47a';
    const simpleDomainJson: any = new Object();
    // this structure should match the generated structure
    // verifies that we are generating the correct stuff
    // and verifies that we can set and retrieve the content
    simpleDomainJson.id = testUUID;
    const simpleDomain: SimpleDomain = new SimpleDomain(simpleDomainJson);
    expect(simpleDomain.id).toEqual(testUUID);
  });

  it(`should be able to serialize from json for a valid number field`, () => {
    const testNumber = 1234;
    const simpleDomainJson: any = new Object();
    // this structure should match the generated structure
    // verifies that we are generating the correct stuff
    // and verifies that we can set and retrieve the content
    simpleDomainJson.theLong1 = testNumber;
    const simpleDomain: SimpleDomain = new SimpleDomain(simpleDomainJson);
    expect(simpleDomain.theLong1).toEqual(testNumber);
  });

  it(`should be able to serialize from json for a valid date field`, () => {
    const testDate = new Date();
    const simpleDomainJson: any = new Object();
    // this structure should match the generated structure
    // verifies that we are generating the correct stuff
    // and verifies that we can set and retrieve the content
    simpleDomainJson.theDate1 = testDate;
    const simpleDomain: SimpleDomain = new SimpleDomain(simpleDomainJson);
    expect(simpleDomain.theDate1).toEqual(testDate);
  });

  it(`should be able to serialize from json for a valid relation`, () => {
    const simpleDomainChildTestName = 'I am the related child';
    const simpleDomainChild: any = new Object();
    simpleDomainChild.name = simpleDomainChildTestName;

    const simpleDomainChilds = new Array();
    simpleDomainChilds.push(simpleDomainChild);

    const simpleDomainTestName = 'I am the parent';
    const simpleDomainJson: any = new Object();
    simpleDomainJson.name = simpleDomainTestName;
    simpleDomainJson.simpleDomainChilds = simpleDomainChilds;

    const simpleDomain: SimpleDomain = new SimpleDomain(simpleDomainJson);

    expect(simpleDomain.simpleDomainChilds[0].name).toEqual(
      simpleDomainChildTestName
    );
  });

  it(`should be able to serialize from json for a valid reference`, () => {
    const testUUID = 'd3d062f8-b4fb-4ca6-a6e9-36db76a6f47a';
    const validationReferencedObject: any = new Object();
    validationReferencedObject.id = testUUID;

    const validationRefJson: any = new Object();
    validationRefJson.requiredReference = validationReferencedObject;

    const validationRef = new ValidationReferenceExample(validationRefJson);
    expect(validationRef.requiredReference.id).toEqual(testUUID);
  });
});
