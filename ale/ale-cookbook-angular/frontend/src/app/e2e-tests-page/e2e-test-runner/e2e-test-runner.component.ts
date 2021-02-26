import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { e2eTestStatus } from '../e2e-test-status.enum';

@Component({
  selector: 'app-e2e-test-runner',
  templateUrl: './e2e-test-runner.component.html',
  styleUrls: ['./e2e-test-runner.component.css']
})
export class E2eTestRunnerComponent implements OnInit {

  public e2eTestStatus = e2eTestStatus;
  @Input("testName") testName: string;
  @Output() runTestChange = new EventEmitter();

  testResultValue = e2eTestStatus.TEST_PENDING;

  @Input()
  get testResult() {
    return this.testResultValue;
  }

  @Output() testResultChange = new EventEmitter();

  set testResult(val: e2eTestStatus) {
    this.testResultValue = val;
    this.testResultChange.emit(this.testResultValue);
  }

  constructor() { }

  ngOnInit() {
  }

  runTest() {
    this.runTestChange.emit();
  }

}
