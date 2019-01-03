import { FermenterResponseBase } from '../../generated/model-base/fermenter-response-base.model';

export class FermenterResponse<T> extends FermenterResponseBase<T> {
  constructor(response?: FermenterResponseBase<T>) {
    super(response);
  }
}
