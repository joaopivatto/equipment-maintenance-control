import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';

export abstract class EmployeeApiClient {
  abstract listAll(): Observable<Employee[]>;

  abstract findById(id: number): Observable<Employee | undefined>;

  abstract findByEmail(email: string): Observable<Employee | undefined>;

  abstract insert(
    name: string,
    email: string,
    birthDate: string,
    password: string,
  ): Observable<Employee>;

  abstract update(
    id: number,
    name: string,
    email: string,
    birthDate: string,
  ): Observable<Employee | undefined>;

  abstract deactivate(id: number, currentEmployeeId: number): Observable<{ error?: string }>;
}
