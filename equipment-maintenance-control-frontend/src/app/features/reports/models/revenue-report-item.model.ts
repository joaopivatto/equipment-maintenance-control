import { RequestStatus } from '../../maintenance-requests/models';

export interface RevenueReportItem {
  maintenanceRequestId: number;
  equipmentDescription: string;
  customerName: string;
  paymentDate: string;
  amount: number;
  status: RequestStatus;
}
