import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';

import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { MaintenanceRequest } from '../../models/maintenance-request.model';

const ESTADO_SEVERITY: Record<string, 'secondary' | 'info' | 'success' | 'danger' | 'contrast'> = {
  ABERTA: 'secondary',
  ORÇADA: 'info',
  APROVADA: 'success',
  REJEITADA: 'danger',
  ARRUMADA: 'contrast',
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
    // Busca as solicitações e as ordena de forma crescente por data/hora
    // Tipagem explícita de (a: MaintenanceRequest, b: MaintenanceRequest) resolve os erros TS7006
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

  // Métodos de ação fictícios para testar cliques na tela
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

  estadoSeverity(estado: string) {
    return ESTADO_SEVERITY[estado] ?? 'secondary';
  }

  goBack(): void {
    this.location.back();
  }
}
