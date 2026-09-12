import { MaintenanceRequest, RequestStatus } from './maintenance-request.model';

describe('MaintenanceRequest', () => {
  it('should accept a valid object shape', () => {
    const request: MaintenanceRequest = {
      id: 1,
      createdAt: '2026-08-25 09:00',
      equipmentDescription: 'Notebook Dell Inspiron',
      defectDescription: 'Tela piscando sem parar',
      status: RequestStatus.OPEN,
      history: [],
    };

    expect(request).toBeTruthy();
  });
});
