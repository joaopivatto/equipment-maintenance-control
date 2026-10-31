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

  readonly RequestStatus = RequestStatus;

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
    return STATUS_CLASSES[status as RequestStatus] ?? 'bg-gray-200 text-gray-800';
  }

  hasActions(): boolean {
    const request = this.request();
    if (!request) {
      return false;
    }

    return (
      request.status === RequestStatus.QUOTED ||
      request.status === RequestStatus.REJECTED ||
      request.status === RequestStatus.REPAIRED
    );
  }

  // RF005 - Mostrar orçamento
  reviewBudget(): void {
    const request = this.request();
    if (!request) {
      return;
    }

    this.router.navigate(['/requests', request.id, 'budget']);
  }

  // RF009 - Resgatar serviço: REJEITADA -> APROVADA
  recoverService(): void {
    const request = this.request();
    if (!request) {
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

  // RF010 - Pagar serviço: ARRUMADA -> PAGA
  payForService(): void {
    const request = this.request();
    if (request) {
      this.router.navigate(['/requests', request.id, 'payment']);
    }
  }
}
