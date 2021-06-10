import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SimpleDomainComponent } from './simple-domain/simple-domain.component';
import { E2eTestsPageComponent } from './e2e-tests-page/e2e-tests-page.component';
import { BeerExampleComponent } from './beer-example-page/beer-example-page.component';
import { ROUTE_CODES } from './constants';

const routes: Routes = [
  {
    path: ROUTE_CODES.SIMPLE_PAGE,
    component: SimpleDomainComponent
  },
  {
    path: ROUTE_CODES.E2E_PAGE,
    component: E2eTestsPageComponent
  },
  {
    path: ROUTE_CODES.BEER_EXAMPLE,
    component: BeerExampleComponent
  },
  {
    path: '',
    redirectTo: ROUTE_CODES.SIMPLE_PAGE,
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
