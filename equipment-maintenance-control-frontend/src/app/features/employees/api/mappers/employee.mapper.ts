import { Employee } from '../../models';
import { ApiEmployeeDto } from '../dto';

export function toEmployee(dto: ApiEmployeeDto): Employee {
  return new Employee(dto.id, dto.name, dto.email, dto.birthDate, true);
}
