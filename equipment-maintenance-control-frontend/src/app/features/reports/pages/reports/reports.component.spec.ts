import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';

import { ReportsDashboard } from '../../models';
import { ReportsService } from '../../services/reports.service';
import { ReportsComponent } from './reports.component';

describe('ReportsComponent', () => {
  let component: ReportsComponent;
  let fixture: ComponentFixture<ReportsComponent>;

  const dashboard: ReportsDashboard = {
    revenueReport: {
      startDate: '2026-09-01',
      endDate: '2026-09-30',
      totalRevenue: 0,
      totalQuoted: 0,
      paidRequestsCount: 0,
      averageTicket: 0,
      approvalRate: 0,
      dailyRevenue: [],
      items: [],
    },
    categoryRevenue: [],
    statusSummary: [],
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsComponent],
      providers: [
        MessageService,
        { provide: ReportsService, useValue: { loadDashboard: () => of(dashboard) } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
