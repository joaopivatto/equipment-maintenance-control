import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MessageService } from 'primeng/api';
import { NotificationService } from '../../../core/notifications/notification.service';
import { Employee } from '../models/employee.model';
import { HttpEmployeeApiClient } from './http-employee-api-client';

const MARIA = { id: 1, name: 'Maria', email: 'maria@empresa.com', birthDate: '1990-05-10' };

describe('HttpEmployeeApiClient', () => {
  let client: HttpEmployeeApiClient;
  let http: HttpTestingController;
  let notifyError: ReturnType<typeof vi.spyOn>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        HttpEmployeeApiClient,
        MessageService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    client = TestBed.inject(HttpEmployeeApiClient);
    http = TestBed.inject(HttpTestingController);
    notifyError = vi.spyOn(TestBed.inject(NotificationService), 'error');
  });

  afterEach(() => http.verify());

  it('lists employees as Employee objects', () => {
    let result: Employee[] = [];
    client.listAll().subscribe((employees) => (result = employees));

    http.expectOne('/api/employees').flush([MARIA]);

    expect(result).toEqual([new Employee(1, 'Maria', 'maria@empresa.com', '1990-05-10', true)]);
  });

  it('returns undefined when the employee does not exist', () => {
    let result: Employee | undefined = new Employee();
    client.findById(99).subscribe((employee) => (result = employee));

    http
      .expectOne('/api/employees/99')
      .flush({ code: 'EMPLOYEE_NOT_FOUND' }, { status: 404, statusText: 'Not Found' });

    expect(result).toBeUndefined();
  });

  it('finds an employee by e-mail ignoring case and spaces', () => {
    let result: Employee | undefined;
    client.findByEmail('  MARIA@empresa.com ').subscribe((employee) => (result = employee));

    http.expectOne('/api/employees').flush([MARIA]);

    expect(result?.id).toBe(1);
  });

  it('returns undefined when no employee has the e-mail', () => {
    let result: Employee | undefined = new Employee();
    client.findByEmail('outro@empresa.com').subscribe((employee) => (result = employee));

    http.expectOne('/api/employees').flush([MARIA]);

    expect(result).toBeUndefined();
  });

  it('creates an employee with trimmed fields', () => {
    let result: Employee | undefined;
    client
      .insert(' Maria ', ' maria@empresa.com ', '1990-05-10', 'segredo')
      .subscribe((employee) => (result = employee));

    const request = http.expectOne('/api/employees');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      name: 'Maria',
      email: 'maria@empresa.com',
      birthDate: '1990-05-10',
      password: 'segredo',
    });
    request.flush(MARIA);

    expect(result?.name).toBe('Maria');
  });

  it('notifies and emits nothing when the e-mail is already registered', () => {
    const next = vi.fn();
    client.insert('Maria', 'maria@empresa.com', '1990-05-10', 'segredo').subscribe({ next });

    http
      .expectOne('/api/employees')
      .flush({ code: 'EMPLOYEE_EMAIL_ALREADY_EXISTS' }, { status: 409, statusText: 'Conflict' });

    expect(next).not.toHaveBeenCalled();
    expect(notifyError).toHaveBeenCalledWith(
      'Erro',
      'Já existe um funcionário cadastrado com este e-mail.',
    );
  });

  it('sends who is removing the employee and reports success without an error', () => {
    let result: unknown;
    client.deactivate(2, 1).subscribe((response) => (result = response));

    const request = http.expectOne('/api/employees/2?currentEmployeeId=1');
    expect(request.request.method).toBe('DELETE');
    request.flush(null, { status: 204, statusText: 'No Content' });

    expect(result).toEqual({});
  });

  it('returns a translated error when the employee removes themselves', () => {
    let result: unknown;
    client.deactivate(1, 1).subscribe((response) => (result = response));

    http
      .expectOne('/api/employees/1?currentEmployeeId=1')
      .flush({ code: 'EMPLOYEE_CANNOT_DEACTIVATE_SELF' }, { status: 409, statusText: 'Conflict' });

    expect(result).toEqual({ error: 'Você não pode remover a si mesmo.' });
  });
});
