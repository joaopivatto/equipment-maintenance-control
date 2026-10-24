export interface ApiLoginResponseDto {
  token?: string;
  userId: number;
  profileType: 'EMPLOYEE' | 'CUSTOMER' | string;
  name?: string;
  email?: string;
}
