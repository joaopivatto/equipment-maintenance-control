import { Employee } from '../../models/employee.model';
import { ApiEmployeeDto } from '../dto/api-employee.dto';

export function toEmployee(dto: ApiEmployeeDto): Employee {
  return new Employee(dto.id, dto.name, dto.email, dto.birthDate, true);
}
