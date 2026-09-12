import { Component, OnInit, inject, computed } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SessionService } from '../../../../core/auth/session.service';
import { EmployeeService } from '../../../employees/services/employee.service';

@Component({
  selector: 'app-maintenance',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    ButtonModule,
    TagModule,
    TextareaModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService],
  templateUrl: './maintenance.component.html',
  styleUrl: './maintenance.component.scss',
})
export class MaintenanceComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private employeeService = inject(EmployeeService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private sessionService = inject(SessionService);

  request: MaintenanceRequest | undefined;

  maintenanceDescription = '';
  maintenanceInstructions = '';

  // Funcionário logado, correlacionado via e-mail (SessionUser.id != Employee.id)
  readonly currentEmployee = computed(() => {
    const email = this.sessionService.currentUser()?.email;
    return email ? this.employeeService.findByEmail(email) : undefined;
  });

  get responsibleEmployeeName(): string {
    return this.currentEmployee()?.name ?? 'Funcionário não identificado';
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);
    this.request = this.maintenanceRequestService.findById(id);

    if (!this.request) {
      this.notificationService.error('Erro', 'Solicitação não encontrada.');
      this.router.navigate(['/requests/list']);
      return;
    }

    if (this.request.status !== RequestStatus.APPROVED) {
      this.notificationService.warning(
        'Aviso',
        'Esta solicitação não está mais aguardando manutenção.',
      );
      this.router.navigate(['/requests/list']);
    }
  }

  goBack(): void {
    this.location.back();
  }

  // RF014 - Efetuar manutenção: APROVADA -> ARRUMADA
  confirmMaintenance(): void {
    if (!this.request) {
      return;
    }

    if (!this.maintenanceDescription.trim() || !this.maintenanceInstructions.trim()) {
      this.notificationService.warning(
        'Aviso',
        'Informe a descrição da manutenção e as orientações antes de confirmar.',
      );
      return;
    }

    const employee = this.currentEmployee();
    if (!employee) {
      this.notificationService.error(
        'Erro',
        'Não foi possível identificar o funcionário logado no cadastro de funcionários.',
      );
      return;
    }

    this.confirmationService.confirm({
      message: 'Confirmar a conclusão da manutenção? A solicitação mudará para "Arrumada".',
      header: 'Efetuar manutenção',
      icon: 'pi pi-wrench',
      acceptLabel: 'Confirmar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'success' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        if (!this.request) {
          return;
        }
        this.maintenanceRequestService.performMaintenance(
          this.request.id,
          this.maintenanceDescription.trim(),
          this.maintenanceInstructions.trim(),
          employee.id,
          employee.name,
        );
        this.notificationService.success(
          'Manutenção concluída',
          'A solicitação foi marcada como Arrumada.',
        );
        this.router.navigate(['/requests/list']);
      },
    });
  }
}
