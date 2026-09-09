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

const ESTADO_SEVERITY: Record<
  RequestStatus,
  'secondary' | 'info' | 'success' | 'danger' | 'contrast'
> = {
  [RequestStatus.ABERTA]: 'secondary',
  [RequestStatus.ORCADA]: 'info',
  [RequestStatus.APROVADA]: 'success',
  [RequestStatus.REJEITADA]: 'danger',
  [RequestStatus.ARRUMADA]: 'contrast',
  [RequestStatus.PAGA]: 'contrast',
};

@Component({
  selector: 'app-cliente-home',
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
  templateUrl: './cliente-home.component.html',
  styleUrl: './cliente-home.component.scss',
})
export class ClienteHomeComponent implements OnInit {
  private maintenanceRequestService = inject(MaintenanceRequestService); // Injeção de dependência via inject()
  private location = inject(Location);
  private router = inject(Router);
  public solicitacoes: MaintenanceRequest[] = [];

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
    this.solicitacoes = this.maintenanceRequestService
      .listarTodas()
      .sort((a: MaintenanceRequest, b: MaintenanceRequest) => {
        return new Date(a.dataHora).getTime() - new Date(b.dataHora).getTime();
      });
    this.isLoading.set(false);
  }

  aprovarRejeitar(id: number) {
    this.router.navigate(['/requests', id, 'budget']);
  }
  resgatar(id: number) {
    alert(`Resgatando serviço #${id} (RF009)`);
  }
  pagar(id: number) {
    alert(`Ir para Pagar Serviço #${id} (RF010)`);
  }
  visualizar(id: number) {
    alert(`Visualizando dados e histórico da solicitação #${id} (RF008)`);
  }

  estadoSeverity(estado: RequestStatus) {
    return ESTADO_SEVERITY[estado] ?? 'secondary';
  }

  readonly RequestStatus = RequestStatus;

  goBack(): void {
    this.location.back();
  }
}
