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
  FINALIZED = 'FINALIZADA',
}

// Escala de cores oficiais definida no RF013 (mesmos valores usados no backend/MaintenanceRequestStatus)
export const STATUS_COLORS: Record<RequestStatus, string> = {
  [RequestStatus.OPEN]: '#808080', // Cinza
  [RequestStatus.QUOTED]: '#8B4513', // Marrom
  [RequestStatus.REJECTED]: '#DC3545', // Vermelho
  [RequestStatus.APPROVED]: '#FFC107', // Amarelo
  [RequestStatus.REDIRECTED]: '#6F42C1', // Roxo
  [RequestStatus.REPAIRED]: '#007BFF', // Azul
  [RequestStatus.PAID]: '#FD7E14', // Alaranjado
  [RequestStatus.FINALIZED]: '#28A745', // Verde
};

export interface MaintenanceRequest {
  id: number;
  createdAt: string;
  equipmentDescription: string;
  defectDescription: string;
  status: RequestStatus;
  budget?: Budget;
  rejectionReason?: string;

  // Nome do cliente que abriu a solicitação (exibido no RF011/RF013)
  customerName?: string;

  // RF014 - Efetuar manutenção
  maintenanceDescription?: string;
  maintenanceInstructions?: string;

  // Funcionário responsável atual pela solicitação (orçamento, manutenção ou redirecionamento)
  assignedEmployeeId?: number;
  assignedEmployeeName?: string;

  // RF010 - Pagar serviço
  paidAt?: string;

  // RF016 - Finalizar solicitação
  finalizedAt?: string;
  finalizedByEmployeeName?: string;

  history: HistoryEntry[];
}
