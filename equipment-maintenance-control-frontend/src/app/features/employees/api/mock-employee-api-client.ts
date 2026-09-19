import { Injectable, inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { DateService } from '../../../shared/services/date.service';
import { EmployeeMockFactory } from '../../../shared/testing';
import { Employee } from '../models/employee.model';
import { EmployeeApiClient } from './employee-api-client';

@Injectable()
export class MockEmployeeApiClient extends EmployeeApiClient {
  private readonly dateService = inject(DateService);
  private readonly employeeFactory = new EmployeeMockFactory();

  private readonly employees: Employee[] = Array.from({ length: 9 }, (_, index) => {
    const mock = this.employeeFactory.generate();
    return new Employee(
      index + 1,
      mock.name,
      mock.email,
      this.dateService.toIsoDate(mock.birthDate),
    );
  });

  private nextId = this.employees.length + 1;

  listAll(): Observable<Employee[]> {
    return of(this.employees.filter((e) => e.active));
  }

  findById(id: number): Observable<Employee | undefined> {
    return of(this.employees.find((e) => e.id === id && e.active));
  }

  findByEmail(email: string): Observable<Employee | undefined> {
    console.log(this.employees)
    return of(this.employees.find((e) => e.email === email && e.active));
  }

  insert(name: string, email: string, birthDate: string, password: string): Observable<Employee> {
    // "password" tem que fazer o hash no backend (SHA-256 + SALT); aqui é só mock
    const employee = new Employee(this.nextId++, name, email, birthDate);
    this.employees.push(employee);
    return of(employee);
  }

  update(
    id: number,
    name: string,
    email: string,
    birthDate: string,
  ): Observable<Employee | undefined> {
    const employee = this.employees.find((e) => e.id === id);
    if (!employee) {
      return of(undefined);
    }

    employee.name = name;
    employee.email = email;
    employee.birthDate = birthDate;

    return of(employee);
  }

  deactivate(id: number, currentEmployeeId: number): Observable<{ error?: string }> {
    if (id === currentEmployeeId) {
      return of({ error: 'Você não pode remover a si mesmo.' });
    }

    if (this.employees.filter((e) => e.active).length <= 1) {
      return of({ error: 'Não é possível remover o único funcionário cadastrado.' });
    }

    const employee = this.employees.find((e) => e.id === id);
    if (employee) {
      employee.active = false;
    }

    return of({});
  }
}
