import { Component } from '@angular/core';
import { ROUTE_CODES } from './constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  routes = ROUTE_CODES;
}
