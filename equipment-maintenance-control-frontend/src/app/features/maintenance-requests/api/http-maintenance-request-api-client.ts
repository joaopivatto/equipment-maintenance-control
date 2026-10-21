import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { EMPTY, Observable, catchError, map, of } from 'rxjs';
import { API_BASE_URL } from '../../../core/api/api.config';
import { notifyAndStop, toErrorMessage } from '../../../core/api/api-error';
import { SessionService } from '../../../core/auth/session.service';
import { NotificationService } from '../../../core/notifications/notification.service';
import { MaintenanceRequest } from '../models/maintenance-request.model';
import { ApiMaintenanceRequestDto } from './dto/api-maintenance-request.dto';
import { toMaintenanceRequest } from './mappers/maintenance-request.mapper';
import { MaintenanceRequestApiClient } from './maintenance-request-api-client';

@Injectable()
export class HttpMaintenanceRequestApiClient extends MaintenanceRequestApiClient {
  private readonly http = inject(HttpClient);
  private readonly session = inject(SessionService);
  private readonly notifications = inject(NotificationService);

  private readonly url = `${API_BASE_URL}/maintenance-request`;

  listAll(): Observable<MaintenanceRequest[]> {
    const user = this.session.currentUser();
    const endpoint = this.session.isCustomer()
      ? `${API_BASE_URL}/customers/${user?.id}/maintenance-requests`
      : this.url;

    return this.http
      .get<ApiMaintenanceRequestDto[]>(endpoint)
      .pipe(map((requests) => requests.map(toMaintenanceRequest)));
  }

  findById(id: number): Observable<MaintenanceRequest | undefined> {
    return this.http.get<ApiMaintenanceRequestDto>(`${this.url}/${id}`).pipe(
      map(toMaintenanceRequest),
      catchError(() => of(undefined)),
    );
  }

  insert(
    equipmentDescription: string,
    equipmentCategoryId: number,
    _equipmentCategoryName: string,
    defectDescription: string,
    _customerName: string,
  ): Observable<MaintenanceRequest> {
    return this.withUserId((customerId) =>
      this.http
        .post<ApiMaintenanceRequestDto>(this.url, {
          customerId,
          equipment: { description: equipmentDescription.trim(), typeId: equipmentCategoryId },
          defectDescription: defectDescription.trim(),
        })
        .pipe(
          map(toMaintenanceRequest),
          catchError(
            notifyAndStop<MaintenanceRequest>(
              this.notifications,
              'Não foi possível criar a solicitação.',
            ),
          ),
        ),
    );
  }

  createBudget(id: number, value: number): Observable<MaintenanceRequest | undefined> {
    return this.withUserId((employeeId) =>
      this.post(id, 'budget', { value, employeeId }, 'Não foi possível registrar o orçamento.'),
    );
  }

  approve(id: number): Observable<MaintenanceRequest | undefined> {
    return this.withUserId((customerId) =>
      this.post(id, 'approve', { customerId }, 'Não foi possível aprovar o orçamento.'),
    );
  }

  reject(id: number, reason: string): Observable<MaintenanceRequest | undefined> {
    return this.withUserId((customerId) =>
      this.post(id, 'reject', { customerId, reason }, 'Não foi possível rejeitar o orçamento.'),
    );
  }

  recover(id: number): Observable<MaintenanceRequest | undefined> {
    return this.withUserId((customerId) =>
      this.post(id, 'approve', { customerId }, 'Não foi possível resgatar a solicitação.'),
    );
  }

  pay(id: number): Observable<MaintenanceRequest | undefined> {
    return this.post(id, 'pay', null, 'Não foi possível registrar o pagamento.');
  }

  performMaintenance(
    id: number,
    maintenanceDescription: string,
    maintenanceInstructions: string,
    employeeId: number,
    _employeeName: string,
  ): Observable<MaintenanceRequest | undefined> {
    return this.post(
      id,
      'maintenance',
      {
        description: maintenanceDescription.trim(),
        customerInstructions: maintenanceInstructions.trim(),
        employeeId,
      },
      'Não foi possível registrar a manutenção.',
    );
  }

  redirect(
    id: number,
    fromEmployeeId: number,
    _fromEmployeeName: string,
    toEmployeeId: number,
    _toEmployeeName: string,
  ): Observable<{ request?: MaintenanceRequest; error?: string }> {
    return this.http
      .post<ApiMaintenanceRequestDto>(`${this.url}/${id}/redirect`, {
        sourceEmployeeId: fromEmployeeId,
        destinationEmployeeId: toEmployeeId,
      })
      .pipe(
        map((dto) => ({ request: toMaintenanceRequest(dto) })),
        catchError((error: unknown) =>
          of({ error: toErrorMessage(error, 'Não foi possível redirecionar a solicitação.') }),
        ),
      );
  }

  finalize(
    id: number,
    employeeId: number,
    _employeeName: string,
  ): Observable<MaintenanceRequest | undefined> {
    return this.post(id, 'finish', { employeeId }, 'Não foi possível finalizar a solicitação.');
  }

  private post(
    id: number,
    action: string,
    body: object | null,
    fallbackMessage: string,
  ): Observable<MaintenanceRequest | undefined> {
    return this.http
      .post<ApiMaintenanceRequestDto>(`${this.url}/${id}/${action}`, body)
      .pipe(
        map(toMaintenanceRequest),
        catchError(
          notifyAndStop<MaintenanceRequest | undefined>(this.notifications, fallbackMessage),
        ),
      );
  }

  private withUserId<T>(call: (userId: number) => Observable<T>): Observable<T> {
    const user = this.session.currentUser();

    if (!user) {
      this.notifications.error('Erro', 'Sessão expirada. Faça login novamente.');
      return EMPTY;
    }

    return call(user.id);
  }
}
