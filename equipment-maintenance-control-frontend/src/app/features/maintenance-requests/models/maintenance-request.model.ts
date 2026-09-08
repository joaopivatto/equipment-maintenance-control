import { Budget } from './budget.model';
import { HistoryEntry } from './history-entry.model';

export interface MaintenanceRequest {
  id: number;
  dataHora: string;
  descricaoEquipamento: string;
  descricaoDefeito: string;
  estado: string; // Estados previstos: ABERTA, ORÇADA, APROVADA, REJEITADA, ARRUMADA, PAGA, etc.
  budget?: Budget;
  rejectionReason?: string;
  history: HistoryEntry[];
}
