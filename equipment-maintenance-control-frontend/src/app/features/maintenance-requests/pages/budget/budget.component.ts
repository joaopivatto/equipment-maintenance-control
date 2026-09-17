import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule, CurrencyPipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

// PrimeNG
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { MessageModule } from 'primeng/message';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
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
  private maintenanceRequestApiClient = inject(MaintenanceRequestApiClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private currencyPipe = inject(CurrencyPipe);

  // Disponibiliza a Enum de status para uso no template HTML
  RequestStatus = RequestStatus;

  request: MaintenanceRequest | undefined;

  // Campo para o Funcionário digitar o valor (RF012)
  budgetValue: number | null = null;
  submitting = false;

  // Controle do motivo de rejeição pelo Cliente (RF007)
  showRejectReason = signal(false);
  rejectionReason = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);

    this.maintenanceRequestApiClient.findById(id).subscribe((request) => {
      this.request = request;

      if (!this.request) {
        this.notificationService.error('Erro', 'Solicitação não encontrada.');
        this.router.navigate(['/requests/list']);
        return;
      }

      // Validação Flexível:
      // Se a solicitação não estiver ABERTA (esperando Funcionário)
      // e nem ORÇADA (esperando aprovação do Cliente), redireciona com aviso.
      if (
        this.request.status !== RequestStatus.OPEN &&
        this.request.status !== RequestStatus.QUOTED
      ) {
        this.notificationService.warning(
          'Aviso',
          'Esta solicitação não está aguardando orçamento nem aprovação.',
        );
        this.router.navigate(['/requests/list']);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  // =========================================================================
  // RF012 - EFETUAR ORÇAMENTO (Ação do Funcionário)
  // =========================================================================
  submitBudget(): void { if (!this.request || !this.budgetValue || this.budgetValue <= 0) {
    this.notificationService.warning('Aviso', 'Informe um valor válido de orçamento.'); return; }

    // Chama o método doApiClient que atualiza o registro no mock
    (this.maintenanceRequestApiClient as any).createBudget(this.request.id, this.budgetValue).subscribe({
      next: () => {
        this.notificationService.success('Sucesso', 'Orçamento cadastrado com sucesso!');
        this.router.navigate(['/requests/employee-home']); },
        error: (err: any) => {
          console.error('Erro ao salvar orçamento:', err); } });
    }

  // =========================================================================
  // RF006 - APROVAR SERVIÇO (Ação do Cliente)
  // =========================================================================
  approve(): void {
    if (!this.request) { return; }

    const formattedBudgetValue = this.currencyPipe.transform(
      this.request.budget?.value,
      'BRL'
    );

    this.confirmationService.confirm({
      message: `Confirmar aprovação do orçamento de ${formattedBudgetValue}?`,
      header: 'Aprovar orçamento',
      icon: 'pi pi-check-circle',
      acceptLabel: 'Aprovar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'success' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        if (!this.request) { return; }
        this.maintenanceRequestApiClient.approve(this.request.id).subscribe(() => {
          this.notificationService.success(
            'Orçamento aprovado',
            `Serviço aprovado com valor de ${formattedBudgetValue}.`
          );
          this.router.navigate(['/requests/list']);
        });
      },
    });
  }

  // =========================================================================
  // RF007 - REJEITAR SERVIÇO (Ação do Cliente)
  // =========================================================================
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
        if (!this.request) { return; }
        this.maintenanceRequestApiClient
          .reject(this.request.id, this.rejectionReason.trim())
          .subscribe(() => {
            this.notificationService.success('Orçamento rejeitado', 'A rejeição foi registrada.');
            this.router.navigate(['/requests/list']);
          });
      },
    });
  }
}


