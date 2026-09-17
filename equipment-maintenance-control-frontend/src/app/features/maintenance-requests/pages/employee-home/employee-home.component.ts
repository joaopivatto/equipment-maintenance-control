import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';

@Component({
  selector: 'app-employee-home',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, CardModule, SkeletonModule],
  templateUrl: './employee-home.component.html',
  styleUrl: './employee-home.component.scss',
})
export class EmployeeHomeComponent implements OnInit {
  private readonly maintenanceRequestApiClient = inject(MaintenanceRequestApiClient);
  private readonly router = inject(Router);

  requests: MaintenanceRequest[] = [];

  isLoading = signal(true);

  protected readonly skeletonRows: MaintenanceRequest[] = Array.from(
    { length: 5 },
    () => ({}) as MaintenanceRequest,
  );

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.isLoading.set(true);

    this.maintenanceRequestApiClient.listAll().subscribe((requests) => {
      this.requests = requests
        .filter((request) => request.status === RequestStatus.OPEN)
        .sort(
          (a, b) =>
            this.parseRequestDate(a.createdAt).getTime() -
            this.parseRequestDate(b.createdAt).getTime(),
        );

      this.isLoading.set(false);
    });
  }

  // RF012 - Efetuar orçamento
  performBudget(id: number): void {
    this.router.navigate(['/requests', id, 'budget']);
  }

  equipmentLabel(description: string): string {
    if (!description || description.length <= 30) {
      return description;
    }

    return `${description.slice(0, 30)}…`;
  }

  /**
   * Converte a data da solicitação para Date.
   *
   * As solicitações antigas estão no formato:
   * YYYY-MM-DD HH:mm
   *
   * Enquanto algumas datas são geradas com:
   * YYYY-MM-DDTHH:mm:ss.sssZ
   *
   * O método trata os dois formatos.
   */
  private parseRequestDate(value: string): Date {
    if (value.includes('T')) {
      return new Date(value);
    }

    return new Date(value.replace(' ', 'T'));
  }
}
