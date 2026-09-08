import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { MessageModule } from 'primeng/message';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { MaintenanceRequest } from '../../models/maintenance-request.model';
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
  ],
  templateUrl: './budget.component.html',
  styleUrl: './budget.component.scss',
})
export class BudgetComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);

  solicitacao: MaintenanceRequest | undefined;

  // Controla a exibição do campo de motivo antes de confirmar a rejeição (RF007)
  showRejectReason = signal(false);
  rejectionReason = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);
    this.solicitacao = this.maintenanceRequestService.findById(id);

    if (!this.solicitacao) {
      this.notificationService.error('Erro', 'Solicitação não encontrada.');
      this.router.navigate(['/requests/list']);
      return;
    }

    if (this.solicitacao.estado !== 'ORÇADA') {
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
    if (!this.solicitacao) {
      return;
    }
    if (
      !confirm(
        `Confirmar aprovação do orçamento de R$ ${this.solicitacao.budget?.value.toFixed(2)}?`,
      )
    ) {
      return;
    }

    this.maintenanceRequestService.approve(this.solicitacao.id);
    this.notificationService.success(
      'Orçamento aprovado',
      `Serviço aprovado com valor de R$ ${this.solicitacao.budget?.value.toFixed(2)}.`,
    );
    this.router.navigate(['/requests/list']);
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
    if (!this.solicitacao || !this.rejectionReason.trim()) {
      this.notificationService.warning('Aviso', 'Informe o motivo da rejeição.');
      return;
    }

    this.maintenanceRequestService.reject(this.solicitacao.id, this.rejectionReason.trim());
    this.notificationService.success('Orçamento rejeitado', 'A rejeição foi registrada.');
    this.router.navigate(['/requests/list']);
  }
}
