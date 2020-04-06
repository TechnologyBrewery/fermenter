import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SimpleDomainComponent } from './simple-domain/simple-domain.component';
import { E2eTestsPageComponent } from './e2e-tests-page/e2e-tests-page.component';
import { BeerExampleComponent } from './beer-example-page/beer-example-page.component';

const routes: Routes = [
  {
    path: 'simple-domain',
    component: SimpleDomainComponent
  },
  {
    path: 'e2e-tests',
    component: E2eTestsPageComponent
  },
  {
    path: 'beer-example',
    component: BeerExampleComponent
  },
  {
    path: '',
    redirectTo: 'simple-domain',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
