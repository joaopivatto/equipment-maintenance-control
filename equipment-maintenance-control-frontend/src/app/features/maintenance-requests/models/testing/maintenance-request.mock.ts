import { BaseMockFactory } from '../../../../shared/testing';
import { MaintenanceRequest, RequestStatus } from '../maintenance-request.model';
import { BudgetMockFactory } from './budget.mock';
import { HistoryEntryMockFactory } from './history-entry.mock';

export class MaintenanceRequestMockFactory extends BaseMockFactory<MaintenanceRequest> {
  protected readonly length = 25;

  private readonly budgetFactory = new BudgetMockFactory();
  private readonly historyFactory = new HistoryEntryMockFactory();

  private readonly statuses = [
    RequestStatus.ABERTA,
    RequestStatus.ORCADA,
    RequestStatus.APROVADA,
    RequestStatus.REJEITADA,
    RequestStatus.ARRUMADA,
    RequestStatus.PAGA,
  ];

  private readonly equipamentos = ['Computador', 'Celular', 'Notebook', 'Tablet', 'Monitor', 'Impressora'];

  private readonly defeitos = [
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
    const isBudgeted = status !== RequestStatus.ABERTA;

    const request: MaintenanceRequest = {
      id: index + 1,
      dataHora: new Date(Date.UTC(2026, 0, 1 + index, 8, 0, 0)).toISOString(),
      descricaoEquipamento: this.equipamentos[index % this.equipamentos.length],
      descricaoDefeito: this.defeitos[index % this.defeitos.length],
      estado: status,
      history: [this.historyFactory.generate({ status })],
    };

    if (isBudgeted) {
      request.budget = this.budgetFactory.generate();
    }

    if (status === RequestStatus.REJEITADA) {
      request.rejectionReason = this.rejectionReasons[index % this.rejectionReasons.length];
    }

    return request;
  }
}
