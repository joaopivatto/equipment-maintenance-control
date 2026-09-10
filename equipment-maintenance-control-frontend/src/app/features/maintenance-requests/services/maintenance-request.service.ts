import { Injectable } from '@angular/core';
import { MaintenanceRequest, RequestStatus } from '../models/maintenance-request.model';

@Injectable({
  providedIn: 'root',
})
export class MaintenanceRequestService {
  // Massa de teste fictícia para validar todos os estados possíveis da solicitação
  private listaSolicitacoes: MaintenanceRequest[] = [
    {
      id: 1,
      dataHora: '2026-08-25 09:00',
      descricaoEquipamento: 'Notebook Dell Inspiron',
      descricaoDefeito: 'Tela piscando sem parar',
      estado: RequestStatus.ABERTA,
      history: [],
    },
    {
      id: 2,
      dataHora: '2026-08-25 10:30',
      descricaoEquipamento: 'Impressora HP Laserjet Pro',
      descricaoDefeito: 'Papel enroscando na saída',
      estado: RequestStatus.ORCADA,
      budget: { id: 1, value: 180.5, createdAt: '2026-08-25 11:00', employeeId: 1 },
      history: [],
    },
    {
      id: 3,
      dataHora: '2026-08-26 14:00',
      descricaoEquipamento: 'Desktop Gamer Core i7',
      descricaoDefeito: 'Não liga, bipa 3 vezes',
      estado: RequestStatus.APROVADA,
      history: [],
    },
    {
      id: 4,
      dataHora: '2026-08-26 15:15',
      descricaoEquipamento: 'Teclado Mecânico HyperX',
      descricaoDefeito: 'Tecla Espaço parou de funcionar',
      estado: RequestStatus.REJEITADA,
      history: [],
    },
    {
      id: 5,
      dataHora: '2026-08-27 08:30',
      descricaoEquipamento: 'Mouse Logitech MX Master',
      descricaoDefeito: 'Clique duplo involuntário',
      estado: RequestStatus.ARRUMADA,
      history: [],
    },
  ];

  // Retorna a lista de solicitações simuladas
  listarTodas(): MaintenanceRequest[] {
    return this.listaSolicitacoes;
  }

  findById(id: number): MaintenanceRequest | undefined {
    return this.listaSolicitacoes.find((s) => s.id === id);
  }

  // RF006 - Aprovar serviço: ORÇADA -> APROVADA
  approve(id: number): MaintenanceRequest | undefined {
    const solicitacao = this.findById(id);
    if (!solicitacao) {
      return undefined;
    }

    solicitacao.estado = RequestStatus.APROVADA;
    solicitacao.history.push({
      status: RequestStatus.APROVADA,
      dateTime: new Date().toISOString(),
    });

    return solicitacao;
  }

  // RF007 - Rejeitar serviço: ORÇADA -> REJEITADA, com motivo obrigatório
  reject(id: number, reason: string): MaintenanceRequest | undefined {
    const solicitacao = this.findById(id);
    if (!solicitacao) {
      return undefined;
    }

    solicitacao.estado = RequestStatus.REJEITADA;
    solicitacao.rejectionReason = reason;
    solicitacao.history.push({
      status: RequestStatus.REJEITADA,
      dateTime: new Date().toISOString(),
      reason,
    });

    return solicitacao;
  }
}
