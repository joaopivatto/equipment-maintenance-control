import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

// Módulos do PrimeNG
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';

@Component({
  selector: 'app-employee-home',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    TableModule,
    TagModule,
    ButtonModule
  ],
  templateUrl: './employee-home.component.html'
})
export class EmployeeHomeComponent implements OnInit {
  private apiClient = inject(MaintenanceRequestApiClient);
  private router = inject(Router);

  solicitacoes: MaintenanceRequest[] = [];
  loading: boolean = true;

  ngOnInit(): void {
    this.carregarSolicitacoesAbertas();
  }

  carregarSolicitacoesAbertas(): void {
    this.loading = true;

    this.apiClient.listAll().subscribe({
      next: (res: any) => {
        const lista: any[] = Array.isArray(res) ? res : (res?.content || res?.data || []);

        this.solicitacoes = lista.filter((soli: any) => {
          const statusStr = String(soli.status || '').toUpperCase();
          return statusStr === 'ABERTA' || statusStr === 'OPEN' || soli.status === RequestStatus.OPEN;
        });

        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar solicitações:', err);
        this.loading = false;
      }
    });
  }

  efetuarOrcamento(id: number): void {
    this.router.navigate(['/requests', id, 'budget']);
  }
}

