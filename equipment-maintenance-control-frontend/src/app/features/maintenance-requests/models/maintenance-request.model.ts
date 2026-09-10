import { Budget } from './budget.model';
import { HistoryEntry } from './history-entry.model';

export enum RequestStatus {
  ABERTA = 'ABERTA',
  ORCADA = 'ORÇADA',
  APROVADA = 'APROVADA',
  REJEITADA = 'REJEITADA',
  ARRUMADA = 'ARRUMADA',
  PAGA = 'PAGA',
}

export interface MaintenanceRequest {
  id: number;
  dataHora: string;
  descricaoEquipamento: string;
  descricaoDefeito: string;
  estado: RequestStatus;
  budget?: Budget;
  rejectionReason?: string;
  history: HistoryEntry[];
}
