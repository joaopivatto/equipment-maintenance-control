import { MaintenanceRequest } from './maintenance-request.model';

describe('MaintenanceRequest', () => {
  it('should accept a valid object shape', () => {
    const request: MaintenanceRequest = {
      id: 1,
      dataHora: '2026-08-25 09:00',
      descricaoEquipamento: 'Notebook Dell Inspiron',
      descricaoDefeito: 'Tela piscando sem parar',
      estado: 'ABERTA',
      history: [],
    };

    expect(request).toBeTruthy();
  });
});
