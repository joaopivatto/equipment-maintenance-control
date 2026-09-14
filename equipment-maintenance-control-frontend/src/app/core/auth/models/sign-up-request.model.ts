import { Address } from '../../../shared';

export interface SignUpRequest {
  name: string;
  email: string;
  cpf: string;
  phoneNumber: string;
  address: Address;
}
