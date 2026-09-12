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
      history: [],
    },
    {
      id: 4,
      createdAt: '2026-08-26 15:15',
      equipmentDescription: 'Teclado Mecânico HyperX',
      defectDescription: 'Tecla Espaço parou de funcionar',
      status: RequestStatus.REJECTED,
      history: [],
    },
    {
      id: 5,
      createdAt: '2026-08-27 08:30',
      equipmentDescription: 'Mouse Logitech MX Master',
      defectDescription: 'Clique duplo involuntário',
      status: RequestStatus.REPAIRED,
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
}
