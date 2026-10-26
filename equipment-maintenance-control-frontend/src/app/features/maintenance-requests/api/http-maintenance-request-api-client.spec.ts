import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MessageService } from 'primeng/api';
import { SessionService } from '../../../core/auth/session.service';
import { NotificationService } from '../../../core/notifications/notification.service';
import { MaintenanceRequest, RequestStatus } from '../models/maintenance-request.model';
import { ApiMaintenanceRequestDto } from './dto/api-maintenance-request.dto';
import { HttpMaintenanceRequestApiClient } from './http-maintenance-request-api-client';

const API_REQUEST: ApiMaintenanceRequestDto = {
  id: 15,
  createdAt: '05/03/2026 14:30',
  equipment: { id: 2, description: 'Notebook Dell', type: { id: 1, description: 'Notebook' } },
  defectDescription: 'Não liga',
  status: 'ORCADA',
  customer: { id: 3, name: 'João', email: 'joao@email.com' },
  budget: { id: 4, value: 250, createdAt: '06/03/2026 09:00' },
};

describe('HttpMaintenanceRequestApiClient', () => {
  let client: HttpMaintenanceRequestApiClient;
  let http: HttpTestingController;
  let notifyError: ReturnType<typeof vi.spyOn>;
  let user: { id: number } | null;
  let customer: boolean;

  beforeEach(() => {
    user = { id: 3 };
    customer = true;

    TestBed.configureTestingModule({
      providers: [
        HttpMaintenanceRequestApiClient,
        MessageService,
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: SessionService,
          useValue: { currentUser: () => user, isCustomer: () => customer },
        },
      ],
    });
    client = TestBed.inject(HttpMaintenanceRequestApiClient);
    http = TestBed.inject(HttpTestingController);
    notifyError = vi.spyOn(TestBed.inject(NotificationService), 'error');
  });

  afterEach(() => http.verify());

  it('lists only the requests of the logged customer', () => {
    client.listAll().subscribe();

    http.expectOne('/api/customers/3/maintenance-requests').flush([]);
  });

  it('lists every request for an employee', () => {
    customer = false;
    client.listAll().subscribe();

    http.expectOne('/api/maintenance-request').flush([]);
  });

  it('converts status and dates coming from the backend', () => {
    let result: MaintenanceRequest[] = [];
    client.listAll().subscribe((requests) => (result = requests));

    http.expectOne('/api/customers/3/maintenance-requests').flush([API_REQUEST]);

    expect(result[0].status).toBe(RequestStatus.QUOTED);
    expect(result[0].createdAt).toBe('2026-03-05T14:30:00');
    expect(result[0].budget?.value).toBe(250);
    expect(result[0].customerName).toBe('João');
    expect(result[0].equipmentCategoryName).toBe('Notebook');
  });

  it('returns undefined when the request does not exist', () => {
    let result: MaintenanceRequest | undefined = {} as MaintenanceRequest;
    client.findById(99).subscribe((request) => (result = request));

    http
      .expectOne('/api/maintenance-request/99')
      .flush({ code: 'MAINTENANCE_REQUEST_NOT_FOUND' }, { status: 404, statusText: 'Not Found' });

    expect(result).toBeUndefined();
  });

  it('creates a request on behalf of the logged customer', () => {
    client.insert(' Notebook Dell ', 1, 'Notebook', ' Não liga ', 'João').subscribe();

    const request = http.expectOne('/api/maintenance-request');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      customerId: 3,
      equipment: { description: 'Notebook Dell', typeId: 1 },
      defectDescription: 'Não liga',
    });
    request.flush({ ...API_REQUEST, status: 'ABERTA' });
  });

  it('sends the budget with the logged employee id', () => {
    customer = false;
    user = { id: 1 };
    client.createBudget(15, 250).subscribe();

    const request = http.expectOne('/api/maintenance-request/15/budget');
    expect(request.request.body).toEqual({ value: 250, employeeId: 1 });
    request.flush(API_REQUEST);
  });

  it('approves and rejects as the logged customer', () => {
    client.approve(15).subscribe();
    const approval = http.expectOne('/api/maintenance-request/15/approve');
    expect(approval.request.body).toEqual({ customerId: 3 });
    approval.flush({ ...API_REQUEST, status: 'APROVADA' });

    client.reject(15, 'Muito caro').subscribe();
    const rejection = http.expectOne('/api/maintenance-request/15/reject');
    expect(rejection.request.body).toEqual({ customerId: 3, reason: 'Muito caro' });
    rejection.flush({ ...API_REQUEST, status: 'REJEITADA' });
  });

  it('recovers a rejected request through the approve endpoint', () => {
    client.recover(15).subscribe();

    const request = http.expectOne('/api/maintenance-request/15/approve');
    expect(request.request.body).toEqual({ customerId: 3 });
    request.flush({ ...API_REQUEST, status: 'APROVADA' });
  });

  it('pays the service without a body', () => {
    client.pay(15).subscribe();

    const request = http.expectOne('/api/maintenance-request/15/pay');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBeNull();
    request.flush({ ...API_REQUEST, status: 'PAGA' });
  });

  it('records the maintenance with the employee that did it', () => {
    client.performMaintenance(15, ' Troca de tela ', ' Evitar líquidos ', 1, 'Maria').subscribe();

    const request = http.expectOne('/api/maintenance-request/15/maintenance');
    expect(request.request.body).toEqual({
      description: 'Troca de tela',
      customerInstructions: 'Evitar líquidos',
      employeeId: 1,
    });
    request.flush({ ...API_REQUEST, status: 'ARRUMADA' });
  });

  it('redirects the request between employees', () => {
    let result: unknown;
    client.redirect(15, 1, 'Maria', 2, 'Mário').subscribe((response) => (result = response));

    const request = http.expectOne('/api/maintenance-request/15/redirect');
    expect(request.request.body).toEqual({ sourceEmployeeId: 1, destinationEmployeeId: 2 });
    request.flush({ ...API_REQUEST, status: 'REDIRECIONADA' });

    expect((result as { request: MaintenanceRequest }).request.status).toBe(
      RequestStatus.REDIRECTED,
    );
  });

  it('returns a translated error when redirecting to the same employee', () => {
    let result: unknown;
    client.redirect(15, 1, 'Maria', 1, 'Maria').subscribe((response) => (result = response));

    http
      .expectOne('/api/maintenance-request/15/redirect')
      .flush({ code: 'SELF_REDIRECT_NOT_ALLOWED' }, { status: 409, statusText: 'Conflict' });

    expect(result).toEqual({
      error: 'Não é possível redirecionar a solicitação para si mesmo.',
    });
  });

  it('finalizes the request as the given employee', () => {
    client.finalize(15, 1, 'Maria').subscribe();

    const request = http.expectOne('/api/maintenance-request/15/finish');
    expect(request.request.body).toEqual({ employeeId: 1 });
    request.flush({ ...API_REQUEST, status: 'FINALIZADA' });
  });

  it('notifies and emits nothing when an action is not allowed in the current status', () => {
    const next = vi.fn();
    client.pay(15).subscribe({ next });

    http
      .expectOne('/api/maintenance-request/15/pay')
      .flush({ code: 'INVALID_STATUS_TRANSITION' }, { status: 409, statusText: 'Conflict' });

    expect(next).not.toHaveBeenCalled();
    expect(notifyError).toHaveBeenCalledWith(
      'Erro',
      'Esta ação não é permitida no estado atual da solicitação.',
    );
  });

  it('does not call the API when there is no logged user', () => {
    user = null;
    const next = vi.fn();

    client.approve(15).subscribe({ next });

    http.expectNone('/api/maintenance-request/15/approve');
    expect(next).not.toHaveBeenCalled();
    expect(notifyError).toHaveBeenCalledWith('Erro', 'Sessão expirada. Faça login novamente.');
  });
});
