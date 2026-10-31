import { ApiEmployeeDto } from '../../../employees/api/dto/api-employee.dto';
import { ApiEquipmentTypeDto } from '../../../equipment-categories/api/dto/api-equipment-type.dto';

export interface ApiEquipmentDto {
  id: number;
  description: string;
  type?: ApiEquipmentTypeDto;
}

export interface ApiAddressDto {
  zipCode?: string;
  street?: string;
  number?: number;
  complement?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
}

export interface ApiCustomerDto {
  id: number;
  name: string;
  email: string;
  cpf?: string;
  phone?: string;
  address?: ApiAddressDto;
}

export interface ApiBudgetDto {
  id: number;
  value: number;
  createdAt: string;
  employee?: ApiEmployeeDto;
}

export interface ApiMaintenanceDto {
  id: number;
  description: string;
  instructions: string;
  employee?: ApiEmployeeDto;
}

export interface ApiHistoryEntryDto {
  status: string;
  dateTime: string;
  responsible?: ApiEmployeeDto;
  reason?: string;
  fromEmployee?: ApiEmployeeDto;
  toEmployee?: ApiEmployeeDto;
}

export interface ApiMaintenanceRequestDto {
  id: number;
  createdAt: string;
  updatedAt?: string;
  equipment?: ApiEquipmentDto;
  defectDescription: string;
  status: string;
  budget?: ApiBudgetDto;
  rejectionReason?: string;
  customer?: ApiCustomerDto;
  assignedEmployee?: ApiEmployeeDto;
  maintenance?: ApiMaintenanceDto;
  paidAt?: string;
  finalizedAt?: string;
  finalizedBy?: ApiEmployeeDto;
  history?: ApiHistoryEntryDto[];
}
