import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { MaintenanceRequest, RequestStatus } from '../models/maintenance-request.model';
import { MaintenanceRequestMockFactory } from '../models/testing/maintenance-request.mock';
import { MaintenanceRequestApiClient } from './maintenance-request-api-client';

@Injectable()
export class MockMaintenanceRequestApiClient extends MaintenanceRequestApiClient {
  private readonly requestFactory = new MaintenanceRequestMockFactory();

  private readonly requests: MaintenanceRequest[] = Array.from({ length: 25 }, () =>
    this.requestFactory.generate(),
  );

  listAll(): Observable<MaintenanceRequest[]> {
    return of(this.requests);
  }

  findById(id: number): Observable<MaintenanceRequest | undefined> {
    return of(this.requests.find((request) => request.id === id));
  }

  // RF006 - Aprovar serviço: ORÇADA -> APROVADA
  approve(id: number): Observable<MaintenanceRequest | undefined> {
    const request = this.requests.find((r) => r.id === id);
    if (!request) {
      return of(undefined);
    }

    request.status = RequestStatus.APPROVED;
    request.history.push({
      status: RequestStatus.APPROVED,
      dateTime: new Date().toISOString(),
    });

    return of(request);
  }

  // RF007 - Rejeitar serviço: ORÇADA -> REJEITADA, com motivo obrigatório
  reject(id: number, reason: string): Observable<MaintenanceRequest | undefined> {
    const request = this.requests.find((r) => r.id === id);
    if (!request) {
      return of(undefined);
    }

    request.status = RequestStatus.REJECTED;
    request.rejectionReason = reason;
    request.history.push({
      status: RequestStatus.REJECTED,
      dateTime: new Date().toISOString(),
      reason,
    });

    return of(request);
  }

  // RF009 - Resgatar serviço: REJEITADA -> APROVADA
  recover(id: number): Observable<MaintenanceRequest | undefined> {
    const request = this.requests.find((r) => r.id === id);
    if (!request || request.status !== RequestStatus.REJECTED) {
      return of(undefined);
    }

    request.status = RequestStatus.APPROVED;
    request.rejectionReason = undefined;
    request.history.push({
      status: RequestStatus.APPROVED,
      dateTime: new Date().toISOString(),
      reason: 'Serviço resgatado após rejeição anterior.',
    });

    return of(request);
  }

  // RF010 - Pagar serviço: ARRUMADA -> PAGA
  pay(id: number): Observable<MaintenanceRequest | undefined> {
    const request = this.requests.find((r) => r.id === id);
    if (!request || request.status !== RequestStatus.REPAIRED) {
      return of(undefined);
    }

    const now = new Date().toISOString();
    request.status = RequestStatus.PAID;
    request.paidAt = now;
    request.history.push({
      status: RequestStatus.PAID,
      dateTime: now,
    });

    return of(request);
  }

  // RF014 - Efetuar manutenção: APROVADA -> ARRUMADA
  performMaintenance(
    id: number,
    maintenanceDescription: string,
    maintenanceInstructions: string,
    employeeId: number,
    employeeName: string,
  ): Observable<MaintenanceRequest | undefined> {
    const request = this.requests.find((r) => r.id === id);
    if (!request || request.status !== RequestStatus.APPROVED) {
      return of(undefined);
    }

    request.status = RequestStatus.REPAIRED;
    request.maintenanceDescription = maintenanceDescription;
    request.maintenanceInstructions = maintenanceInstructions;
    request.assignedEmployeeId = employeeId;
    request.assignedEmployeeName = employeeName;
    request.history.push({
      status: RequestStatus.REPAIRED,
      dateTime: new Date().toISOString(),
      responsible: employeeName,
    });

    return of(request);
  }

  // RF015 - Redirecionar manutenção: mantém o estado atual como REDIRECIONADA,
  // impede redirecionar para o próprio funcionário e registra origem/destino
  redirect(
    id: number,
    fromEmployeeId: number,
    fromEmployeeName: string,
    toEmployeeId: number,
    toEmployeeName: string,
  ): Observable<{ request?: MaintenanceRequest; error?: string }> {
    const request = this.requests.find((r) => r.id === id);
    if (!request) {
      return of({ error: 'Solicitação não encontrada.' });
    }

    if (fromEmployeeId === toEmployeeId) {
      return of({ error: 'Não é possível redirecionar a solicitação para si mesmo.' });
    }

    request.status = RequestStatus.REDIRECTED;
    request.assignedEmployeeId = toEmployeeId;
    request.assignedEmployeeName = toEmployeeName;
    request.history.push({
      status: RequestStatus.REDIRECTED,
      dateTime: new Date().toISOString(),
      responsible: toEmployeeName,
      fromEmployee: fromEmployeeName,
      toEmployee: toEmployeeName,
    });

    return of({ request });
  }
}
