import { RevenueReportItem } from './revenue-report-item.model';
import { RequestStatus } from '../../maintenance-requests/models';

export interface DailyRevenue {
  date: string;
  totalRevenue: number;
  paidRequestsCount: number;
}

export interface CategoryRevenue {
  categoryId: number;
  categoryName: string;
  totalRevenue: number;
  paidRequestsCount: number;
  active: boolean;
}

export interface StatusSummary {
  status: RequestStatus;
  requestsCount: number;
}

export interface RevenueReport {
  startDate?: string;
  endDate?: string;
  totalRevenue: number;
  totalQuoted: number;
  paidRequestsCount: number;
  averageTicket: number;
  approvalRate: number;
  dailyRevenue: DailyRevenue[];
  items: RevenueReportItem[];
}

export interface ReportsDashboard {
  revenueReport: RevenueReport;
  categoryRevenue: CategoryRevenue[];
  statusSummary: StatusSummary[];
}
