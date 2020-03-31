import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SimpleDomainEnumeration } from '../generated/enum/simple-domain-enumeration.model';
import { ValuedEnumerationExample } from '../generated/enum/valued-enumeration-example.model';

describe('Ale Simple Domain Enumeration Serialization', () => {
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [RouterTestingModule]
        }).compileComponents();
    }));

    it(`should be able to serialize from json the enumeration value`, () => {
        const firstValue = 'FIRST';
        const secondValue = 'SECOND';
        const thirdValue = 'THIRD';
        const fourthValue = 'FOURTH';

        expect(SimpleDomainEnumeration.FIRST).toEqual(firstValue);
        expect(SimpleDomainEnumeration.SECOND).toEqual(secondValue);
        expect(SimpleDomainEnumeration.THIRD).toEqual(thirdValue);
        expect(SimpleDomainEnumeration.FOURTH).toEqual(fourthValue);
    });

    it(`should be able to serialize from the enumeration value to json`, () => {
        const enumJSON = JSON.stringify(SimpleDomainEnumeration.FIRST);
        expect(enumJSON).toEqual('"FIRST"');
    });

    it(`should be able to serialize from json the value-based enumeration values`, () => {
        const monthEnumJan = 1;
        const monthEnumMay = 5;
        const monthEnumDec = 12;
        expect(ValuedEnumerationExample.JANUARY).toEqual(monthEnumJan);
        expect(ValuedEnumerationExample.MAY).toEqual(monthEnumMay);
        expect(ValuedEnumerationExample.DECEMBER).toEqual(monthEnumDec);
    });

    it(`should be able to serialize from the value-based enumeration values to json`, () => {
        const enumJanJSON = JSON.stringify(ValuedEnumerationExample[1]);
        const expectedJanJSON = '"JANUARY"';
        const enumJanJSON2 = JSON.stringify(ValuedEnumerationExample.JANUARY);
        const expectedJanJSON2 = '1';
        expect(enumJanJSON).toEqual(expectedJanJSON);
        expect(enumJanJSON2).toEqual(expectedJanJSON2);
    });

});
