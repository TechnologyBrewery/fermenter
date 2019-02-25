import { ErrorHandlerApi } from '../generated/error-handler-api';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, Subject } from 'rxjs';
import { Injectable } from '@angular/core';
import { BusinessError } from '../generated/business-error.model';
import { FermenterMessage } from './model/fermenter-message.model';

@Injectable()
export class GlobalErrorHandler implements ErrorHandlerApi {

  protected errorsToShowToUser = new Subject<FermenterMessage>();
  public errorsToShowToUser$ = this.errorsToShowToUser.asObservable();

  handleServiceCallError(error: any): Observable<never> {
    let msg = 'Sorry, it looks like you have encountered an error.';

    // Fermenter Message errors
    if (error instanceof BusinessError) {
      error = <BusinessError>error;
      // show error handler dialog
      for(const message of error.messages.messages) {
        console.error(message.detailMessage);
        this.errorsToShowToUser.next(message);
      }

    // malformed request comes back as 400 w/ fermenter error messages
    } else if (error.status === 400 && error.error && error.error.messages && error.error.messages.messages) {
      for(const message of error.error.messages.messages) {
        this.errorsToShowToUser.next(new FermenterMessage(message));
      }

    // HTTP errors
    } else if (error instanceof HttpErrorResponse) {
      error = <HttpErrorResponse>error;
      if (error.code === 404) {
        msg =
          'This is embarassing, it looks like the resource you were trying to access is incorrect.';
      } else if (error.code === 403) {
        msg =
          'It looks like you are trying to access a resource you are not authorized for.';
      } else if (error.code === 401) {
        msg = 'It looks like you may not be logged in correctly.';
      } else if (error.code === 408 || error.code === 504) {
        msg =
          'It looks like your request has timed out. Please try again later or contact the helpdesk.';
      }
      const message = new FermenterMessage();
      message.severity = 'CRITICAL';
      message.summaryMessage = 'Sever connection error';
      message.detailMessage = msg;
      this.errorsToShowToUser.next(message);
    }
    console.error(msg);
    return throwError(error);
  }

  handleError(error: any): void {
    throw new Error('Method not implemented.');
  }
}
