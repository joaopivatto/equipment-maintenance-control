import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { EmployeeApiClient } from '../../api/employee-api-client';
import { Employee } from '../../models/employee.model';
import { SkeletonModule } from 'primeng/skeleton';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { CurrentEmployeeService } from '../../services/current-employee.service';

@Component({
  imports: [
    CommonModule,
    RouterLink,
    CardModule,
    TableModule,
    ButtonModule,
    SkeletonModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService],
  selector: 'app-employee-list',
  styleUrl: './employee-list.component.scss',
  templateUrl: './employee-list.component.html',
})
export class EmployeeListComponent implements OnInit {
  private employeeApiClient = inject(EmployeeApiClient);
  private location = inject(Location);
  private notificationService = inject(NotificationService);
  private confirmationService = inject(ConfirmationService);
  private currentEmployeeService = inject(CurrentEmployeeService);

  protected readonly skeletonRows: Employee[] = Array.from(
    { length: 5 },
    () => ({}) as Employee
  );

  isLoading = signal(true);

  readonly currentEmployee = this.currentEmployeeService.currentEmployee;

  employees: Employee[] = [];

  ngOnInit(): void {
    this.reload();
  }

  goBack(): void {
    this.location.back();
  }

  private reload(): void {
    this.isLoading.set(true);

    this.employeeApiClient.listAll().subscribe((employees) => {
      this.employees = employees;
      this.isLoading.set(false);
    });
  }

  remove(employee: Employee): void {
    const loggedEmployee = this.currentEmployee();

    if (!loggedEmployee) {
      this.notificationService.error(
        'Erro',
        'Não foi possível identificar o funcionário logado.'
      );
      return;
    }

    if (employee.id === loggedEmployee.id) {
      this.notificationService.warning(
        'Atenção',
        'Você não pode remover o próprio usuário da sessão.'
      );
      return;
    }

    this.confirmationService.confirm({
      message: `Deseja realmente remover o funcionário "${employee.name}"?`,
      header: 'Confirmar remoção',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Remover',
      rejectLabel: 'Cancelar',
      acceptButtonProps: {
        severity: 'danger',
      },
      rejectButtonProps: {
        severity: 'secondary',
        outlined: true,
      },
      accept: () => {
        this.employeeApiClient
          .deactivate(employee.id, loggedEmployee.id)
          .subscribe(({ error }) => {
            if (error) {
              this.notificationService.warning('Atenção', error);
              return;
            }

            this.reload();
          });
      },
    });
  }
}
