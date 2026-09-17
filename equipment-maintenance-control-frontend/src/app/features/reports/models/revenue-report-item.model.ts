import { RequestStatus } from '../../maintenance-requests/models/maintenance-request.model';

export interface RevenueReportItem {
  maintenanceRequestId: number;
  equipmentDescription: string;
  customerName: string;
  paymentDate: string;
  amount: number;
  status: RequestStatus;
}
