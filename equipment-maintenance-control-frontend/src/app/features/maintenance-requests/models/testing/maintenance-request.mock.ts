import { BaseMockFactory } from '../../../../shared/testing';
import { MaintenanceRequest, RequestStatus } from '../maintenance-request.model';
import { BudgetMockFactory } from './budget.mock';
import { HistoryEntryMockFactory } from './history-entry.mock';

export class MaintenanceRequestMockFactory extends BaseMockFactory<MaintenanceRequest> {
  protected readonly length = 25;

  private readonly budgetFactory = new BudgetMockFactory();
  private readonly historyFactory = new HistoryEntryMockFactory();

  private readonly statuses = [
    RequestStatus.OPEN,
    RequestStatus.QUOTED,
    RequestStatus.APPROVED,
    RequestStatus.REJECTED,
    RequestStatus.REPAIRED,
    RequestStatus.PAID,
  ];

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

  protected build(index: number): MaintenanceRequest {
    const status = this.statuses[index % this.statuses.length];
    const isBudgeted = status !== RequestStatus.OPEN;

    const request: MaintenanceRequest = {
      id: index + 1,
      createdAt: new Date(Date.UTC(2026, 0, 1 + index, 8, 0, 0)).toISOString(),
      equipmentDescription: this.equipmentDescriptions[index % this.equipmentDescriptions.length],
      defectDescription: this.defectDescriptions[index % this.defectDescriptions.length],
      status,
      history: [this.historyFactory.generate({ status })],
    };

    if (isBudgeted) {
      request.budget = this.budgetFactory.generate();
    }

    if (status === RequestStatus.REJECTED) {
      request.rejectionReason = this.rejectionReasons[index % this.rejectionReasons.length];
    }

    return request;
  }
}
