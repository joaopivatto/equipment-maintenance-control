import { ApiEmployeeDto } from '../../../employees/api/dto/api-employee.dto';
import { Budget } from '../../models/budget.model';
import { HistoryEntry } from '../../models/history-entry.model';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import {
  ApiBudgetDto,
  ApiHistoryEntryDto,
  ApiMaintenanceRequestDto,
} from '../dto/api-maintenance-request.dto';

const STATUS_BY_API_VALUE: Record<string, RequestStatus> = {
  ABERTA: RequestStatus.OPEN,
  ORCADA: RequestStatus.QUOTED,
  APROVADA: RequestStatus.APPROVED,
  REJEITADA: RequestStatus.REJECTED,
  REDIRECIONADA: RequestStatus.REDIRECTED,
  ARRUMADA: RequestStatus.REPAIRED,
  PAGA: RequestStatus.PAID,
  FINALIZADA: RequestStatus.FINALIZED,
};

export function toRequestStatus(value: string): RequestStatus {
  return STATUS_BY_API_VALUE[value] ?? (value as RequestStatus);
}

const BACKEND_DATE_TIME = /^(\d{2})\/(\d{2})\/(\d{4})(?:\s+(\d{2}):(\d{2}))?$/;

export function toIsoDateTime(value: string): string;
export function toIsoDateTime(value: string | undefined): string | undefined;
export function toIsoDateTime(value: string | undefined): string | undefined {
  if (!value) {
    return undefined;
  }

  const match = BACKEND_DATE_TIME.exec(value);
  if (!match) {
    return value;
  }

  const [, day, month, year, hour = '00', minute = '00'] = match;
  return `${year}-${month}-${day}T${hour}:${minute}:00`;
}

function toBudget(dto: ApiBudgetDto): Budget {
  return {
    id: dto.id,
    value: Number(dto.value),
    createdAt: toIsoDateTime(dto.createdAt),
    employeeId: dto.employee?.id ?? 0,
  };
}

function nameOf(employee: ApiEmployeeDto | undefined): string | undefined {
  return employee?.name;
}

function toHistoryEntry(dto: ApiHistoryEntryDto): HistoryEntry {
  return {
    status: toRequestStatus(dto.status),
    dateTime: toIsoDateTime(dto.dateTime),
    responsible: nameOf(dto.responsible),
    reason: dto.reason,
    fromEmployee: nameOf(dto.fromEmployee),
    toEmployee: nameOf(dto.toEmployee),
  };
}

export function toMaintenanceRequest(dto: ApiMaintenanceRequestDto): MaintenanceRequest {
  return {
    id: dto.id,
    createdAt: toIsoDateTime(dto.createdAt),
    equipmentDescription: dto.equipment?.description ?? '',
    equipmentCategoryId: dto.equipment?.type?.id,
    equipmentCategoryName: dto.equipment?.type?.description,
    defectDescription: dto.defectDescription,
    status: toRequestStatus(dto.status),
    budget: dto.budget ? toBudget(dto.budget) : undefined,
    rejectionReason: dto.rejectionReason,
    customerName: dto.customer?.name,
    maintenanceDescription: dto.maintenance?.description,
    maintenanceInstructions: dto.maintenance?.instructions,
    assignedEmployeeId: dto.assignedEmployee?.id,
    assignedEmployeeName: nameOf(dto.assignedEmployee),
    paidAt: toIsoDateTime(dto.paidAt),
    finalizedAt: toIsoDateTime(dto.finalizedAt),
    finalizedByEmployeeName: nameOf(dto.finalizedBy),
    history: (dto.history ?? []).map(toHistoryEntry),
  };
}
