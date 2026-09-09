import { BaseProfile } from './base-profile.model';
import { Address } from './address.model';

export interface Customer extends BaseProfile {
  cpf: string;
  address: Address;
  phoneNumber: string;
}
