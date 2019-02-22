import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SimpleDomainComponent } from './simple-domain/simple-domain.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { GlobalErrorHandler } from './shared/global-error-handler.service';
import { SimpleDomainMaintenanceService } from './generated/service/maintenance/simple-domain-maintenance.service';
import { GlobalErrorHandlerComponent } from './shared/global-error-handler/global-error-handler.component';
import { ErrorDialogComponent } from './shared/error-dialog/error-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    SimpleDomainComponent,
    GlobalErrorHandlerComponent,
    ErrorDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [SimpleDomainMaintenanceService, GlobalErrorHandler],
  bootstrap: [AppComponent],
  entryComponents: [ErrorDialogComponent]
})
export class AppModule { }
// TODO: https://alligator.io/angular/providers-shared-modules/
