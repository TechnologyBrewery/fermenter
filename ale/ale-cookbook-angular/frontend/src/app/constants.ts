import { Injectable } from '@angular/core';

export const ROUTE_CODES = {
  SIMPLE_PAGE: 'simple-domain',
  E2E_PAGE: 'e2e-tests',
  BEER_EXAMPLE: 'beer-example'
};

@Injectable()
export class Constants {
  public STOUT_COOKBOOK_DOMAIN_END_POINT = '/cookbook/rest';
  public STOUT_COOKBOOK_REFERENCING_DOMAIN_END_POINT = '/currently-not-used';
  public DEFAULT_PAGE_SIZE = 100;
}
