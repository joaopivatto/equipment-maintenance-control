import { BaseMockFactory } from '../../../../shared/testing';
import { HistoryEntry } from '../history-entry.model';
import { RequestStatus } from '../maintenance-request.model';

export class HistoryEntryMockFactory extends BaseMockFactory<HistoryEntry> {
  protected readonly length = 6;

  private readonly statuses = [
    RequestStatus.ABERTA,
    RequestStatus.ORCADA,
    RequestStatus.APROVADA,
    RequestStatus.REJEITADA,
    RequestStatus.ARRUMADA,
    RequestStatus.PAGA,
  ];

  private readonly dateTimes = [
    '2026-01-05T08:00:00Z',
    '2026-01-06T09:30:00Z',
    '2026-01-07T11:15:00Z',
    '2026-01-08T14:45:00Z',
    '2026-01-09T16:20:00Z',
    '2026-01-10T18:00:00Z',
  ];

  private readonly responsibles = [
    'Matheus Smith',
    'João Guilherme Johnson',
    'Samuel Brown',
    'João Davis',
    'Saulo Miller',
    'Razer Doe',
  ];

  protected build(index: number): HistoryEntry {
    return {
      status: this.statuses[index],
      dateTime: this.dateTimes[index],
      responsible: this.responsibles[index],
    };
  }
}
