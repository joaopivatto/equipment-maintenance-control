import { inject, Service } from '@angular/core';
import { forkJoin, map, Observable } from 'rxjs';
import { EquipmentCategoryApiClient } from '../../equipment-categories/api/equipment-category-api-client';
import { MaintenanceRequestApiClient } from '../../maintenance-requests/api/maintenance-request-api-client';
import {
  CategoryRevenue,
  DailyRevenue,
  ReportsDashboard,
  RevenueReport,
  StatusSummary,
} from '../models';
import {
  MaintenanceRequest,
  RequestStatus,
} from '../../maintenance-requests/models/maintenance-request.model';
import { DateService } from '../../../shared/services/date.service';
import { EquipmentCategory } from '../../equipment-categories/models/equipment-category.model';

const REVENUE_STATUSES = new Set([RequestStatus.PAID, RequestStatus.FINALIZED]);
const APPROVED_STATUSES = new Set([
  RequestStatus.APPROVED,
  RequestStatus.REDIRECTED,
  RequestStatus.REPAIRED,
  RequestStatus.PAID,
  RequestStatus.FINALIZED,
]);

@Service()
export class ReportsService {
  private readonly dateService = inject(DateService);
  private readonly categoryApiClient = inject(EquipmentCategoryApiClient);
  private readonly maintenanceRequestApiClient = inject(MaintenanceRequestApiClient);

  loadDashboard(startDate: Date, endDate: Date): Observable<ReportsDashboard> {
    return forkJoin({
      requests: this.maintenanceRequestApiClient.listAll(),
      categories: this.categoryApiClient.listAll(),
    }).pipe(
      map(({ requests, categories }) => ({
        revenueReport: this.buildRevenueReport(requests, startDate, endDate),
        categoryRevenue: this.buildCategoryRevenue(requests, categories),
        statusSummary: this.buildStatusSummary(requests, startDate, endDate),
      })),
    );
  }

  private buildRevenueReport(
    requests: MaintenanceRequest[],
    startDate: Date,
    endDate: Date,
  ): RevenueReport {
    const paidRequests = requests.filter(
      (request) =>
        REVENUE_STATUSES.has(request.status) &&
        !!request.paidAt &&
        this.isDateInRange(request.paidAt, startDate, endDate),
    );
    const quotedRequests = requests.filter(
      (request) =>
        !!request.budget && this.isDateInRange(request.budget.createdAt, startDate, endDate),
    );
    const totalRevenue = this.sumRevenue(paidRequests);
    const totalQuoted = this.sumRevenue(quotedRequests);
    const approvedRequestsCount = quotedRequests.filter((request) =>
      APPROVED_STATUSES.has(request.status),
    ).length;

    return {
      startDate: this.dateService.toIsoDate(startDate),
      endDate: this.dateService.toIsoDate(endDate),
      totalRevenue,
      totalQuoted,
      paidRequestsCount: paidRequests.length,
      averageTicket: paidRequests.length > 0 ? totalRevenue / paidRequests.length : 0,
      approvalRate:
        quotedRequests.length > 0 ? (approvedRequestsCount / quotedRequests.length) * 100 : 0,
      dailyRevenue: this.buildDailyRevenue(paidRequests),
      items: paidRequests
        .map((request) => ({
          maintenanceRequestId: request.id,
          equipmentDescription: request.equipmentDescription,
          amount: request.budget?.value ?? 0,
          paymentDate: request.paidAt ?? '',
          status: request.status,
          customerName: request.customerName ?? '',
        }))
        .sort((a, b) => a.paymentDate.localeCompare(b.paymentDate)),
    };
  }

  private buildDailyRevenue(requests: MaintenanceRequest[]): DailyRevenue[] {
    const revenueByDate = new Map<string, DailyRevenue>();

    for (const request of requests) {
      const date = request.paidAt!.slice(0, 10);
      const current = revenueByDate.get(date) ?? {
        date,
        totalRevenue: 0,
        paidRequestsCount: 0,
      };

      current.totalRevenue += request.budget?.value ?? 0;
      current.paidRequestsCount += 1;
      revenueByDate.set(date, current);
    }

    return [...revenueByDate.values()].sort((a, b) => a.date.localeCompare(b.date));
  }

  private buildCategoryRevenue(
    requests: MaintenanceRequest[],
    categories: EquipmentCategory[],
  ): CategoryRevenue[] {
    const paidRequests = requests.filter(
      (request) => REVENUE_STATUSES.has(request.status) && !!request.paidAt,
    );

    return categories.map((category) => {
      const categoryRequests = paidRequests.filter(
        (request) => request.equipmentCategoryId === category.id,
      );

      return {
        categoryId: category.id,
        categoryName: category.name,
        totalRevenue: this.sumRevenue(categoryRequests),
        paidRequestsCount: categoryRequests.length,
      };
    });
  }

  private buildStatusSummary(
    requests: MaintenanceRequest[],
    startDate: Date,
    endDate: Date,
  ): StatusSummary[] {
    const requestsInPeriod = requests.filter((request) =>
      this.isDateInRange(request.createdAt, startDate, endDate),
    );

    return Object.values(RequestStatus).map((status) => ({
      status,
      requestsCount: requestsInPeriod.filter((request) => request.status === status).length,
    }));
  }

  private isDateInRange(value: string, startDate: Date, endDate: Date): boolean {
    const date = new Date(value);
    const start = new Date(startDate);
    const end = new Date(endDate);

    start.setHours(0, 0, 0, 0);
    end.setHours(23, 59, 59, 999);

    return !Number.isNaN(date.getTime()) && date >= start && date <= end;
  }

  private sumRevenue(requests: MaintenanceRequest[]): number {
    return requests.reduce((total, request) => total + (request.budget?.value ?? 0), 0);
  }
}
