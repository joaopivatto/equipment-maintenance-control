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

// Cores do RF013 usadas pelos gráficos e pelos indicadores das telas.
export const STATUS_COLORS: Record<RequestStatus, string> = {
  [RequestStatus.OPEN]: '#4b5563',
  [RequestStatus.QUOTED]: '#92400e',
  [RequestStatus.APPROVED]: '#facc15',
  [RequestStatus.REJECTED]: '#b91c1c',
  [RequestStatus.REDIRECTED]: '#7e22ce',
  [RequestStatus.REPAIRED]: '#1d4ed8',
  [RequestStatus.PAID]: '#c2410c',
  [RequestStatus.FINALIZED]: '#15803d',
};

// Mesmas cores em classes Tailwind, incluindo a cor legível do texto.
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

export interface CustomerAddress {
  zipCode?: string;
  street?: string;
  number?: number;
  complement?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
}

export interface CustomerInfo {
  name: string;
  email?: string;
  cpf?: string;
  phone?: string;
  address?: CustomerAddress;
}

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
  customer?: CustomerInfo;

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
