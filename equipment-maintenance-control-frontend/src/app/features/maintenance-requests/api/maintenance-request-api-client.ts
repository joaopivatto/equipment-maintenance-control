import { Observable } from 'rxjs';
import { MaintenanceRequest } from '../models/maintenance-request.model';

export abstract class MaintenanceRequestApiClient {
  abstract listAll(): Observable<MaintenanceRequest[]>;

  abstract findById(id: number): Observable<MaintenanceRequest | undefined>;

  abstract approve(id: number): Observable<MaintenanceRequest | undefined>;

  abstract reject(id: number, reason: string): Observable<MaintenanceRequest | undefined>;

  abstract recover(id: number): Observable<MaintenanceRequest | undefined>;

  abstract pay(id: number): Observable<MaintenanceRequest | undefined>;

  abstract performMaintenance(
    id: number,
    maintenanceDescription: string,
    maintenanceInstructions: string,
    employeeId: number,
    employeeName: string,
  ): Observable<MaintenanceRequest | undefined>;

  abstract redirect(
    id: number,
    fromEmployeeId: number,
    fromEmployeeName: string,
    toEmployeeId: number,
    toEmployeeName: string,
  ): Observable<{ request?: MaintenanceRequest; error?: string }>;
}
