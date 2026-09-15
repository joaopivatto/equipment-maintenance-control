import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TimelineModule } from 'primeng/timeline';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MaintenanceRequest, RequestStatus, STATUS_COLORS } from '../../models/maintenance-request.model';
import { HistoryEntry } from '../../models/history-entry.model';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SessionService } from '../../../../core/auth/session.service';

// Mesma paleta usada na listagem para o cliente (RF003/RF013)
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

@Component({
  selector: 'app-request-details',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule, TagModule, TimelineModule, ConfirmDialogModule],
  providers: [ConfirmationService, CurrencyPipe],
  templateUrl: './request-details.component.html',
  styleUrl: './request-details.component.scss',
})
export class RequestDetailsComponent implements OnInit {
  private maintenanceRequestApiClient = inject(MaintenanceRequestApiClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private currencyPipe = inject(CurrencyPipe);
  private sessionService = inject(SessionService);

  readonly RequestStatus = RequestStatus;
  readonly isEmployee = this.sessionService.isEmployee;

  request: MaintenanceRequest | undefined;
  timelineEvents: HistoryEntry[] = [];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);

    this.maintenanceRequestApiClient.findById(id).subscribe((request) => {
      if (!request) {
        this.notificationService.error('Erro', 'Solicitação não encontrada.');
        this.router.navigate(['/requests/list']);
        return;
      }

      this.setRequest(request);
    });
  }

  private setRequest(request: MaintenanceRequest): void {
    this.request = request;
    this.timelineEvents = this.buildTimeline(request);
  }

  // A linha do tempo do RF008 deve sempre exibir ao menos a criação da solicitação,
  // mesmo quando o histórico ainda não possui eventos registrados
  private buildTimeline(request: MaintenanceRequest): HistoryEntry[] {
    if (request.history.length > 0) {
      return [...request.history].sort(
        (a, b) => new Date(a.dateTime).getTime() - new Date(b.dateTime).getTime(),
      );
    }

    return [{ status: RequestStatus.OPEN, dateTime: request.createdAt }];
  }

  goBack(): void {
    this.location.back();
  }

  statusColor(status: string): string {
    return STATUS_COLORS[status as RequestStatus] ?? '#808080';
  }

  statusSeverity(status: string) {
    return STATUS_SEVERITY[status as RequestStatus] ?? 'secondary';
  }

  hasActions(): boolean {
    if (!this.request) {
      return false;
    }

    return (
      this.request.status === RequestStatus.QUOTED ||
      this.request.status === RequestStatus.REJECTED ||
      this.request.status === RequestStatus.REPAIRED
    );
  }

  // RF005 - Mostrar orçamento
  reviewBudget(): void {
    if (!this.request) {
      return;
    }

    this.router.navigate(['/requests', this.request.id, 'budget']);
  }

  // RF009 - Resgatar serviço: REJEITADA -> APROVADA
  recoverService(): void {
    if (!this.request) {
      return;
    }

    const id = this.request.id;

    this.confirmationService.confirm({
      message: 'Deseja resgatar este serviço? O estado voltará para "Aprovada".',
      header: 'Resgatar serviço',
      icon: 'pi pi-undo',
      acceptLabel: 'Resgatar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'warn' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        this.maintenanceRequestApiClient.recover(id).subscribe((updated) => {
          if (!updated) {
            this.notificationService.error('Erro', 'Não foi possível resgatar o serviço.');
            return;
          }

          this.setRequest(updated);
          this.notificationService.success(
            'Serviço resgatado',
            'A solicitação voltou a ser aprovada.',
          );
        });
      },
    });
  }

  // RF010 - Pagar serviço: ARRUMADA -> PAGA
  payForService(): void {
    if (!this.request) {
      return;
    }

    const id = this.request.id;
    const formattedValue = this.currencyPipe.transform(this.request.budget?.value, 'BRL');

    this.confirmationService.confirm({
      message: `Confirmar o pagamento no valor de ${formattedValue}?`,
      header: 'Pagar serviço',
      icon: 'pi pi-wallet',
      acceptLabel: 'Confirmar pagamento',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'success' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        this.maintenanceRequestApiClient.pay(id).subscribe((updated) => {
          if (!updated) {
            this.notificationService.error('Erro', 'Não foi possível registrar o pagamento.');
            return;
          }

          this.setRequest(updated);
          this.notificationService.success(
            'Pagamento confirmado',
            `Serviço pago no valor de ${formattedValue}.`,
          );
        });
      },
    });
  }
}
