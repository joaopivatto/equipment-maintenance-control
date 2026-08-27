import { Injectable } from '@angular/core';
import { Solicitacao } from '../models/solicitacao.model';

@Injectable({
  providedIn: 'root'
})
export class SolicitacaoService {
  // Massa de teste fictícia para validar todos os estados de RF003
  private listaSolicitacoes: Solicitacao[] = [
    new Solicitacao(1, '2026-08-25 09:00', 'Notebook Dell Inspiron', 'Tela piscando sem parar', 'ABERTA'),
    new Solicitacao(2, '2026-08-25 10:30', 'Impressora HP Laserjet Pro', 'Papel enroscando na saída', 'ORÇADA'),
    new Solicitacao(3, '2026-08-26 14:00', 'Desktop Gamer Core i7', 'Não liga, bipa 3 vezes', 'APROVADA'),
    new Solicitacao(4, '2026-08-26 15:15', 'Teclado Mecânico HyperX', 'Tecla Espaço parou de funcionar', 'REJEITADA'),
    new Solicitacao(5, '2026-08-27 08:30', 'Mouse Logitech MX Master', 'Clique duplo involuntário', 'ARRUMADA')
  ];

  constructor() {}

  // Retorna a lista de solicitações simuladas
  listarTodas(): Solicitacao[] {
    return this.listaSolicitacoes;
  }
}
