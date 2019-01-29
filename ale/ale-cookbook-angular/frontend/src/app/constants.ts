import { Injectable } from '@angular/core';

@Injectable()
export class Constants {
  public stoutCookbookDomainEndPoint = '/cookbook/rest';
  public stoutCookbookReferencingDomainEndPoint = '/currently-not-used';
  public DEFAULT_PAGE_SIZE = 100;
}
