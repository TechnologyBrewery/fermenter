import { ErrorHandlerApi } from '../generated/error-handler-api';
import { HttpErrorResponse } from '@angular/common/http';
import { ObservableInput, Observable, throwError } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class GlobalErrorHandler implements ErrorHandlerApi {
  handleCriticalError(message: string): void {
    throw new Error('Method not implemented.');
  }

  handleServiceCallError(error: any): Observable<never> {
    let msg = 'Sorry, it looks like you have encountered an error.';
    if (error instanceof HttpErrorResponse) {
      error = <HttpErrorResponse>error;
      if (error.code === 404) {
        msg =
          'This is embarassing, it looks like the resource you were trying to access is in correct.';
      } else if (error.code === 403) {
        msg =
          'It looks like you are trying to access a resource you are not authorized for.';
      } else if (error.code === 401) {
        msg = 'It looks like you may not be logged in correctly.';
      } else if (error.code === 408 || error.code === 504) {
        msg =
          'It looks like your request has timeed out. Please try again later or contact the helpdesk.';
      }
      msg += ' Error details : ' + error.code + ' - ' + error.message;
    }
    // should show the error to the user not using alerts as they are evil
    // example of a nicer error handler for users:
    // https://stackoverflow.com/questions/44526390/angular-material-create-alert-similar-to-bootstrap-alerts
    // alert(msg);
    console.error(msg);
    return throwError(error);
  }

  handleError(error: any): void {
    throw new Error('Method not implemented.');
  }
}
