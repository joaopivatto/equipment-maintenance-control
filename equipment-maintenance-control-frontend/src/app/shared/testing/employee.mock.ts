import { Employee, ProfileType } from '../models';
import { BaseMockFactory } from './mock';

export class EmployeeMockFactory extends BaseMockFactory<Employee> {
  protected readonly length = 8;

  private readonly names = [
    'Mario Rossi',
    'Maria Bianchi',
    'Razer Doe',
    'Matheus Smith',
    'João Guilherme Johnson',
    'Samuel Brown',
    'João Davis',
    'Saulo Miller',
  ];

  private readonly emails = [
    'mario.rossi@example.com',
    'maria.bianchi@example.com',
    'razer.doe@example.com',
    'matheus.smith@example.com',
    'joaoguilherme.johnson@example.com',
    'samuel.brown@example.com',
    'joao.davis@example.com',
    'saulo.miller@example.com',
  ];

  private readonly birthDates = [
    new Date('1990-01-01'),
    new Date('1985-05-15'),
    new Date('1990-01-01'),
    new Date('1985-05-15'),
    new Date('1992-09-30'),
    new Date('1988-12-10'),
    new Date('1995-07-20'),
    new Date('1993-03-25'),
  ];

  protected build(index: number): Employee {
    return {
      name: this.names[index],
      email: this.emails[index],
      profileType: ProfileType.EMPLOYEE,
      birthDate: this.birthDates[index],
    };
  }
}
