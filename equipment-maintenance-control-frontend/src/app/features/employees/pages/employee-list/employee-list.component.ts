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

@Component({
  imports: [CommonModule, RouterLink, CardModule, TableModule, ButtonModule, SkeletonModule],
  selector: 'app-employee-list',
  styleUrl: './employee-list.component.scss',
  templateUrl: './employee-list.component.html',
})
export class EmployeeListComponent implements OnInit {
  private employeeApiClient = inject(EmployeeApiClient);
  private location = inject(Location);
  private notificationService = inject(NotificationService);

  protected readonly skeletonRows: Employee[] = Array.from({ length: 5 }, () => ({}) as Employee);
  isLoading = signal(true);

  // TODO: substituir por AuthService real quando o login/sessão estiver implementado
  private currentEmployeeId = 1;

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
    // Confirmação obrigatória antes de qualquer remoção (requisito não-funcional)
    if (!confirm(`Deseja realmente remover o funcionário "${employee.name}"?`)) {
      return;
    }

    this.employeeApiClient
      .deactivate(employee.id, this.currentEmployeeId)
      .subscribe(({ error }) => {
        if (error) {
          this.notificationService.warning('Atenção', error);
          return;
        }

        this.reload();
      });
  }
}
