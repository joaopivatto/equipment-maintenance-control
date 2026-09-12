import { Budget } from './budget.model';
import { HistoryEntry } from './history-entry.model';

export enum RequestStatus {
  OPEN = 'ABERTA',
  QUOTED = 'ORÇADA',
  APPROVED = 'APROVADA',
  REJECTED = 'REJEITADA',
  REPAIRED = 'ARRUMADA',
  PAID = 'PAGA',
}

export interface MaintenanceRequest {
  id: number;
  createdAt: string;
  equipmentDescription: string;
  defectDescription: string;
  status: RequestStatus;
  budget?: Budget;
  rejectionReason?: string;
  history: HistoryEntry[];
}
