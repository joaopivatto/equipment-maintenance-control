import { Component, OnInit, inject, computed, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { SelectModule } from 'primeng/select';
import { MessageModule } from 'primeng/message';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SessionService } from '../../../../core/auth/session.service';
import { EmployeeService } from '../../../employees/services/employee.service';
import { Employee } from '../../../employees/models/employee.model';

@Component({
  selector: 'app-redirect',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    ButtonModule,
    TagModule,
    SelectModule,
    MessageModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService],
  templateUrl: './redirect.component.html',
  styleUrl: './redirect.component.scss',
})
export class RedirectComponent implements OnInit {
  private maintenanceRequestApiClient = inject(MaintenanceRequestApiClient);
  private employeeService = inject(EmployeeService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private sessionService = inject(SessionService);

  request: MaintenanceRequest | undefined;
  selectedEmployeeId: number | null = null;

  // Funcionário logado, correlacionado via e-mail (SessionUser.id != Employee.id)
  readonly currentEmployee = computed<Employee | undefined>(() => {
    const email = this.sessionService.currentUser()?.email;
    return email ? this.employeeService.findByEmail(email) : undefined;
  });

  // Impede que o próprio funcionário logado apareça como opção de destino (RF015)
  readonly availableEmployees = computed<Employee[]>(() => {
    const currentEmployeeId = this.currentEmployee()?.id;
    return this.employeeService.listAll().filter((employee) => employee.id !== currentEmployeeId);
  });

  errorMessage = signal<string | null>(null);

  get fromEmployeeName(): string {
    return this.currentEmployee()?.name ?? 'Funcionário não identificado';
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);
    this.maintenanceRequestApiClient.findById(id).subscribe((request) => {
      this.request = request;

      if (!this.request) {
        this.notificationService.error('Erro', 'Solicitação não encontrada.');
        this.router.navigate(['/requests/list']);
        return;
      }

      if (
        this.request.status !== RequestStatus.APPROVED &&
        this.request.status !== RequestStatus.QUOTED
      ) {
        this.notificationService.warning(
          'Aviso',
          'Esta solicitação não pode mais ser redirecionada neste estado.',
        );
        this.router.navigate(['/requests/list']);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  // RF015 - Redirecionar manutenção
  confirmRedirect(): void {
    this.errorMessage.set(null);

    if (!this.request) {
      return;
    }

    if (!this.selectedEmployeeId) {
      this.errorMessage.set('Selecione o funcionário de destino.');
      return;
    }

    const fromEmployee = this.currentEmployee();
    if (!fromEmployee) {
      this.notificationService.error(
        'Erro',
        'Não foi possível identificar o funcionário logado no cadastro de funcionários.',
      );
      return;
    }

    // Bloqueio adicional (além do filtro na lista): redirecionar para si mesmo
    if (this.selectedEmployeeId === fromEmployee.id) {
      this.errorMessage.set('Não é possível redirecionar a solicitação para si mesmo.');
      return;
    }

    const toEmployee = this.availableEmployees().find((e) => e.id === this.selectedEmployeeId);
    if (!toEmployee) {
      this.errorMessage.set('Funcionário de destino inválido.');
      return;
    }

    this.confirmationService.confirm({
      message: `Confirmar o redirecionamento desta solicitação para ${toEmployee.name}?`,
      header: 'Redirecionar manutenção',
      icon: 'pi pi-directions',
      acceptLabel: 'Redirecionar',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'secondary' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        if (!this.request) {
          return;
        }

        this.maintenanceRequestApiClient
          .redirect(
            this.request.id,
            fromEmployee.id,
            fromEmployee.name,
            toEmployee.id,
            toEmployee.name,
          )
          .subscribe((result) => {
            if (result.error) {
              this.notificationService.error('Erro', result.error);
              return;
            }

            this.notificationService.success(
              'Solicitação redirecionada',
              `Encaminhada para ${toEmployee.name}.`,
            );
            this.router.navigate(['/requests/list']);
          });
      },
    });
  }
}
