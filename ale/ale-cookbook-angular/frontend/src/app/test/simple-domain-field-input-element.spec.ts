import { TestBed, ComponentFixture } from '@angular/core/testing';
import { MaterialModule } from '../material.module';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SimpleDomainTheLong1FormComponent } from '../generated/ui/simple-domain/the-long1/form/simple-domain-the-long1-form.component';

let component: SimpleDomainTheLong1FormComponent;
let fixture: ComponentFixture<SimpleDomainTheLong1FormComponent>;
let htmlInput: HTMLInputElement;

describe('Ale Simple Domain Input Element Generation Validation', () => {
  // Setup test environment, etc
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialModule, FormsModule, BrowserAnimationsModule ],
      declarations: [SimpleDomainTheLong1FormComponent],
    });
    fixture = TestBed.createComponent(SimpleDomainTheLong1FormComponent);
    component = fixture.componentInstance; // BannerComponent test instance
  });

  it('should create an input component', () => {
    htmlInput = fixture.nativeElement.querySelector('input');
    expect(htmlInput).toBeTruthy();
  });

});