import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TimelineModule } from 'primeng/timeline';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import {
  MaintenanceRequest,
  RequestStatus,
  STATUS_CLASSES,
} from '../../models/maintenance-request.model';
import { HistoryEntry } from '../../models/history-entry.model';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SessionService } from '../../../../core/auth/session.service';

@Component({
  selector: 'app-request-details',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule, TimelineModule, ConfirmDialogModule],
  providers: [ConfirmationService],
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
  private sessionService = inject(SessionService);

  readonly RequestStatus = RequestStatus;
  readonly isCustomer = this.sessionService.isCustomer;

  readonly request = signal<MaintenanceRequest | undefined>(undefined);
  readonly timelineEvents = computed(() => {
    const request = this.request();
    return request ? this.buildTimeline(request) : [];
  });

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
    this.request.set(request);
  }

  // O backend já entrega o histórico em ordem cronológica; reordenar aqui perderia a sequência
  // das transições gravadas no mesmo minuto, que a data formatada não distingue.
  private buildTimeline(request: MaintenanceRequest): HistoryEntry[] {
    if (request.history.length > 0) {
      return request.history;
    }

    return [{ status: RequestStatus.OPEN, dateTime: request.createdAt }];
  }

  goBack(): void {
    this.location.back();
  }

  statusColor(status: string): string {
    return STATUS_CLASSES[status as RequestStatus] ?? 'bg-gray-200 text-gray-800';
  }

  hasActions(): boolean {
    return this.canReviewBudget() || this.canRecoverService() || this.canPayForService();
  }

  canReviewBudget(): boolean {
    return this.isCustomerActionAvailable(RequestStatus.QUOTED);
  }

  // RF009 - Resgatar serviço é uma ação exclusiva do cliente
  canRecoverService(): boolean {
    return this.isCustomerActionAvailable(RequestStatus.REJECTED);
  }

  canPayForService(): boolean {
    return this.isCustomerActionAvailable(RequestStatus.REPAIRED);
  }

  private isCustomerActionAvailable(status: RequestStatus): boolean {
    return this.isCustomer() && this.request()?.status === status;
  }

  reviewBudget(): void {
    const request = this.request();
    if (!request || !this.canReviewBudget()) {
      return;
    }

    this.router.navigate(['/requests', request.id, 'budget']);
  }

  recoverService(): void {
    const request = this.request();
    if (!request || !this.canRecoverService()) {
      return;
    }

    const id = request.id;

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

  payForService(): void {
    const request = this.request();
    if (!request || !this.canPayForService()) {
      return;
    }

    this.router.navigate(['/requests', request.id, 'payment']);
  }
}
