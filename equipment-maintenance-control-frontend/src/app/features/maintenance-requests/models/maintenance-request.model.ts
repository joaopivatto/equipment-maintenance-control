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
export const STATUS_CLASSES: Record<RequestStatus, string> = {
  [RequestStatus.OPEN]: 'bg-gray-200 text-gray-800',
  [RequestStatus.QUOTED]: 'bg-taupe-300 text-taupe-800',
  [RequestStatus.APPROVED]: 'bg-yellow-100 text-yellow-800',
  [RequestStatus.REJECTED]: 'bg-red-100 text-red-800',
  [RequestStatus.REDIRECTED]: 'bg-violet-100 text-violet-800',
  [RequestStatus.REPAIRED]: 'bg-blue-100 text-blue-800',
  [RequestStatus.PAID]: 'bg-amber-100 text-amber-800',
  [RequestStatus.FINALIZED]: 'bg-green-100 text-green-800',
};

export interface MaintenanceRequest {
  id: number;
  createdAt: string;
  equipmentDescription: string;
  equipmentCategoryId?: number;
  equipmentCategoryName?: string;
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
