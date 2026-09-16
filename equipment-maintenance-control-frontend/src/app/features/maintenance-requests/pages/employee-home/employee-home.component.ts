import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { SkeletonModule } from 'primeng/skeleton';


interface SolicitacaoAberta {
  id: number; createdAt: string;
  clienteNome: string;
  descricaoEquipamento: string; }

  @Component({
    selector: 'app-employee-home',
    standalone: true,
    imports: [CommonModule, RouterModule, CardModule, TableModule, ButtonModule, TagModule, SkeletonModule],
    templateUrl: './employee-home.component.html',
    styleUrl: './employee-home.component.scss' })

    export class EmployeeHomeComponent implements OnInit {
      private router = inject(Router);
      skeletonRows = Array(5).fill({});
      loading = false;
      solicitacoes: SolicitacaoAberta[] = [{
        id: 101,
        createdAt: '2026-09-10T14:30:00',
        clienteNome: 'João Pedro',
        descricaoEquipamento: 'Notebook Dell com tela quebrada e fonte queimada' },
      { id: 102,
        createdAt: '2026-09-10T15:10:00',
        clienteNome: 'Maria Souza',
        descricaoEquipamento: 'Impressora HP travando papel no alimentador' }];

        ngOnInit(): void { } isLoading(): boolean {
          return this.loading; }

        efetuarOrcamento(id: number): void {
          this.router.navigate(['/requests', id, 'budget']); } }
