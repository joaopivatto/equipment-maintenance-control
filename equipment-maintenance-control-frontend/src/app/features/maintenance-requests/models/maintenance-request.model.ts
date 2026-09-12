import { Budget } from './budget.model';
import { HistoryEntry } from './history-entry.model';

export enum RequestStatus {
  OPEN = 'ABERTA',
  QUOTED = 'ORÇADA',
  APPROVED = 'APROVADA',
  REJECTED = 'REJEITADA',
  REDIRECTED = 'REDIRECIONADA',
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

  // RF014 - Efetuar manutenção
  maintenanceDescription?: string;
  maintenanceInstructions?: string;

  // Funcionário responsável atual pela solicitação (orçamento, manutenção ou redirecionamento)
  assignedEmployeeId?: number;
  assignedEmployeeName?: string;

  // RF010 - Pagar serviço
  paidAt?: string;

  history: HistoryEntry[];
}
