import { BaseProfile } from './base-profile.model';
import { Address } from './address.model';

export interface Customer extends BaseProfile {
  address: Address;
  phoneNumber: string;
}
