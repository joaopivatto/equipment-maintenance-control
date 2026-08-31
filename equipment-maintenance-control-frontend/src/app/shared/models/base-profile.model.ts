export interface BaseProfile {
  name: string;
  email: string;
  password: string;
  profileType: ProfileType;
}

export enum ProfileType {
  CUSTOMER = 'customer',
  EMPLOYEE = 'employee',
}

