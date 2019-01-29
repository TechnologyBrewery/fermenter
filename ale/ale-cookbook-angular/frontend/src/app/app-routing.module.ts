import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SimpleDomainComponent } from './simple-domain/simple-domain.component';

const routes: Routes = [
  {
    path: 'SimpleDomain',
    component: SimpleDomainComponent
  },
  {
    path: '',
    redirectTo: 'SimpleDomain',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
