import { Component, OnInit } from '@angular/core';
import { BeerExampleEntity } from '../shared/model/beer-example-entity.model';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { BeerServiceExampleService } from '../generated/service/business/beer-service-example.service';
import { BeerExampleEntityMaintenanceService } from '../generated/service/maintenance/beer-example-entity-maintenance.service'; 

@Component({
  selector: 'beer-example-page',
  templateUrl: './beer-example-page.component.html',
  styleUrls: ['./beer-example-page.component.css']
})
export class BeerExampleComponent implements OnInit {
  brewedBeers = new Array<BeerExampleEntity>();
  testVariable = '';

  constructor(
    private beerServiceExampleService: BeerServiceExampleService,
    private beerExampleEntityMaintenanceService: BeerExampleEntityMaintenanceService
  ) {}

  ngOnInit() {
  }

  onBrewSomething() {
    this.beerServiceExampleService.brewMeABeer('ANY').subscribe((response: FermenterResponse<BeerExampleEntity>) => {
      this.brewedBeers.push(response.value);
    });
  }

  onDrink(beer: BeerExampleEntity) {
    this.beerExampleEntityMaintenanceService.delete(beer.primaryKey).subscribe((response: FermenterResponse<BeerExampleEntity>) => {
      this.brewedBeers = this.brewedBeers.filter(obj => obj !== beer);
    })
  }
  
}
