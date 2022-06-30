import { TestBed } from '@angular/core/testing';
import { MaterialModule } from '../material.module';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SimpleDomainTheLong1FormComponent } from '../generated/ui/simple-domain/the-long1/form/simple-domain-the-long1-form.component';
import { SimpleDomainTheDate1FormComponent } from '../generated/ui/simple-domain/the-date1/form/simple-domain-the-date1-form.component';

describe('Ale Simple Domain Input Element Generation Validation', () => {
  // Setup test environment, etc
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialModule, FormsModule, BrowserAnimationsModule ],
      declarations: [SimpleDomainTheDate1FormComponent, SimpleDomainTheLong1FormComponent],
    });

  });

  it('should create an input component', () => {
    let fixture = TestBed.createComponent(SimpleDomainTheDate1FormComponent);
    let htmlInput = fixture.nativeElement.querySelector('input');
    expect(htmlInput).toBeTruthy();
    expect(htmlInput.type).toBe('text')
  });

  it('should be able to create a date input component', () => {
    let fixture = TestBed.createComponent(SimpleDomainTheDate1FormComponent);
    let htmlDateInput = fixture.nativeElement.querySelector('input');
    expect(htmlDateInput).toBeTruthy();
    expect(htmlDateInput.type).toBe('text')
    expect(htmlDateInput.classList.contains('mat-datepicker-input')).toBeTruthy()
  });

});
