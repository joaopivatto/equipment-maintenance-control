import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule, CurrencyPipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { DatePickerModule } from 'primeng/datepicker';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import {
  MaintenanceRequest,
  RequestStatus,
  STATUS_COLORS,
} from '../../models/maintenance-request.model';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SessionService } from '../../../../core/auth/session.service';
import { EmployeeService } from '../../../employees/services/employee.service';

const STATUS_SEVERITY: Record<
  RequestStatus,
  'secondary' | 'info' | 'success' | 'danger' | 'contrast' | 'warn'
> = {
  [RequestStatus.OPEN]: 'secondary',
  [RequestStatus.QUOTED]: 'info',
  [RequestStatus.APPROVED]: 'success',
  [RequestStatus.REJECTED]: 'danger',
  [RequestStatus.REDIRECTED]: 'warn',
  [RequestStatus.REPAIRED]: 'contrast',
  [RequestStatus.PAID]: 'contrast',
  [RequestStatus.FINALIZED]: 'success',
};

type FilterMode = 'today' | 'period' | 'all';

const FILTER_OPTIONS: { label: string; value: FilterMode }[] = [
  { label: 'Hoje', value: 'today' },
  { label: 'Período', value: 'period' },
  { label: 'Todas', value: 'all' },
];

@Component({
  selector: 'app-customer-request-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    TableModule,
    TagModule,
    ButtonModule,
    CardModule,
    SkeletonModule,
    SelectButtonModule,
    DatePickerModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService, CurrencyPipe],
  templateUrl: './customer-request-list.component.html',
  styleUrl: './customer-request-list.component.scss',
})
export class CustomerRequestListComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private employeeService = inject(EmployeeService);
  private sessionService = inject(SessionService);
  private location = inject(Location);
  private router = inject(Router);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private currencyPipe = inject(CurrencyPipe);

  private allRequests: MaintenanceRequest[] = [];
  requests: MaintenanceRequest[] = [];

  isLoading = signal(true);

  protected readonly skeletonRows: MaintenanceRequest[] = Array.from(
    { length: 5 },
    () => ({}) as MaintenanceRequest,
  );

  readonly RequestStatus = RequestStatus;
  readonly isEmployee = this.sessionService.isEmployee;

  // RF013 - filtros
  readonly filterOptions = FILTER_OPTIONS;

  filterMode: FilterMode = 'today';

  periodStart: Date | null = null;
  periodEnd: Date | null = null;

  // Funcionário logado, correlacionado via e-mail
  readonly currentEmployee = computed(() => {
    const email = this.sessionService.currentUser()?.email;

    return email ? this.employeeService.findByEmail(email) : undefined;
  });

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.isLoading.set(true);

    this.allRequests = this.maintenanceRequestService.listAll();

    this.applyFilters();

    this.isLoading.set(false);
  }

  onFilterModeChange(): void {
    this.applyFilters();
  }

  /**
   * Executado sempre que uma das datas do período é alterada.
   */
  onPeriodChange(): void {
    if (this.filterMode === 'period') {
      this.applyFilters();
    }
  }

  private applyFilters(): void {
    let filtered = [...this.allRequests];

    // Regra de visibilidade específica para funcionários
    if (this.isEmployee()) {
      const currentEmployeeId = this.currentEmployee()?.id;

      filtered = filtered.filter((request) => {
        if (request.status === RequestStatus.REDIRECTED) {
          return request.assignedEmployeeId === currentEmployeeId;
        }

        return true;
      });

      // Filtros de data somente para funcionários
      if (this.filterMode === 'today') {
        const today = new Date();

        filtered = filtered.filter((request) => {
          const requestDate = this.parseRequestDate(request.createdAt);
          return this.isSameDay(requestDate, today);
        });
      } else if (this.filterMode === 'period') {
        if (this.periodStart) {
          const start = this.startOfDay(this.periodStart);

          filtered = filtered.filter((request) => {
            const requestDate = this.parseRequestDate(request.createdAt);
            return requestDate >= start;
          });
        }

        if (this.periodEnd) {
          const end = this.endOfDay(this.periodEnd);

          filtered = filtered.filter((request) => {
            const requestDate = this.parseRequestDate(request.createdAt);
            return requestDate <= end;
          });
        }
      }
    }

    this.requests = filtered.sort(
      (a, b) =>
        this.parseRequestDate(a.createdAt).getTime() - this.parseRequestDate(b.createdAt).getTime(),
    );
  }

  /**
   * Converte a data da solicitação para Date.
   *
   * As solicitações antigas estão no formato:
   * YYYY-MM-DD HH:mm
   *
   * Enquanto algumas datas são geradas com:
   * YYYY-MM-DDTHH:mm:ss.sssZ
   *
   * O método trata os dois formatos.
   */
  private parseRequestDate(value: string): Date {
    if (value.includes('T')) {
      return new Date(value);
    }

    return new Date(value.replace(' ', 'T'));
  }

  private isSameDay(a: Date, b: Date): boolean {
    return (
      a.getFullYear() === b.getFullYear() &&
      a.getMonth() === b.getMonth() &&
      a.getDate() === b.getDate()
    );
  }

  private startOfDay(date: Date): Date {
    const result = new Date(date);

    result.setHours(0, 0, 0, 0);

    return result;
  }

  private endOfDay(date: Date): Date {
    const result = new Date(date);

    result.setHours(23, 59, 59, 999);

    return result;
  }

  // --- Ações do cliente ---

  reviewBudget(id: number): void {
    this.router.navigate(['/requests', id, 'budget']);
  }

  viewDetails(id: number): void {
    alert(`Visualizando dados e histórico da solicitação #${id} (RF008)`);
  }

  // RF009 - Resgatar serviço: REJEITADA -> APROVADA
  recoverService(id: number): void {
    this.confirmationService.confirm({
      message: 'Deseja resgatar este serviço? O estado voltará para "Aprovada".',
      header: 'Resgatar serviço',
      icon: 'pi pi-undo',
      acceptLabel: 'Resgatar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: {
        severity: 'warn',
      },
      rejectButtonProps: {
        severity: 'secondary',
        outlined: true,
      },
      accept: () => {
        const updated = this.maintenanceRequestService.recover(id);

        if (!updated) {
          this.notificationService.error('Erro', 'Não foi possível resgatar o serviço.');

          return;
        }

        this.notificationService.success(
          'Serviço resgatado',
          'A solicitação voltou a ser aprovada.',
        );

        this.reload();
      },
    });
  }

  // RF010 - Pagar serviço: ARRUMADA -> PAGA
  payForService(id: number): void {
    const request = this.maintenanceRequestService.findById(id);
    const formattedValue = this.currencyPipe.transform(request?.budget?.value, 'BRL');

    this.confirmationService.confirm({
      message: `Confirmar o pagamento no valor de ${formattedValue}?`,
      header: 'Pagar serviço',
      icon: 'pi pi-wallet',
      acceptLabel: 'Confirmar pagamento',
      rejectLabel: 'Cancelar',
      acceptButtonProps: {
        severity: 'success',
      },
      rejectButtonProps: {
        severity: 'secondary',
        outlined: true,
      },
      accept: () => {
        const updated = this.maintenanceRequestService.pay(id);

        if (!updated) {
          this.notificationService.error('Erro', 'Não foi possível registrar o pagamento.');

          return;
        }

        this.notificationService.success(
          'Pagamento confirmado',
          `Serviço pago no valor de ${formattedValue}.`,
        );

        this.reload();
      },
    });
  }

  // --- Ações do funcionário ---

  // RF012 - Efetuar orçamento
  performBudget(id: number): void {
    alert(`Ir para Efetuar Orçamento #${id} (RF012)`);
  }

  // RF014 - Efetuar manutenção
  performMaintenance(id: number): void {
    this.router.navigate(['/requests', id, 'maintenance']);
  }

  // RF015 - Redirecionar manutenção
  redirectService(id: number): void {
    this.router.navigate(['/requests', id, 'redirect']);
  }

  // RF016 - Finalizar solicitação: PAGA -> FINALIZADA
  finalizeService(id: number): void {
    const employee = this.currentEmployee();

    if (!employee) {
      this.notificationService.error(
        'Erro',
        'Não foi possível identificar o funcionário logado no cadastro de funcionários.',
      );

      return;
    }

    this.confirmationService.confirm({
      message: 'Confirmar a finalização desta solicitação?',
      header: 'Finalizar solicitação',
      icon: 'pi pi-flag-fill',
      acceptLabel: 'Finalizar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: {
        severity: 'success',
      },
      rejectButtonProps: {
        severity: 'secondary',
        outlined: true,
      },
      accept: () => {
        const updated = this.maintenanceRequestService.finalize(id, employee.id, employee.name);

        if (!updated) {
          this.notificationService.error('Erro', 'Não foi possível finalizar a solicitação.');

          return;
        }

        this.notificationService.success('Solicitação finalizada', 'O atendimento foi concluído.');

        this.reload();
      },
    });
  }

  statusSeverity(status: RequestStatus) {
    return STATUS_SEVERITY[status] ?? 'secondary';
  }

  statusColor(status: RequestStatus): string {
    return STATUS_COLORS[status];
  }

  goBack(): void {
    this.location.back();
  }
}
