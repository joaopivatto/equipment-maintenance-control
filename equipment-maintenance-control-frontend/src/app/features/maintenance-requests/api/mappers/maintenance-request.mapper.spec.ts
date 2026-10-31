import { toIsoDateTime, toMaintenanceRequest, toRequestStatus } from './maintenance-request.mapper';
import { RequestStatus } from '../../models/maintenance-request.model';

describe('maintenance-request.mapper', () => {
  it('converte datas dd/MM/yyyy HH:mm para ISO local', () => {
    expect(toIsoDateTime('05/03/2026 14:30')).toBe('2026-03-05T14:30:00');
  });

  it('mantém undefined quando não há data', () => {
    expect(toIsoDateTime(undefined)).toBeUndefined();
  });

  it('traduz status do backend', () => {
    expect(toRequestStatus('ORCADA')).toBe(RequestStatus.QUOTED);
    expect(toRequestStatus('ABERTA')).toBe(RequestStatus.OPEN);
  });

  it('mapeia os dados completos do cliente', () => {
    const request = toMaintenanceRequest({
      id: 1,
      createdAt: '05/03/2026 14:30',
      defectDescription: 'Não liga',
      status: 'ABERTA',
      customer: {
        id: 3,
        name: 'João',
        email: 'joao@email.com',
        cpf: '52998224725',
        phone: '41999990001',
        address: {
          zipCode: '80010000',
          street: 'Rua XV',
          number: 100,
          city: 'Curitiba',
          state: 'PR',
        },
      },
    });

    expect(request.customerName).toBe('João');
    expect(request.customer).toEqual({
      name: 'João',
      email: 'joao@email.com',
      cpf: '52998224725',
      phone: '41999990001',
      address: {
        zipCode: '80010000',
        street: 'Rua XV',
        number: 100,
        city: 'Curitiba',
        state: 'PR',
      },
    });
  });
});
