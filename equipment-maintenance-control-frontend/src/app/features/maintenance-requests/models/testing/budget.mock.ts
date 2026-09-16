import { BaseMockFactory } from '../../../../shared/testing';
import { Budget } from '../budget.model';

export class BudgetMockFactory extends BaseMockFactory<Budget> {
  protected readonly length = 6;

  private readonly values = [150.0, 320.5, 89.9, 1200.0, 450.75, 60.0];

  private readonly createdAts = [
    '2026-01-05T08:00:00Z',
    '2026-01-06T09:30:00Z',
    '2026-01-07T11:15:00Z',
    '2026-01-08T14:45:00Z',
    '2026-01-09T16:20:00Z',
    '2026-01-10T18:00:00Z',
  ];

  private readonly employeeIds = [1, 2, 3, 4, 5, 6];

  protected build(index: number): Budget {
    return {
      id: index + 1,
      value: this.values[index],
      createdAt: this.createdAts[index],
      employeeId: this.employeeIds[index],
    };
  }
}
