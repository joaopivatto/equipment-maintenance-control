import { Injectable } from '@angular/core';
import { Employee } from '../models/employee.model';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  // Massa de teste - depois substituído pela API REST
  private employees: Employee[] = [
    new Employee(1, 'Maria', 'maria@empresa.com', '1990-04-12'),
    new Employee(2, 'Mário', 'mario@empresa.com', '1988-11-03'),
  ];
  private nextId = 3;

  listAll(): Employee[] {
    return this.employees.filter((e) => e.active);
  }

  findById(id: number): Employee | undefined {
    return this.employees.find((e) => e.id === id && e.active);
  }

  insert(name: string, email: string, birthDate: string, password: string): void {
    // "password" tem que fazer o hash no backend (SHA-256 + SALT); aqui é só mock
    this.employees.push(new Employee(this.nextId++, name, email, birthDate));
  }

  update(id: number, name: string, email: string, birthDate: string): void {
    const employee = this.employees.find((e) => e.id === id);
    if (employee) {
      employee.name = name;
      employee.email = email;
      employee.birthDate = birthDate;
    }
  }

  // Não pode remover a si mesmo, nem remover se for o único funcionário ativo
  canRemove(id: number, currentEmployeeId: number): { allowed: boolean; reason?: string } {
    if (id === currentEmployeeId) {
      return { allowed: false, reason: 'Você não pode remover a si mesmo.' };
    }
    if (this.listAll().length <= 1) {
      return { allowed: false, reason: 'Não é possível remover o único funcionário cadastrado.' };
    }
    return { allowed: true };
  }

  deactivate(id: number): void {
    const employee = this.employees.find((e) => e.id === id);
    if (employee) {
      employee.active = false;
    }
  }
}
