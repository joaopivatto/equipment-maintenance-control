import { Injectable } from '@angular/core';
import { MaintenanceRequest, RequestStatus } from '../models/maintenance-request.model';

@Injectable({
  providedIn: 'root',
})
export class MaintenanceRequestService {
  // Massa de teste fictícia para validar todos os estados possíveis da solicitação
  private requests: MaintenanceRequest[] = [
    {
      id: 1,
      createdAt: '2026-08-25 09:00',
      equipmentDescription: 'Notebook Dell Inspiron',
      defectDescription: 'Tela piscando sem parar',
      status: RequestStatus.OPEN,
      history: [],
    },
    {
      id: 2,
      createdAt: '2026-08-25 10:30',
      equipmentDescription: 'Impressora HP Laserjet Pro',
      defectDescription: 'Papel enroscando na saída',
      status: RequestStatus.QUOTED,
      budget: { id: 1, value: 180.5, createdAt: '2026-08-25 11:00', employeeId: 1 },
      history: [],
    },
    {
      id: 3,
      createdAt: '2026-08-26 14:00',
      equipmentDescription: 'Desktop Gamer Core i7',
      defectDescription: 'Não liga, bipa 3 vezes',
      status: RequestStatus.APPROVED,
      budget: { id: 2, value: 320, createdAt: '2026-08-26 14:30', employeeId: 1 },
      assignedEmployeeId: 1,
      assignedEmployeeName: 'Maria',
      history: [],
    },
    {
      id: 4,
      createdAt: '2026-08-26 15:15',
      equipmentDescription: 'Teclado Mecânico HyperX',
      defectDescription: 'Tecla Espaço parou de funcionar',
      status: RequestStatus.REJECTED,
      budget: { id: 3, value: 60, createdAt: '2026-08-26 15:40', employeeId: 2 },
      rejectionReason: 'Valor do conserto acima do valor do equipamento.',
      history: [],
    },
    {
      id: 5,
      createdAt: '2026-08-27 08:30',
      equipmentDescription: 'Mouse Logitech MX Master',
      defectDescription: 'Clique duplo involuntário',
      status: RequestStatus.REPAIRED,
      budget: { id: 4, value: 45, createdAt: '2026-08-27 09:00', employeeId: 2 },
      assignedEmployeeId: 2,
      assignedEmployeeName: 'Mário',
      maintenanceDescription: 'Troca do micro switch do botão esquerdo.',
      maintenanceInstructions: 'Evitar cliques muito bruscos nos próximos dias.',
      history: [],
    },
  ];

  // Retorna a lista de solicitações simuladas
  listAll(): MaintenanceRequest[] {
    return this.requests;
  }

  findById(id: number): MaintenanceRequest | undefined {
    return this.requests.find((request) => request.id === id);
  }

  // RF006 - Aprovar serviço: ORÇADA -> APROVADA
  approve(id: number): MaintenanceRequest | undefined {
    const request = this.findById(id);
    if (!request) {
      return undefined;
    }

    request.status = RequestStatus.APPROVED;
    request.history.push({
      status: RequestStatus.APPROVED,
      dateTime: new Date().toISOString(),
    });

    return request;
  }

  // RF007 - Rejeitar serviço: ORÇADA -> REJEITADA, com motivo obrigatório
  reject(id: number, reason: string): MaintenanceRequest | undefined {
    const request = this.findById(id);
    if (!request) {
      return undefined;
    }

    request.status = RequestStatus.REJECTED;
    request.rejectionReason = reason;
    request.history.push({
      status: RequestStatus.REJECTED,
      dateTime: new Date().toISOString(),
      reason,
    });

    return request;
  }

  // RF009 - Resgatar serviço: REJEITADA -> APROVADA
  recover(id: number): MaintenanceRequest | undefined {
    const request = this.findById(id);
    if (!request || request.status !== RequestStatus.REJECTED) {
      return undefined;
    }

    request.status = RequestStatus.APPROVED;
    request.rejectionReason = undefined;
    request.history.push({
      status: RequestStatus.APPROVED,
      dateTime: new Date().toISOString(),
      reason: 'Serviço resgatado após rejeição anterior.',
    });

    return request;
  }

  // RF010 - Pagar serviço: ARRUMADA -> PAGA
  pay(id: number): MaintenanceRequest | undefined {
    const request = this.findById(id);
    if (!request || request.status !== RequestStatus.REPAIRED) {
      return undefined;
    }

    const now = new Date().toISOString();
    request.status = RequestStatus.PAID;
    request.paidAt = now;
    request.history.push({
      status: RequestStatus.PAID,
      dateTime: now,
    });

    return request;
  }

  // RF014 - Efetuar manutenção: APROVADA -> ARRUMADA
  performMaintenance(
    id: number,
    maintenanceDescription: string,
    maintenanceInstructions: string,
    employeeId: number,
    employeeName: string,
  ): MaintenanceRequest | undefined {
    const request = this.findById(id);
    if (!request || request.status !== RequestStatus.APPROVED) {
      return undefined;
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

    return request;
  }

  // RF015 - Redirecionar manutenção: mantém o estado atual como REDIRECIONADA,
  // impede redirecionar para o próprio funcionário e registra origem/destino
  redirect(
    id: number,
    fromEmployeeId: number,
    fromEmployeeName: string,
    toEmployeeId: number,
    toEmployeeName: string,
  ): { request?: MaintenanceRequest; error?: string } {
    const request = this.findById(id);
    if (!request) {
      return { error: 'Solicitação não encontrada.' };
    }

    if (fromEmployeeId === toEmployeeId) {
      return { error: 'Não é possível redirecionar a solicitação para si mesmo.' };
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

    return { request };
  }
}
