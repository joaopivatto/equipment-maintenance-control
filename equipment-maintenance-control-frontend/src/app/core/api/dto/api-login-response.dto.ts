export interface ApiLoginResponseDto {
  userId: number;
  profileType: 'EMPLOYEE' | 'CUSTOMER' | string;
  name?: string;
  email?: string;
}
