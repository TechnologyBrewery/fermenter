import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { GlobalErrorHandler } from '../global-error-handler.service';
import { debounceTime, tap } from 'rxjs/operators';
import { FermenterMessage } from '../model/fermenter-message.model';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-global-error-handler',
  templateUrl: './global-error-handler.component.html',
  styleUrls: ['./global-error-handler.component.css']
})
export class GlobalErrorHandlerComponent implements OnInit {
  private messages = new Array<FermenterMessage>();

  constructor(
    public dialog: MatDialog,
    private globalErrorHandler: GlobalErrorHandler
  ) {}

  ngOnInit() {
    this.globalErrorHandler.errorsToShowToUser$
      .pipe(
        // push messages into a list to avoid showing multiple dialogs
        tap(message => {
          this.messages.push(message);
        }),
        // wait for all messages to come in
        debounceTime(10)
      )
      .subscribe(() => {
        // once we have all the messages show the error dialog
        const messagesToShow = new Array<FermenterMessage>();
        this.messages.forEach(m => messagesToShow.push(m));
        this.messages = new Array<FermenterMessage>();
        this.dialog.open(ErrorDialogComponent, { data: messagesToShow });
      });
  }
}
