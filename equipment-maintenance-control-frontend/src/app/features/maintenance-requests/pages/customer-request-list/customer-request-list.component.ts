import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, CurrencyPipe, Location } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SessionService } from '../../../../core/auth/session.service';

const STATUS_SEVERITY: Record <
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
  };

@Component({
  selector: 'app-customer-request-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TableModule,
    TagModule,
    ButtonModule,
    CardModule,
    SkeletonModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService, CurrencyPipe],
  templateUrl: './customer-request-list.component.html',
  styleUrl: './customer-request-list.component.scss',
})
export class CustomerRequestListComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private location = inject(Location);
  private router = inject(Router);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private currencyPipe = inject(CurrencyPipe);
  private sessionService = inject(SessionService);

  requests: MaintenanceRequest[] = [];

  isLoading = signal(true);
  protected readonly skeletonRows: MaintenanceRequest[] = Array.from(
    { length: 5 },
    () => ({}) as MaintenanceRequest,
  );

  readonly RequestStatus = RequestStatus;
  readonly isEmployee = this.sessionService.isEmployee;

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.isLoading.set(true);
    this.requests = this.maintenanceRequestService
      .listAll()
      .sort((a: MaintenanceRequest, b: MaintenanceRequest) => {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      });
    this.isLoading.set(false);
  }

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
      acceptButtonProps: { severity: 'warn' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        const updated = this.maintenanceRequestService.recover(id);
        if (!updated) {
          this.notificationService.error('Erro', 'Não foi possível resgatar o serviço.');
          return;
        }
        this.notificationService.success('Serviço resgatado', 'A solicitação voltou a ser aprovada.');
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
      acceptButtonProps: { severity: 'success' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        const updated = this.maintenanceRequestService.pay(id);
        if (!updated) {
          this.notificationService.error('Erro', 'Não foi possível registrar o pagamento.');
          return;
        }
        this.notificationService.success('Pagamento confirmado', `Serviço pago no valor de ${formattedValue}.`);
        this.reload();
      },
    });
  }

  // RF014 - Efetuar manutenção
  performMaintenance(id: number): void {
    this.router.navigate(['/requests', id, 'maintenance']);
  }

  // RF015 - Redirecionar manutenção
  redirectService(id: number): void {
    this.router.navigate(['/requests', id, 'redirect']);
  }

  statusSeverity(status: RequestStatus) {
    return STATUS_SEVERITY[status] ?? 'secondary';
  }

  goBack(): void {
    this.location.back();
  }
}
