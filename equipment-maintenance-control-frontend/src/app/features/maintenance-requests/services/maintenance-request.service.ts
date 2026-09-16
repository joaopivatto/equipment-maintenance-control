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
      customerName: 'João da Silva',
      history: [],
    },
    {
      id: 2,
      createdAt: '2026-08-25 10:30',
      equipmentDescription: 'Impressora HP Laserjet Pro',
      defectDescription: 'Papel enroscando na saída',
      status: RequestStatus.QUOTED,
      customerName: 'José Pereira',
      budget: {
        id: 1,
        value: 180.5,
        createdAt: '2026-08-25 11:00',
        employeeId: 1,
      },
      history: [],
    },
    {
      id: 3,
      createdAt: '2026-08-26 14:00',
      equipmentDescription: 'Desktop Gamer Core i7',
      defectDescription: 'Não liga, bipa 3 vezes',
      status: RequestStatus.APPROVED,
      customerName: 'Joana Fernandes',
      budget: {
        id: 2,
        value: 320,
        createdAt: '2026-08-26 14:30',
        employeeId: 1,
      },
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
      customerName: 'Joaquina Martins',
      budget: {
        id: 3,
        value: 60,
        createdAt: '2026-08-26 15:40',
        employeeId: 2,
      },
      rejectionReason: 'Valor do conserto acima do valor do equipamento.',
      history: [],
    },
    {
      id: 5,
      createdAt: '2026-08-27 08:30',
      equipmentDescription: 'Mouse Logitech MX Master',
      defectDescription: 'Clique duplo involuntário',
      status: RequestStatus.REPAIRED,
      customerName: 'João da Silva',
      budget: {
        id: 4,
        value: 45,
        createdAt: '2026-08-27 09:00',
        employeeId: 2,
      },
      assignedEmployeeId: 2,
      assignedEmployeeName: 'Mário',
      maintenanceDescription: 'Troca do micro switch do botão esquerdo.',
      maintenanceInstructions: 'Evitar cliques muito bruscos nos próximos dias.',
      history: [],
    },
    {
      id: 6,
      createdAt: '2026-08-28 09:00',
      equipmentDescription: 'Notebook Lenovo Ideapad',
      defectDescription: 'Superaquecimento constante',
      status: RequestStatus.PAID,
      customerName: 'José Pereira',
      budget: {
        id: 5,
        value: 210,
        createdAt: '2026-08-28 09:30',
        employeeId: 1,
      },
      assignedEmployeeId: 1,
      assignedEmployeeName: 'Maria',
      maintenanceDescription: 'Limpeza interna e troca de pasta térmica.',
      maintenanceInstructions: 'Manter o notebook em superfície ventilada.',
      paidAt: '2026-08-29T10:00:00.000Z',
      history: [],
    },
    {
      id: 7,
      createdAt: '2026-08-20 11:00',
      equipmentDescription: 'Monitor LG UltraWide',
      defectDescription: 'Tela com listras verticais',
      status: RequestStatus.FINALIZED,
      customerName: 'Joana Fernandes',
      budget: {
        id: 6,
        value: 150,
        createdAt: '2026-08-20 11:30',
        employeeId: 2,
      },
      assignedEmployeeId: 2,
      assignedEmployeeName: 'Mário',
      maintenanceDescription: 'Troca do cabo de vídeo.',
      maintenanceInstructions: 'Evitar dobrar o cabo novo.',
      paidAt: '2026-08-21T15:00:00.000Z',
      finalizedAt: '2026-08-22T09:00:00.000Z',
      finalizedByEmployeeName: 'Mário',
      history: [],
    },
    {
      id: 8,
      createdAt: '2026-08-29 13:45',
      equipmentDescription: 'Tablet Samsung Galaxy Tab',
      defectDescription: 'Não carrega a bateria',
      status: RequestStatus.REDIRECTED,
      customerName: 'Joaquina Martins',
      budget: {
        id: 7,
        value: 95,
        createdAt: '2026-08-29 14:00',
        employeeId: 1,
      },
      assignedEmployeeId: 2,
      assignedEmployeeName: 'Mário',
      history: [],
    },
    {
      id: 9,
      createdAt: new Date().toISOString(),
      equipmentDescription: 'Impressora Epson EcoTank',
      defectDescription: 'Cartucho não é reconhecido',
      status: RequestStatus.OPEN,
      customerName: 'João da Silva',
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

  // RF014 - Efetuar manutenção: APROVADA/REDIRECIONADA -> ARRUMADA
  performMaintenance(
    id: number,
    maintenanceDescription: string,
    maintenanceInstructions: string,
    employeeId: number,
    employeeName: string,
  ): MaintenanceRequest | undefined {
    const request = this.findById(id);

    if (
      !request ||
      (request.status !== RequestStatus.APPROVED &&
        request.status !== RequestStatus.REDIRECTED)
    ) {
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

  // RF015 - Redirecionar manutenção
  redirect(
    id: number,
    fromEmployeeId: number,
    fromEmployeeName: string,
    toEmployeeId: number,
    toEmployeeName: string,
  ): { request?: MaintenanceRequest; error?: string } {
    const request = this.findById(id);

    if (!request) {
      return {
        error: 'Solicitação não encontrada.',
      };
    }

    if (fromEmployeeId === toEmployeeId) {
      return {
        error: 'Não é possível redirecionar a solicitação para si mesmo.',
      };
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

    return {
      request,
    };
  }

  // RF016 - Finalizar solicitação: PAGA -> FINALIZADA
  finalize(
    id: number,
    employeeId: number,
    employeeName: string,
  ): MaintenanceRequest | undefined {
    const request = this.findById(id);

    if (!request || request.status !== RequestStatus.PAID) {
      return undefined;
    }

    const now = new Date().toISOString();

    request.status = RequestStatus.FINALIZED;
    request.finalizedAt = now;
    request.finalizedByEmployeeName = employeeName;
    request.assignedEmployeeId = employeeId;
    request.assignedEmployeeName = employeeName;

    request.history.push({
      status: RequestStatus.FINALIZED,
      dateTime: now,
      responsible: employeeName,
    });

    return request;
  }
}
