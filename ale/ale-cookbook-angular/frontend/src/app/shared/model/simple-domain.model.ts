import { SimpleDomainBase } from '../../generated/model-base/simple-domain-base.model';

/*******************************************************
 * BO for Angular frontend for SimpleDomain
 *
 * Generated Code - DO MODIFY
 * Template = entity.model.ts.vm
 *******************************************************/
export class SimpleDomain extends SimpleDomainBase {
  public extendedProperty: string;

  constructor(simpleDomain?: SimpleDomain) {
    super(simpleDomain);
    if (simpleDomain) {
      this.extendedProperty = simpleDomain.extendedProperty;
    }
  }
}
