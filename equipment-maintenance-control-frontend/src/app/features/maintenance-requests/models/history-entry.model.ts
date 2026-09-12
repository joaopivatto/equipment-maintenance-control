export interface HistoryEntry {
  status: string;
  dateTime: string;
  responsible?: string;
  reason?: string;

  // RF015 - Redirecionar manutenção: registra de quem para quem a solicitação foi redirecionada
  fromEmployee?: string;
  toEmployee?: string;
}
