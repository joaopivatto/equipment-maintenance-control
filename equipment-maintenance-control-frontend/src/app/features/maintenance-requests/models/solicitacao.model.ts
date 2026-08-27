export class Solicitacao {
  constructor(
    public id: number = 0,
    public dataHora: string = '',
    public descricaoEquipamento: string = '',
    public descricaoDefeito: string = '',
    public estado: string = 'ABERTA' // Estados previstos: ABERTA, ORÇADA, APROVADA, REJEITADA, ARRUMADA, PAGA, etc.
  ) {}
}
