import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { EquipmentCategoryApiClient } from '../../equipment-categories/api/equipment-category-api-client';
import { MaintenanceRequestApiClient } from '../../maintenance-requests/api/maintenance-request-api-client';
import {
  MaintenanceRequest,
  RequestStatus,
} from '../../maintenance-requests/models/maintenance-request.model';
import { ReportsService } from './reports.service';

describe('ReportsService', () => {
  let service: ReportsService;

  const requests: MaintenanceRequest[] = [
    {
      id: 1,
      createdAt: '2026-09-01T08:00:00Z',
      equipmentDescription: 'Notebook',
      equipmentCategoryId: 1,
      equipmentCategoryName: 'Notebook',
      defectDescription: 'Não liga',
      status: RequestStatus.PAID,
      paidAt: '2026-09-10T10:00:00Z',
      budget: { id: 1, value: 200, createdAt: '2026-09-02T10:00:00Z', employeeId: 1 },
      history: [],
    },
    {
      id: 2,
      createdAt: '2026-09-02T08:00:00Z',
      equipmentDescription: 'Notebook',
      equipmentCategoryId: 1,
      equipmentCategoryName: 'Notebook',
      defectDescription: 'Tela quebrada',
      status: RequestStatus.FINALIZED,
      paidAt: '2026-09-10T15:00:00Z',
      budget: { id: 2, value: 300, createdAt: '2026-09-03T10:00:00Z', employeeId: 1 },
      history: [],
    },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ReportsService,
        { provide: MaintenanceRequestApiClient, useValue: { listAll: () => of(requests) } },
        {
          provide: EquipmentCategoryApiClient,
          useValue: { listAll: () => of([{ id: 1, name: 'Notebook', active: true }]) },
        },
      ],
    });
    service = TestBed.inject(ReportsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should group paid revenue by day and category', () => {
    service.loadDashboard(new Date(2026, 8, 1), new Date(2026, 8, 30)).subscribe((dashboard) => {
      expect(dashboard.revenueReport.totalRevenue).toBe(500);
      expect(dashboard.revenueReport.paidRequestsCount).toBe(2);
      expect(dashboard.revenueReport.dailyRevenue).toEqual([
        { date: '2026-09-10', totalRevenue: 500, paidRequestsCount: 2 },
      ]);
      expect(dashboard.categoryRevenue[0].totalRevenue).toBe(500);
    });
  });

  it('considera todo o histórico quando as datas não são informadas', () => {
    service.loadDashboard(null, null).subscribe((dashboard) => {
      expect(dashboard.revenueReport.startDate).toBeUndefined();
      expect(dashboard.revenueReport.endDate).toBeUndefined();
      expect(dashboard.revenueReport.totalRevenue).toBe(500);
      expect(dashboard.revenueReport.paidRequestsCount).toBe(2);
    });
  });

  it('aceita apenas data inicial ou apenas data final', () => {
    service.loadDashboard(new Date(2026, 8, 11), null).subscribe((dashboard) => {
      expect(dashboard.revenueReport.paidRequestsCount).toBe(0);
    });
    service.loadDashboard(null, new Date(2026, 8, 10)).subscribe((dashboard) => {
      expect(dashboard.revenueReport.paidRequestsCount).toBe(2);
    });
  });

  it('mantém na receita por categoria uma categoria removida que tem solicitações pagas', () => {
    const removedCategoryRequest: MaintenanceRequest = {
      ...requests[0],
      id: 3,
      equipmentCategoryId: 9,
      equipmentCategoryName: 'Monitor',
      budget: { id: 3, value: 100, createdAt: '2026-09-02T10:00:00Z', employeeId: 1 },
    };
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [
        ReportsService,
        {
          provide: MaintenanceRequestApiClient,
          useValue: { listAll: () => of([...requests, removedCategoryRequest]) },
        },
        {
          provide: EquipmentCategoryApiClient,
          useValue: { listAll: () => of([{ id: 1, name: 'Notebook', active: true }]) },
        },
      ],
    });

    TestBed.inject(ReportsService)
      .loadDashboard(null, null)
      .subscribe((dashboard) => {
        expect(dashboard.categoryRevenue).toEqual([
          { categoryId: 1, categoryName: 'Notebook', totalRevenue: 500, paidRequestsCount: 2 },
          { categoryId: 9, categoryName: 'Monitor', totalRevenue: 100, paidRequestsCount: 1 },
        ]);
      });
  });
});
