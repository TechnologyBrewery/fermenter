import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from '../app.component';
import { SimpleDomainEnumeration } from "../generated/enum/SimpleDomainEnumeration.model";
import { ValuedEnumerationExample } from "../generated/enum/ValuedEnumerationExample.model";

describe('Ale Simple Domain Enumeration Serialization', () => {
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [RouterTestingModule],
            declarations: [AppComponent]
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
        const enumJSON = JSON.stringify(SimpleDomainEnumeration);
        const expectedEnum : any = {"FIRST":"FIRST","SECOND":"SECOND","THIRD":"THIRD","FOURTH":"FOURTH"};
        const expectedJSON = JSON.stringify(expectedEnum);
        expect(enumJSON).toEqual(expectedJSON);
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
        const enumJSON = JSON.stringify(ValuedEnumerationExample);
        const expectedEnum : any = {"1":"JANUARY","2":"FEBRUARY","3":"MARCH","4":"APRIL","5":"MAY","6":"JUNE",
            "7":"JULY","8":"AUGUST","9":"SEPTEMBER","10":"OCTOBER","11":"NOVEMBER","12":"DECEMBER",
            "JANUARY":1,"FEBRUARY":2,"MARCH":3,"APRIL":4,"MAY":5,"JUNE":6,"JULY":7,"AUGUST":8,"SEPTEMBER":9,
            "OCTOBER":10,"NOVEMBER":11,"DECEMBER":12};
        const expectedJSON = JSON.stringify(expectedEnum);
        expect(enumJSON).toEqual(expectedJSON);
    });

});
