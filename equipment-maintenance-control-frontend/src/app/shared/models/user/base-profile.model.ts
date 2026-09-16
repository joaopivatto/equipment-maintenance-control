export interface BaseProfile {
  name: string;
  email: string;
  profileType: ProfileType;
}

export enum ProfileType {
  CUSTOMER = 'customer',
  EMPLOYEE = 'employee',
}

