import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SolicitacaoService } from '../../services/solicitacao.service';
import { Solicitacao } from '../../models/solicitacao.model';

@Component({
  selector: 'app-cliente-home',
  standalone: true,
  imports: [CommonModule],
  template: '',
  styleUrl: './cliente-home.component.css'
})
export class ClienteHomeComponent implements OnInit {
  private solicitacaoService = inject(SolicitacaoService); // Injeção de dependência via inject()
  public solicitacoes: Solicitacao[] = [];

  ngOnInit(): void {
    // Busca as solicitações e as ordena de forma crescente por data/hora (Exigência RF003)
    // Tipagem explícita de (a: Solicitacao, b: Solicitacao) resolve os erros TS7006
    this.solicitacoes = this.solicitacaoService.listarTodas().sort((a: Solicitacao, b: Solicitacao) => {
      return new Date(a.dataHora).getTime() - new Date(b.dataHora).getTime();
    });
  }

  // Métodos de ação fictícios para testar cliques na tela
  aprovarRejeitar(id: number) { alert(`Ir para Mostrar Orçamento (RF005) da solicitação #${id}`); }
  resgatar(id: number) { alert(`Resgatando serviço #${id} (RF009)`); }
  pagar(id: number) { alert(`Ir para Pagar Serviço #${id} (RF010)`); }
  visualizar(id: number) { alert(`Visualizando dados e histórico da solicitação #${id} (RF008)`); }
}
