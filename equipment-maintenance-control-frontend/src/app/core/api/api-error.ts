import { HttpErrorResponse } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { NotificationService } from '../notifications/notification.service';

interface ApiErrorBody {
  code?: string;
  message?: string;
}

const MESSAGES_BY_CODE: Record<string, string> = {
  CUSTOMER_EMAIL_ALREADY_EXISTS: 'Já existe uma conta cadastrada com este e-mail.',
  CUSTOMER_ALREADY_EXISTS: 'Já existe uma conta cadastrada com este CPF.',
  EMPLOYEE_EMAIL_ALREADY_EXISTS: 'Já existe um funcionário cadastrado com este e-mail.',
  EMPLOYEE_CANNOT_DEACTIVATE_SELF: 'Você não pode remover a si mesmo.',
  EMPLOYEE_ALREADY_INACTIVE: 'Este funcionário já está inativo.',
  EMPLOYEE_LAST_ACTIVE: 'Não é possível remover o único funcionário cadastrado.',
  EMPLOYEE_NOT_FOUND: 'Funcionário não encontrado.',
  CUSTOMER_NOT_FOUND: 'Cliente não encontrado.',
  EQUIPMENT_TYPE_NOT_FOUND: 'Categoria não encontrada.',
  EQUIPMENT_TYPE_ALREADY_INACTIVE: 'Esta categoria já foi removida.',
  MAINTENANCE_REQUEST_NOT_FOUND: 'Solicitação não encontrada.',
  MAINTENANCE_REQUEST_NOT_OWNED: 'Esta solicitação não pertence ao cliente informado.',
  INVALID_MAINTENANCE_REQUEST_STATUS: 'Esta ação não é permitida no estado atual da solicitação.',
  INVALID_STATUS_TRANSITION: 'Esta ação não é permitida no estado atual da solicitação.',
  BUDGET_ALREADY_GIVEN: 'Esta solicitação já possui orçamento.',
  MAINTENANCE_ALREADY_PERFORMED: 'A manutenção desta solicitação já foi efetuada.',
  SELF_REDIRECT_NOT_ALLOWED: 'Não é possível redirecionar a solicitação para si mesmo.',
  INVALID_CREDENTIALS: 'E-mail ou senha inválidos.',
};

const NETWORK_ERROR_MESSAGE = 'Não foi possível conectar ao servidor. Tente novamente.';

function stripFieldNames(message: string): string {
  return message
    .split(';')
    .map((part) => part.replace(/^\s*[\w.[\]]+:\s*/, '').trim())
    .filter(Boolean)
    .join(' ');
}

export function toErrorMessage(error: unknown, fallback: string): string {
  if (!(error instanceof HttpErrorResponse)) {
    return fallback;
  }

  if (error.status === 0) {
    return NETWORK_ERROR_MESSAGE;
  }

  const body = error.error as ApiErrorBody | null;
  if (!body?.code) {
    return fallback;
  }

  const known = MESSAGES_BY_CODE[body.code];
  if (known) {
    return known;
  }

  if (body.code === 'VALIDATION_ERROR' && body.message) {
    return stripFieldNames(body.message) || fallback;
  }

  return fallback;
}

export function notifyAndStop<T>(
  notifications: NotificationService,
  fallback: string,
): (error: unknown) => Observable<T> {
  return (error) => {
    notifications.error('Erro', toErrorMessage(error, fallback));
    return EMPTY;
  };
}
