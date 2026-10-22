import { toIsoDateTime, toRequestStatus } from './maintenance-request.mapper';
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
});
