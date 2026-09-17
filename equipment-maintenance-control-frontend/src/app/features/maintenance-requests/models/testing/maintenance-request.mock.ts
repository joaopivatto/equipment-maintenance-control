import { BaseMockFactory } from '../../../../shared/testing';
import { HistoryEntry } from '../history-entry.model';
import { MaintenanceRequest, RequestStatus } from '../maintenance-request.model';
import { BudgetMockFactory } from './budget.mock';

export class MaintenanceRequestMockFactory extends BaseMockFactory<MaintenanceRequest> {
  protected readonly length = 25;

  private readonly budgetFactory = new BudgetMockFactory();

  private readonly statuses = [
    RequestStatus.OPEN,
    RequestStatus.QUOTED,
    RequestStatus.APPROVED,
    RequestStatus.REJECTED,
    RequestStatus.REDIRECTED,
    RequestStatus.REPAIRED,
    RequestStatus.PAID,
    RequestStatus.FINALIZED,
  ];

  private readonly equipmentCategories = ['Notebook', 'Desktop', 'Impressora', 'Mouse', 'Teclado'];

  private readonly equipmentDescriptions = [
    'Computador',
    'Celular',
    'Notebook',
    'Tablet',
    'Monitor',
    'Impressora',
  ];

  private readonly defectDescriptions = [
    'Não liga',
    'Tela quebrada',
    'Bateria não carrega',
    'Superaquecimento',
    'Lentidão excessiva',
    'Sem conexão com a rede',
  ];

  private readonly rejectionReasons = ['Custo do reparo inviável', 'Equipamento fora de garantia'];

  private readonly customerNames = [
    'João da Silva',
    'José Pereira',
    'Joana Fernandes',
    'Joaquina Martins',
  ];

  private readonly employeeNames = [
    'Matheus Smith',
    'João Guilherme Johnson',
    'Samuel Brown',
    'João Davis',
    'Saulo Miller',
    'Razer Doe',
  ];

  protected build(index: number): MaintenanceRequest {
    const status = this.statuses[index % this.statuses.length];
    const isBudgeted = status !== RequestStatus.OPEN;
    const createdAt = new Date(
      Date.now() - (this.length - index) * 7 * 24 * 60 * 60 * 1000,
    ).toISOString();

    const request: MaintenanceRequest = {
      id: index + 1,
      createdAt,
      equipmentDescription: this.equipmentDescriptions[index % this.equipmentDescriptions.length],
      equipmentCategoryId: (index % this.equipmentCategories.length) + 1,
      equipmentCategoryName: this.equipmentCategories[index % this.equipmentCategories.length],
      defectDescription: this.defectDescriptions[index % this.defectDescriptions.length],
      status,
      customerName: this.customerNames[index % this.customerNames.length],
      history: this.buildHistory(status, createdAt, index),
    };

    if (isBudgeted) {
      request.budget = {
        ...this.budgetFactory.generate(),
        createdAt: new Date(new Date(createdAt).getTime() + 2 * 60 * 60 * 1000).toISOString(),
      };
    }

    if (status === RequestStatus.REJECTED) {
      request.rejectionReason = this.rejectionReasons[index % this.rejectionReasons.length];
    }

    if (
      status === RequestStatus.REDIRECTED ||
      status === RequestStatus.REPAIRED ||
      status === RequestStatus.PAID ||
      status === RequestStatus.FINALIZED
    ) {
      request.assignedEmployeeId = (index % this.employeeNames.length) + 1;
      request.assignedEmployeeName = this.employeeNames[index % this.employeeNames.length];
    }

    if (status === RequestStatus.PAID || status === RequestStatus.FINALIZED) {
      request.paidAt = new Date(new Date(createdAt).getTime() + 74 * 60 * 60 * 1000).toISOString();
    }

    if (status === RequestStatus.FINALIZED) {
      request.finalizedAt = new Date(
        new Date(createdAt).getTime() + 98 * 60 * 60 * 1000,
      ).toISOString();
      request.finalizedByEmployeeName = request.assignedEmployeeName;
    }

    return request;
  }

  // Monta o histórico percorrendo, em ordem, apenas os estados possíveis
  // (RequestStatus) pelos quais a solicitação realmente passou até chegar
  // ao status atual, no mesmo formato gerado pelas transições reais da API.
  private buildHistory(status: RequestStatus, createdAt: string, index: number): HistoryEntry[] {
    const responsible = this.employeeNames[index % this.employeeNames.length];
    const baseTime = new Date(createdAt).getTime();
    const hoursAfter = (hours: number) => new Date(baseTime + hours * 60 * 60 * 1000).toISOString();

    const history: HistoryEntry[] = [{ status: RequestStatus.OPEN, dateTime: createdAt }];

    if (status === RequestStatus.OPEN) {
      return history;
    }

    history.push({ status: RequestStatus.QUOTED, dateTime: hoursAfter(2) });

    if (status === RequestStatus.QUOTED) {
      return history;
    }

    if (status === RequestStatus.REJECTED) {
      history.push({
        status: RequestStatus.REJECTED,
        dateTime: hoursAfter(26),
        reason: this.rejectionReasons[index % this.rejectionReasons.length],
      });
      return history;
    }

    history.push({ status: RequestStatus.APPROVED, dateTime: hoursAfter(26) });

    if (status === RequestStatus.APPROVED) {
      return history;
    }

    if (status === RequestStatus.REDIRECTED) {
      history.push({
        status: RequestStatus.REDIRECTED,
        dateTime: hoursAfter(34),
        responsible,
      });
      return history;
    }

    history.push({ status: RequestStatus.REPAIRED, dateTime: hoursAfter(50), responsible });

    if (status === RequestStatus.REPAIRED) {
      return history;
    }

    history.push({ status: RequestStatus.PAID, dateTime: hoursAfter(74) });

    if (status === RequestStatus.FINALIZED) {
      history.push({
        status: RequestStatus.FINALIZED,
        dateTime: hoursAfter(98),
        responsible,
      });
    }

    return history;
  }
}
