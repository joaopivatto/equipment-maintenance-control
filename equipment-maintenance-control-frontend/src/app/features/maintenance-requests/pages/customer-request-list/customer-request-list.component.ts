import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';

const STATUS_SEVERITY: Record<
  RequestStatus,
  'secondary' | 'info' | 'success' | 'danger' | 'contrast'
> = {
  [RequestStatus.OPEN]: 'secondary',
  [RequestStatus.QUOTED]: 'info',
  [RequestStatus.APPROVED]: 'success',
  [RequestStatus.REJECTED]: 'danger',
  [RequestStatus.REPAIRED]: 'contrast',
  [RequestStatus.PAID]: 'contrast',
};

@Component({
  selector: 'app-customer-request-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TableModule,
    TagModule,
    ButtonModule,
    CardModule,
    SkeletonModule,
  ],
  templateUrl: './customer-request-list.component.html',
  styleUrl: './customer-request-list.component.scss',
})
export class CustomerRequestListComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private location = inject(Location);
  private router = inject(Router);
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
    this.requests = this.maintenanceRequestService
      .listAll()
      .sort((a: MaintenanceRequest, b: MaintenanceRequest) => {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      });
    this.isLoading.set(false);
  }

  reviewBudget(id: number): void {
    this.router.navigate(['/requests', id, 'budget']);
  }
  recoverService(id: number): void {
    alert(`Resgatando serviço #${id} (RF009)`);
  }
  payForService(id: number): void {
    alert(`Ir para Pagar Serviço #${id} (RF010)`);
  }
  viewDetails(id: number): void {
    alert(`Visualizando dados e histórico da solicitação #${id} (RF008)`);
  }

  statusSeverity(status: RequestStatus) {
    return STATUS_SEVERITY[status] ?? 'secondary';
  }

  readonly RequestStatus = RequestStatus;

  goBack(): void {
    this.location.back();
  }
}
