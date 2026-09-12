import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, CurrencyPipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { MessageModule } from 'primeng/message';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import { NotificationService } from '../../../../core/notifications/notification.service';

@Component({
  selector: 'app-budget',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    ButtonModule,
    TagModule,
    TextareaModule,
    MessageModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService, CurrencyPipe],
  templateUrl: './budget.component.html',
  styleUrl: './budget.component.scss',
})
export class BudgetComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private currencyPipe = inject(CurrencyPipe);

  request: MaintenanceRequest | undefined;

  // Controla a exibição do campo de motivo antes de confirmar a rejeição (RF007)
  showRejectReason = signal(false);
  rejectionReason = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);
    this.request = this.maintenanceRequestService.findById(id);

    if (!this.request) {
      this.notificationService.error('Erro', 'Solicitação não encontrada.');
      this.router.navigate(['/requests/list']);
      return;
    }

    if (this.request.status !== RequestStatus.QUOTED) {
      this.notificationService.warning(
        'Aviso',
        'Esta solicitação não está mais aguardando aprovação de orçamento.',
      );
      this.router.navigate(['/requests/list']);
    }
  }

  goBack(): void {
    this.location.back();
  }

  // RF006 - Aprovar serviço
  approve(): void {
    if (!this.request) {
      return;
    }

    const formattedBudgetValue = this.currencyPipe.transform(this.request.budget?.value, 'BRL');

    this.confirmationService.confirm({
      message: `Confirmar aprovação do orçamento de ${formattedBudgetValue}?`,
      header: 'Aprovar orçamento',
      icon: 'pi pi-check-circle',
      acceptLabel: 'Aprovar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'success' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        if (!this.request) {
          return;
        }
        this.maintenanceRequestService.approve(this.request.id);
        this.notificationService.success(
          'Orçamento aprovado',
          `Serviço aprovado com valor de ${formattedBudgetValue}.`,
        );
        this.router.navigate(['/requests/list']);
      },
    });
  }

  // RF007 - Rejeitar serviço (abre o campo de motivo)
  openRejectReason(): void {
    this.showRejectReason.set(true);
  }

  cancelReject(): void {
    this.showRejectReason.set(false);
    this.rejectionReason = '';
  }

  confirmReject(): void {
    if (!this.request || !this.rejectionReason.trim()) {
      this.notificationService.warning('Aviso', 'Informe o motivo da rejeição.');
      return;
    }

    this.confirmationService.confirm({
      message: 'Tem certeza que deseja rejeitar este orçamento? Essa ação não pode ser desfeita.',
      header: 'Rejeitar orçamento',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Rejeitar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'danger' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        if (!this.request) {
          return;
        }
        this.maintenanceRequestService.reject(this.request.id, this.rejectionReason.trim());
        this.notificationService.success('Orçamento rejeitado', 'A rejeição foi registrada.');
        this.router.navigate(['/requests/list']);
      },
    });
  }
}
